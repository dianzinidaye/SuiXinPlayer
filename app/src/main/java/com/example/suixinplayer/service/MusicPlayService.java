package com.example.suixinplayer.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.ArrayMap;
import android.util.Log;
import android.widget.SeekBar;

import androidx.annotation.Nullable;

import com.example.suixinplayer.app.App;
import com.example.suixinplayer.bean.SongBean;
import com.example.suixinplayer.liveDataBus.event.PlayEvet;
import com.example.suixinplayer.network.ApiSongDetailService;
import com.example.suixinplayer.other.videocache.CacheListener;
import com.example.suixinplayer.uitli.SharPUtil;
import com.jeremyliao.liveeventbus.LiveEventBus;

import java.io.File;
import java.io.IOException;
import java.util.Observable;

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

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public class MyBinder extends Binder {
        public MyBinder() {
            getMessege();
        }


        public boolean getPlayState() {
            if (mMediaPlayer.isPlaying()) {
                return true;
            } else return false;
        }

        void getMessege() {
            LiveEventBus
                    .get("Play", PlayEvet.class)
                    .observeForever(s -> {
                        Log.i("xiaohuge", "getMessege: 收到一条信息 " + s.paly + "  " + s.hash + "  preHash" + preHash);
                        if (preHash.equals(s.hash)) {
                            Log.i("TAG", "getMessege:  preHash == s.hash");
                            if (mMediaPlayer.isPlaying()) {
                                mMediaPlayer.pause();
                                Log.i("xiaohuge", "getMessege: 收到一条信息    mMediaPlayer.pause");
                            } else {
                                mMediaPlayer.start();
                                Log.i("xiaohuge", "getMessege: 收到一条信息    mMediaPlayer.start");
                            }
                        } else {
                            Log.i("xiaohuge", "getMessege:  preHash != s.hash");
                            hash2Url(s.hash);
                            preHash = s.hash;
                            //  SharPUtil.putString(getApplication(), "LASTSONG",s.songName+"-"+s.author);
                        }

                    });
        }


        //用hash取获取包含歌词和播放Url的SongBean对象(还有付费信息和音质信息)  发送播放事件给service
        private void hash2Url(String hashString) {
            //http://www.kugou.com/yy/index.php?r=play/getdata&hash=79b77708dca79378af538f9a518f1b76/
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://www.kugou.com/")//基础URL 建议以 / 结尾
                    .addConverterFactory(GsonConverterFactory.create())//设置 Json 转换器
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//RxJava 适配器
                    // .client(httpClient)
                    .build();
            ArrayMap<String, String> headMap = new ArrayMap<>();
            headMap.put("r", "play/getdata");
            headMap.put("hash", hashString);
            ApiSongDetailService rxjavaService = retrofit.create(ApiSongDetailService.class);
            rxjavaService.getListBean(headMap)
                    .subscribeOn(Schedulers.io())//IO线程加载数据
                    .observeOn(AndroidSchedulers.mainThread())//主线程显示数据
                    .subscribe(new Observer<SongBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            Log.i("SearchFragment", "onSubscribe");
                        }

                        @Override
                        public void onNext(SongBean songBean) {
                            Log.i("SearchFragment", "onNext: " + songBean.getData().getSong_name() + " " + songBean.getData().getAudio_name());
                            //保存播放地址
                            playUrl = songBean.getData().getPlay_url();
                          /*  songName = songBean.getData().getSong_name();
                            author = songBean.getData().getAuthor_name();
                            is_free_part = songBean.getData().getIs_free_part();
                            hash = songBean.getData().getHash();*/
                        }

                        @Override
                        public void onError(Throwable e) {
                            //如果上次有播放过,则在断网的时候播放本地缓存的
                            commonPart(playUrl);
                            Log.i("SearchFragment", "onError");
                        }

                        @Override
                        public void onComplete() {
                            commonPart(playUrl);
                            Log.i("SearchFragment", "onComplete");
                        }
                    });
        }

        private void commonPart(String url) {
            play(url);
        }


        public void play(String url) {
            mMediaPlayer.reset();
            Uri uri = Uri.parse(url);
            try {
                mMediaPlayer.setDataSource(getApplication(), uri);
                mMediaPlayer.prepareAsync();
                mMediaPlayer.setOnPreparedListener(mp -> {
                    Log.d("TAG", "onPrepared: 播放 " + mp.getDuration());
                    mp.start();
                });
                mMediaPlayer.setOnCompletionListener(mp -> {
                    play(url);

                });

                Log.i("TAG", "playOrStop: service");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public MediaPlayer getMediaPlayer() {
            return mMediaPlayer;
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null)
            mMediaPlayer.release();
        if (mMediaPlayer != null)
            mMediaPlayer = null;
    }


}
