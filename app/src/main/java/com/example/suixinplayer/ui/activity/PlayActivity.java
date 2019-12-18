package com.example.suixinplayer.ui.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;


import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.suixinplayer.R;
import com.example.suixinplayer.adapter.PopUpWindowRecyclerViewPresentListAdapter;
import com.example.suixinplayer.adapter.PopUpWindowRecyclerViewSongListAdapter;
import com.example.suixinplayer.app.App;
import com.example.suixinplayer.base.BaseActivity;
import com.example.suixinplayer.callback.PopRecyclerViewSelectOnclickListener;
import com.example.suixinplayer.db.DBUtil;
import com.example.suixinplayer.liveDataBus.event.AddSong;
import com.example.suixinplayer.liveDataBus.event.ChangePlayModeEvent;
import com.example.suixinplayer.liveDataBus.event.PlayNextSongEvent;
import com.example.suixinplayer.liveDataBus.event.PlayPreSongEvent;
import com.example.suixinplayer.liveDataBus.event.UpDateUI;
import com.example.suixinplayer.service.MusicPlayService;
import com.example.suixinplayer.uitli.CommandUtil;
import com.example.suixinplayer.uitli.GetLrcUtil;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.squareup.picasso.Picasso;

import me.wcy.lrcview.LrcView;


