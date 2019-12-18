package com.example.suixinplayer.service;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.ArrayMap;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.suixinplayer.R;
import com.example.suixinplayer.app.App;
import com.example.suixinplayer.bean.SongBean;
import com.example.suixinplayer.db.DBUtil;
import com.example.suixinplayer.liveDataBus.event.AddSong;
import com.example.suixinplayer.liveDataBus.event.ChangePlayModeEvent;
import com.example.suixinplayer.liveDataBus.event.PlayEvet;
import com.example.suixinplayer.liveDataBus.event.PlayNextSongEvent;
import com.example.suixinplayer.liveDataBus.event.PlayPreSongEvent;
import com.example.suixinplayer.liveDataBus.event.StopPlayEvent;
import com.example.suixinplayer.liveDataBus.event.UpDateUI;
import com.example.suixinplayer.network.ApiSongDetailService;
import com.example.suixinplayer.other.videocache.CacheListener;
import com.example.suixinplayer.ui.activity.PlayActivity;
import com.example.suixinplayer.uitli.SharPUtil;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Observable;

import javax.security.auth.login.LoginException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MusicPlayService extends Service {
    MediaPlayer mMediaPlayer = new MediaPlayer();
    private String preHash = "";
    private String playUrl;
    private String imgUri;
    private int duration;
    private ChangePlayModeEvent changePlayModeEvent = new ChangePlayModeEvent();
    private String lrc = "没有找到歌词";
    private final int PLAYPRE = 1;
    private final int PLAYORPUASE = 2;
    private final int PLAYNEXT = 3;
    private final int FAV = 4;
    private final int CLOSE = 5;
    private int position;
    private NotificationManager manager;
    private RemoteViews remoteViews;
    private NotificationCompat.Builder builder;
    private Notification notification;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("TAG", "onBind: ");
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(2, getNotification(manager));
        return new MyBinder();
    }

    private Notification getNotification(NotificationManager manager) {
        builder = new NotificationCompat.Builder(this, "2");
        //android 8.0 适配     需要配置 通知渠道NotificationChannel
        NotificationChannel b;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            b = new NotificationChannel("2", "随心听", NotificationManager.IMPORTANCE_MIN);
            manager.createNotificationChannel(b);
            builder.setChannelId("2");
        }
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.default_disc);
        notification = builder.setOngoing(true)
                /* .setContentTitle("这是Title")
                 .setContentText("这是ContentText")
                 .setSubText("这是SubText")*/
                .setContent(getRemoteViews())
                .setSmallIcon(R.mipmap.love)
                .setLargeIcon(largeIcon)
                .setSound(null)
                .build();
        return notification;
    }

    private RemoteViews getRemoteViews() {

        remoteViews = new RemoteViews(getPackageName(), R.layout.notification);
        if (App.playingListBean.getPlayList().size() == 0) {
            remoteViews.setTextViewText(R.id.notification_song_name, "还没选择播放歌曲");
            remoteViews.setTextViewText(R.id.notification_author, "null");
        } else {
            remoteViews.setTextViewText(R.id.notification_song_name, App.playingListBean.getPlayList().get(App.playingListBean.getPosition()).songName);
            remoteViews.setTextViewText(R.id.notification_author, App.playingListBean.getPlayList().get(App.playingListBean.getPosition()).author);
        }
        remoteViews.setOnClickPendingIntent(R.id.notification_pre, getPrePendingIntent());
        remoteViews.setOnClickPendingIntent(R.id.notification_play_pause, getPlayOrStopPrePendingIntent());
        remoteViews.setOnClickPendingIntent(R.id.notification_next, getNextPendingIntent());
        remoteViews.setOnClickPendingIntent(R.id.notification_fav, getFavPendingIntent());
        remoteViews.setOnClickPendingIntent(R.id.close, getClosePendingIntent());
        remoteViews.setOnClickPendingIntent(R.id.notification, getNotificationPendingIntent());
        //  remoteViews.setOnClickFillInIntent(R.id.notification, new Intent(this, PlayActivity.class));
        return remoteViews;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int value = intent.getIntExtra("notification", 0);

        switch (value) {
            case PLAYPRE:
                Log.i("TAG", "onStartCommand: PLAYPRE " + value);
                playPre();
                break;

            case PLAYORPUASE:
                if (mMediaPlayer != null) {
                    if (mMediaPlayer.isPlaying()) {
                        puase();
                    } else {
                        continuePlay();
                    }
                }

                break;

            case PLAYNEXT:
                Log.i("TAG", "onStartCommand: PLAYNEXT " + value);
                playNext();
                break;

            case FAV:
                Log.i("TAG", "onStartCommand: FAV " + value);
                break;

            case CLOSE:
                Log.i("TAG", "onStartCommand: CLOSE " + value);
                close();
                break;

        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void playNext() {
        if (App.playingListBean.getPosition() < App.playingListBean.getPlayList().size() - 1) {
            App.playingListBean.setPosition(App.playingListBean.getPosition() + 1);
            PlayEvet playEvet = App.playingListBean.getPlayList().get(App.playingListBean.getPosition());
            if (!playEvet.isLocal) {
                hash2Url(playEvet);
            } else {
                prepare(playEvet.url, playEvet, null);
            }
            remoteViews.setImageViewResource(R.id.notification_play_pause, R.mipmap.ic_pause);
            manager.notify(2, notification);
        }
    }

    public void playPre() {
        if (App.playingListBean.getPosition() > 0) {
            App.playingListBean.setPosition(App.playingListBean.getPosition() - 1);
            PlayEvet playEvet = App.playingListBean.getPlayList().get(App.playingListBean.getPosition());
            if (!playEvet.isLocal) {
                hash2Url(playEvet);
            } else {
                prepare(playEvet.url, playEvet, null);
            }

            remoteViews.setImageViewResource(R.id.notification_play_pause, R.mipmap.ic_pause);
            manager.notify(2, notification);
        }
        return;
    }

    public void puase() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            Log.i("TAG", "puase: ");
            mMediaPlayer.pause();
           /* remoteViews.setImageViewResource(R.id.notification_play_pause, R.mipmap.ic_play);
            manager.notify(2, notification);*/

        }
        UpDateUI upDateUI = new UpDateUI();
        upDateUI.author = "";
        upDateUI.hash = "";
        upDateUI.isFree = 1;
        upDateUI.songName = "";
        upDateUI.duration = 1;
        upDateUI.imaUri = "";
        upDateUI.isPrepar = true;
        upDateUI.lrc = "";
        upDateUI.isPlaying = "仅暂停";
        LiveEventBus.get("UpDateUI", UpDateUI.class).post(upDateUI);

    }

    public void continuePlay() {
        if (mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
            Log.i("TAG", "continuePlay: ");
            mMediaPlayer.start();
            /*remoteViews.setImageViewResource(R.id.notification_play_pause, R.mipmap.ic_pause);
            manager.notify(2, notification);*/
        }
        UpDateUI upDateUI = new UpDateUI();
        upDateUI.author = "";
        upDateUI.hash = "";
        upDateUI.isFree = 1;
        upDateUI.songName = "";
        upDateUI.duration = 1;
        upDateUI.imaUri = "";
        upDateUI.isPrepar = true;
        upDateUI.lrc = "";
        upDateUI.isPlaying = "仅播放";
        LiveEventBus.get("UpDateUI", UpDateUI.class).post(upDateUI);
    }

    public void collectionSong() {
        DBUtil.getDatabase(getApplicationContext());
        // DBUtil.
    }

    public void close() {
        if (manager != null) {
            manager.cancel(2);
        }
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.AppTask> appTaskList = activityManager.getAppTasks();
        for (ActivityManager.AppTask appTask : appTaskList) {
            appTask.finishAndRemoveTask();
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);

    }


    /*
     * 上一曲
     * */
    private PendingIntent getPrePendingIntent() {
        Intent intent = new Intent(this, MusicPlayService.class);
        intent.putExtra("notification", PLAYPRE);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    /*
     * 播放或者停止
     * */
    private PendingIntent getPlayOrStopPrePendingIntent() {
        Intent intent = new Intent(this, MusicPlayService.class);
        intent.putExtra("notification", PLAYORPUASE);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 2, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;

    }

    /*
     * 下一曲
     * */
    private PendingIntent getNextPendingIntent() {
        Intent intent = new Intent(this, MusicPlayService.class);
        intent.putExtra("notification", PLAYNEXT);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 3, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    /*
     * 添加到收藏的歌曲
     * */
    private PendingIntent getFavPendingIntent() {
        Intent intent = new Intent(this, MusicPlayService.class);
        intent.putExtra("notification", FAV);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 4, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    /*
     * 退出程序
     * */
    private PendingIntent getClosePendingIntent() {
        Intent intent = new Intent(this, MusicPlayService.class);
        intent.putExtra("notification", CLOSE);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 5, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    private PendingIntent getNotificationPendingIntent() {
        Activity activity = App.mActivityManager.currentActivity();
        Log.i("TAGH", "getnotificationPendingIntent: " + activity.toString());
        Intent appIntent = new Intent(this, activity.getClass());
        appIntent.setAction(Intent.ACTION_MAIN);
        appIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        //设置启动模式
        appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 6, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }


    @Override
    public void onCreate() {
        Log.i("TAG", "onCreate: ");
        super.onCreate();


    }


    public class MyBinder extends Binder {
        public void puase() {
            MusicPlayService.this.puase();
        }

        public void continuePlay() {
            MusicPlayService.this.continuePlay();
        }

        private MyBinder() {
            Log.i("TAG", "MyBinder: ");
            /*
             * 监听播放完毕操作
             * */
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Log.i("TAG", "onCompletion: "+changePlayModeEvent.mode);
                    switch (changePlayModeEvent.mode) {
                        case ORDER:
                            orderMode(mp);
                            break;

                        case RANDOM:
                            randomMode(mp);
                            break;

                        case SINGLE:
                            singleMode(mp);
                            break;
                    }
                }
            });

            getMessege();
        }

        public MediaPlayer getMediaPlayer() {
            return mMediaPlayer;
        }

        public boolean getPlayState() {
            if (mMediaPlayer.isPlaying()) {
                return true;
            } else return false;
        }

        public int getDuration() {
            return duration;
        }


        private void getMessege() {
            /*
             * 接收播放模式的消息
             * */
            LiveEventBus.get("PLAYMODE", ChangePlayModeEvent.class).observeForever(new androidx.lifecycle.Observer<ChangePlayModeEvent>() {
                @Override
                public void onChanged(ChangePlayModeEvent event) {
                    changePlayModeEvent.mode = event.mode;
                    Log.i("TAG", "onCompletion1111: "+changePlayModeEvent.mode);
                }
            });
            LiveEventBus
                    .get("Play", PlayEvet.class)
                    .observeForever(s -> {
                                //  Log.i("TAG", "getMessege: " + (s.hash == null) + (preHash == null));
                                if (s.isLocal) {
                                  //  Log.i("TAG", "getMessege: " + (s.hash == null) + (preHash == null) + "  " + s.url);
                                    prepare(s.url, s, null);
                                } else {
                                    if (preHash.equals(s.hash)) {
                                        if (mMediaPlayer.isPlaying()) {
                                            mMediaPlayer.pause();
                                        } else {
                                            mMediaPlayer.start();
                                        }
                                    } else {
                                        hash2Url(s);

                                    }
                                }
                            }
                    );
            LiveEventBus
                    .get("Pre", PlayPreSongEvent.class)
                    .observeForever(s -> {
                                playPre();
                            }
                    );
            LiveEventBus
                    .get("Next", PlayNextSongEvent.class)
                    .observeForever(s -> {
                                playNext();
                            }
                    );


            LiveEventBus.get("UpDateUI", UpDateUI.class).observeStickyForever(new androidx.lifecycle.Observer<UpDateUI>() {
                @Override
                public void onChanged(UpDateUI upDateUI) {
                    if (upDateUI.isPlaying.equals("仅播放")) {
                        Log.i("TAG", "updateUi: 仅播放");
                        remoteViews.setImageViewResource(R.id.notification_play_pause, R.mipmap.ic_pause);
                    } else if (upDateUI.isPlaying.equals("仅暂停")) {
                        Log.i("TAG", "updateUi: 仅暂停");
                        remoteViews.setImageViewResource(R.id.notification_play_pause, R.mipmap.ic_play);

                    } else {
                        Log.i("TAG", "onChanged: 通知栏收到更新通知");
                        remoteViews.setImageViewResource(R.id.notification_play_pause, R.mipmap.ic_pause);
                        remoteViews.setTextViewText(R.id.notification_song_name, upDateUI.songName);
                        remoteViews.setTextViewText(R.id.notification_author, upDateUI.author);
                        //Picasso会自动刷新notification
                        if (upDateUI.imaUri!=null){
                            Picasso.get().load(upDateUI.imaUri).into(remoteViews, R.id.author_image, 2, notification);
                        }else {
                            remoteViews.setImageViewResource(R.id.author_image, R.mipmap.ic_disc);
                        }

                   /*
                    不变的参数需要改动
                    builder.setOngoing(true)
                            .setContent(remoteViews)
                            .setSmallIcon(R.mipmap.love)
                            .build();*/

                    }
                    //改完后要刷新notification
                    manager.notify(2, notification);
                }
            });
        }


        /*
         * 单曲模式
         * */
        private void singleMode(MediaPlayer mediaPlayer) {
            Log.i("TAG", "singleMode: ");
            mediaPlayer.seekTo(0);
            mediaPlayer.start();
        }

        /*
         * 顺序播放模式
         * */
        private void orderMode(MediaPlayer mediaPlayer) {
            playNext();
        }

        /*
         * 随机播放模式
         * */
        private void randomMode(MediaPlayer mediaPlayer) {
            Log.i("TAG", "randomMode: ");
            int nb = (int) (1 + Math.random() * (App.playingListBean.getPlayList().size() - 1 + 0));
            PlayEvet playEvet = App.playingListBean.getPlayList().get(nb);
            prepare(playEvet.url, playEvet, null);
            remoteViews.setImageViewResource(R.id.notification_play_pause, R.mipmap.ic_pause);
            manager.notify(2, notification);
        }

    }


    //用hash取获取包含歌词和播放Url的SongBean对象(还有付费信息和音质信息)  发送播放事件给service
    public void hash2Url(PlayEvet playEvet) {
        if (preHash.equals(playEvet.hash)) {
            return;
        }
        SharPUtil.putString(getApplication(), "LASTSONG", playEvet.songName + "-" + playEvet.author);

        preHash = playEvet.hash != null ? playEvet.hash : "";
        //http://www.kugou.com/yy/index.php?r=play/getdata&hash=79b77708dca79378af538f9a518f1b76/
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.kugou.com/")//基础URL 建议以 / 结尾
                .addConverterFactory(GsonConverterFactory.create())//设置 Json 转换器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//RxJava 适配器
                // .client(httpClient)
                .build();
        ArrayMap<String, String> headMap = new ArrayMap<>();
        headMap.put("r", "play/getdata");
        headMap.put("hash", playEvet.hash);
        ApiSongDetailService rxjavaService = retrofit.create(ApiSongDetailService.class);
        rxjavaService.getListBean(headMap)
                .subscribeOn(Schedulers.io())//IO线程加载数据
                .observeOn(AndroidSchedulers.mainThread())//主线程显示数据
                .subscribe(new Observer<SongBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        // Log.i("SearchFragment", "onSubscribe");
                    }

                    @Override
                    public void onNext(SongBean songBean) {
                        //  Log.i("SearchFragment", "onNext: " + songBean.getData().getSong_name() + " " + songBean.getData().getAudio_name());
                        //保存播放地址
                        playUrl = songBean.getData().getPlay_url();
                        imgUri = songBean.getData().getImg();
                        lrc = songBean.getData().getLyrics();
                    }

                    @Override
                    public void onError(Throwable e) {
                        prepare(playUrl, playEvet, imgUri);
                        //  Log.i("SearchFragment", "onError");
                    }

                    @Override
                    public void onComplete() {
                        prepare(playUrl, playEvet, imgUri);
                        //  Log.i("SearchFragment", "onComplete");
                    }
                });
    }


    private void prepare(String url, PlayEvet playEvet, String imgUri) {
        mMediaPlayer.reset();
        try {
            if (playEvet.hash == null && playEvet.isPrepare) {
                return;    //第一次运行程序点击播放为null
            }
            Uri uri = Uri.parse(url);
            mMediaPlayer.setDataSource(getApplication(), uri);
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(mp -> {
                  Log.d("TAG", "onPrepared: 播放 " + mp.getDuration());
                duration = mp.getDuration();
                if (!playEvet.isPrepare) {
                    play(mp, playEvet);
                } else {
                    /*
                     * 程序重启后再次进入,用于上次播放页面的恢复
                     * upDateUI.isPrepar 为true的时候表示这时只需要准备,不需要播放
                     * */
                    UpDateUI upDateUI = new UpDateUI();
                    upDateUI.author = playEvet.author;
                    upDateUI.hash = playEvet.hash;
                    upDateUI.isFree = playEvet.isFree;
                    upDateUI.songName = playEvet.songName;
                    upDateUI.duration = mp.getDuration();
                    upDateUI.imaUri = imgUri;
                    upDateUI.isPrepar = true;
                    upDateUI.lrc = lrc;
                    upDateUI.isPlaying = "暂停";
                    LiveEventBus.get("UpDateUI", UpDateUI.class).post(upDateUI);
                    lrc = "";
                }

            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void play(MediaPlayer mp, PlayEvet playEvet) {
        //开始播放
        mp.start();
        //更新界面
        UpDateUI upDateUI = new UpDateUI();
        upDateUI.author = playEvet.author;
        upDateUI.hash = playEvet.hash;
        upDateUI.isFree = playEvet.isFree;
        upDateUI.songName = playEvet.songName;
        upDateUI.duration = mp.getDuration();
        upDateUI.imaUri = imgUri;
        upDateUI.lrc = lrc;
        upDateUI.isPlaying = "播放";
        // Log.i("TAG", "play: lrc:" + lrc);
        LiveEventBus.get("UpDateUI", UpDateUI.class).post(upDateUI);
        LiveEventBus.get("ADDSONG", AddSong.class).post(new AddSong());
        lrc = "";

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // Log.i("TAG", "onDestroy: ");
        if (mMediaPlayer != null)
            mMediaPlayer.release();
        if (mMediaPlayer != null)
            mMediaPlayer = null;
    }


}