public class PlayActivity extends BaseActivity implements View.OnClickListener, LrcView.OnPlayClickListener, View.OnTouchListener, PopRecyclerViewSelectOnclickListener, MediaPlayer.OnBufferingUpdateListener {
    private ImageView stop_play;
    private ImageView pre;
    private ImageView next;
    String TAG = "TAG";
    private ImageView back;
    private MusicPlayService.MyBinder binder;
    private TextView songName, author;
    private Observer<UpDateUI> observer;
    private ImageView ivPlayMode;
    private ImageView ivPlaylist;
    private ImageView ivLovesong;
    private ImageView ivLrc;
    private ImageView iv_singer;
    private LrcView lrcView;
    private SeekBar seekBar;
    private FrameLayout img_lrc;
    private final int UPDATE = 122;
    private boolean visiabe = true;
    private int index = 0;
    private int ivModel[] = {R.mipmap.play_mode_order, R.mipmap.play_mode_random, R.mipmap.play_mode_single};
    private PopupWindow popupWindowSongList;
    private View popRootView;
    private RecyclerView recyclerView;
    private PopUpWindowRecyclerViewPresentListAdapter adapter;
    private int position = 0;
    public static int playMode = 0;
    private String lyricUrl;
    private TextView current_time;
    private TextView duration;

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        Log.i("TAG", "onBufferingUpdate: PlayActivity  ");
        seekBar.setSecondaryProgress(percent * seekBar.getMax());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (binder.getPlayState()) {
            Log.i(TAG, "onRestart: 在播放");
            stop_play.setImageResource(R.drawable.ic_pause_60dp);
        } else {
            Log.i(TAG, "onRestart: 不播放");
            stop_play.setImageResource(R.drawable.ic_play_60dp);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected int getLayoutResId() {
        setStatusBarFullTransparent();
        return R.layout.activity_play;
    }

    @Override
    protected void initView() {
        adapter = new PopUpWindowRecyclerViewPresentListAdapter(this, App.playingListBean, this);
        stop_play = findViewById(R.id.stop_play);
        pre = findViewById(R.id.pre);
        next = findViewById(R.id.next);
        back = findViewById(R.id.back);
        songName = findViewById(R.id.textView1);
        author = findViewById(R.id.textView2);
        ivPlayMode = findViewById(R.id.playMode);
        ivPlaylist = findViewById(R.id.playlist);
        ivLovesong = findViewById(R.id.lovesong);
        ivLrc = findViewById(R.id.lrc);
        seekBar = findViewById(R.id.seekBar);
        img_lrc = findViewById(R.id.img_lrc);
        iv_singer = findViewById(R.id.iv_singer);
        lrcView = findViewById(R.id.lrcView);
        current_time = findViewById(R.id.current_time);
        duration = findViewById(R.id.duration);

        lrcView.setDraggable(true, this::onPlayClick);
        //  lrcView.setLabel("没有找到歌词");

        ivPlaylist.setOnClickListener(this::onClick);
        ivPlayMode.setOnClickListener(this::onClick);
        ivLovesong.setOnClickListener(this::onClick);
        ivLrc.setOnClickListener(this::onClick);
        stop_play.setOnClickListener(this::onClick);
        pre.setOnClickListener(this::onClick);
        next.setOnClickListener(this::onClick);
        back.setOnClickListener(this::onClick);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                binder.getMediaPlayer().seekTo(seekBar.getProgress());
                if (!binder.getPlayState()) {
                    binder.getMediaPlayer().start();
                    stop_play.setImageResource(R.drawable.ic_pause_60dp);
                }
            }
        });

    }

    private void updateUi() {
        observer = upDateUI -> {
            Log.i(TAG, "updateUi: 收到UI更新信息");
            if (upDateUI.isPlaying.equals("仅播放")) {
                stop_play.setImageResource(R.drawable.ic_pause_60dp);
            } else if (upDateUI.isPlaying.equals("仅暂停")) {
                stop_play.setImageResource(R.drawable.ic_play_60dp);
            } else {
                if (upDateUI.imaUri != null) {
                    Picasso.get().load(upDateUI.imaUri).into(iv_singer);
                }
                duration.setText(CommandUtil.getFormatMMSS(binder.getDuration()));
                ivPlayMode.setImageResource(ivModel[playMode]);
                songName.setText(upDateUI.songName);
                author.setText(upDateUI.author);
                stop_play.setImageResource(R.drawable.ic_pause_60dp);
                seekBar.setMax(upDateUI.duration);
                if (!upDateUI.isPrepar) {
                    stop_play.setImageResource(R.drawable.ic_pause_60dp);
                } else {
                    stop_play.setImageResource(R.drawable.ic_play_60dp);
                }
                lyricUrl = upDateUI.lrc;
                //if (!lyricUrl.equals("")) {
                    lrcView.loadLrc(lyricUrl);
              //  }
            }
        };
        LiveEventBus.get("UpDateUI", UpDateUI.class).observeStickyForever(observer);
    }

    @Override
    protected void initData() {
        Intent intent = new Intent(this, MusicPlayService.class);
        ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                binder = (MusicPlayService.MyBinder) service;
                MediaPlayer mediaPlayer = binder.getMediaPlayer();
                mediaPlayer.setOnBufferingUpdateListener(PlayActivity.this);
                seekBar.setMax(binder.getDuration());
                Log.d("TAG", "onPrepared: 播放  主线程" + binder.getDuration());
                handler.sendEmptyMessage(UPDATE);
                updateUi();
                if (binder.getPlayState()) {
                    stop_play.setImageResource(R.drawable.ic_pause_60dp);
                } else {
                    stop_play.setImageResource(R.drawable.ic_play_60dp);
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.pre:
                //  if (App.playingListBean.position > 0) {
                // App.playingListBean.position -= 1;
                LiveEventBus.get("Pre", PlayPreSongEvent.class).post(new PlayPreSongEvent());
                // }
                Log.i(TAG, "onClick: pre");
                break;
            case R.id.stop_play:
                if (binder.getPlayState()) {
                    binder.puase();
                    stop_play.setImageResource(R.drawable.ic_play_60dp);
                } else {
                    binder.continuePlay();
                    stop_play.setImageResource(R.drawable.ic_pause_60dp);
                }
                break;
            case R.id.next:
                Log.i(TAG, "onClick: next");
                //  if (App.playingListBean.position < App.playingListBean.playList.size() - 1) {
                // App.playingListBean.position += 1;
                LiveEventBus.get("Next", PlayNextSongEvent.class).post(new PlayNextSongEvent());
                // }

                break;
                /*
                播放模式(循环,随机,顺序播放3种)
                 */
            case R.id.playMode:
                switch (playMode) {
                    case 0:
                        ivPlayMode.setImageResource(ivModel[1]);

                        playMode++;
                        ChangePlayModeEvent eventOrder = new ChangePlayModeEvent();
                        eventOrder.mode = ChangePlayModeEvent.MODE.RANDOM;
                        LiveEventBus.get("PLAYMODE", ChangePlayModeEvent.class).post(eventOrder);
                        break;
                    case 1:
                        ivPlayMode.setImageResource(ivModel[2]);
                        playMode++;
                        ChangePlayModeEvent eventRandom = new ChangePlayModeEvent();
                        eventRandom.mode = ChangePlayModeEvent.MODE.SINGLE;
                        LiveEventBus.get("PLAYMODE", ChangePlayModeEvent.class).post(eventRandom);
                        break;
                    case 2:
                        ivPlayMode.setImageResource(ivModel[0]);
                        playMode = 0;
                        ChangePlayModeEvent eventSingle = new ChangePlayModeEvent();
                        eventSingle.mode = ChangePlayModeEvent.MODE.ORDER;
                        LiveEventBus.get("PLAYMODE", ChangePlayModeEvent.class).post(eventSingle);
                        break;
                }


                break;

            case R.id.playlist:
                if (popupWindowSongList == null) {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    popRootView = inflater.inflate(R.layout.popupwindow_songlist, null, false);//引入弹窗布局
                    // PopUpWindow 传入 ContentView
                    int height = this.getResources().getDisplayMetrics().heightPixels;
                    popupWindowSongList = new PopupWindow(popRootView, ViewGroup.LayoutParams.MATCH_PARENT, height / 5 * 3);
                    popupWindowSongList.setTouchable(true);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(popRootView.getContext());

                    recyclerView = popRootView.findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adapter);

                    // 设置背景
                    popupWindowSongList.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

                    // 外部点击事件
                    popupWindowSongList.setOutsideTouchable(true);
                    popupWindowSongList.setAnimationStyle(R.style.popwin_anim_style);
                    popupWindowSongList.showAtLocation(popRootView, Gravity.BOTTOM, 0, 0);
                    recyclerView.scrollToPosition(position + 1);
                    // recyclerView.findViewHolderForAdapterPosition(position).itemView.setBackgroundColor(Color.RED);
                } else {
                    popupWindowSongList.showAtLocation(popRootView, Gravity.BOTTOM, 0, 0);
                    adapter.notifyDataSetChanged();
                }
                popupWindowSongList.showAtLocation(popRootView, Gravity.BOTTOM, 0, 0);
                break;

            case R.id.lovesong:
                DBUtil.addDate2Table(this, App.playingListBean.getPlayList().get(App.playingListBean.getPosition()), "我喜欢");
                LiveEventBus.get("ADDSONG", AddSong.class).post(new AddSong());
                break;
            case R.id.lrc:
                if (!lyricUrl.equals("")) {
                    if (visiabe) {
                        lrcView.setVisibility(View.INVISIBLE);
                    } else {
                        lrcView.setVisibility(View.VISIBLE);
                    }
                    visiabe = !visiabe;
                }else {
                    searchLrc();
                }
                break;


        }

    }

    private void searchLrc() {
        GetLrcUtil.getLrcUtil(songName.getText().toString(), null, lrcView,iv_singer);
    }

    /*
     * 更新进度条
     * */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == UPDATE) {
                current_time.setText(CommandUtil.getFormatMMSS(binder.getMediaPlayer().getCurrentPosition()));
                seekBar.setProgress(binder.getMediaPlayer().getCurrentPosition());
                //   Log.i("TAG", "handleMessage: " + binder.getMediaPlayer().getCurrentPosition() + "  " + seekBar.getMax());
                lrcView.updateTime(binder.getMediaPlayer().getCurrentPosition());

            }
            handler.sendEmptyMessageDelayed(UPDATE, 500);

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LiveEventBus.get("UpDateUI", UpDateUI.class).removeObserver(observer);

    }


    @Override
    public boolean onPlayClick(long time) {

        // Log.i(TAG, "onPlayClick: 拉动的time"+time);
        binder.getMediaPlayer().seekTo((int) time);
        seekBar.setProgress((int) time);
        return false;
    }

    /**
     * 全透状态栏
     */
    protected void setStatusBarFullTransparent() {
        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //虚拟键盘也透明
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public void deal(int position, View v) {
        App.playingListBean.position = position;
        this.position = position;
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {

        if (popupWindowSongList != null && popupWindowSongList.isShowing()) {
            popupWindowSongList.dismiss();
        } else {
            super.onBackPressed();
        }
    }
}
