package com.example.suixinplayer.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.lifecycle.Observer;

import com.example.suixinplayer.R;
import com.example.suixinplayer.app.App;
import com.example.suixinplayer.base.BaseActivity;
import com.example.suixinplayer.bean.SongInMainActivityBean;
import com.example.suixinplayer.liveDataBus.event.PlayEvet;
import com.example.suixinplayer.liveDataBus.event.UpDateUI;
import com.example.suixinplayer.service.MusicPlayService;
import com.jeremyliao.liveeventbus.LiveEventBus;


public class PlayActivity extends BaseActivity implements View.OnClickListener {
    private ImageView stop_play;
    private ImageView pre;
    private ImageView next;
    String TAG = "TAG";
    boolean isLoveSong;
    private ImageView back;
    private PlayEvet playEvet;
    private MusicPlayService.MyBinder binder;
    private TextView songName, author;
    //private SongInMainActivityBean mSongInMainActivityBean;
    private Observer<UpDateUI> observer;


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
        // model.setPlayEver(model.getPlayEver());
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_play;
    }

    @Override
    protected void initView() {
       // Intent intent = getIntent();
       // mSongInMainActivityBean = (SongInMainActivityBean) intent.getSerializableExtra("FMainActivity");
       // playEvet = mSongInMainActivityBean.playList.get(mSongInMainActivityBean.position);
        stop_play = findViewById(R.id.stop_play);
        pre = findViewById(R.id.pre);
        next = findViewById(R.id.next);
        back = findViewById(R.id.back);
        songName = findViewById(R.id.textView1);
        author = findViewById(R.id.textView2);


        stop_play.setOnClickListener(this::onClick);
        pre.setOnClickListener(this::onClick);
        next.setOnClickListener(this::onClick);
        back.setOnClickListener(this::onClick);
        if (playEvet != null) {
            songName.setText(playEvet.songName);
            author.setText(playEvet.author);
        } else Log.i(TAG, "initView: playEvet 等于空");
        updateUi();
    }

    private void updateUi() {
        observer = upDateUI -> {
            songName.setText(upDateUI.songName);
            author.setText(upDateUI.author);
            stop_play.setImageResource(R.drawable.ic_pause_60dp);
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
                if (App.songInMainActivityBean.position > 0) {
                    App.songInMainActivityBean.position -= 1;
                    binder.hash2Url(App.songInMainActivityBean.playList.get(App.songInMainActivityBean.position));

                }
                Log.i(TAG, "onClick: pre");
                break;
            case R.id.stop_play:
                if (binder.getPlayState()) {
                    stop_play.setImageResource(R.drawable.ic_play_60dp);
                    binder.getMediaPlayer().pause();
                } else {
                    stop_play.setImageResource(R.drawable.ic_pause_60dp);
                    binder.getMediaPlayer().start();
                }
                break;
            case R.id.next:
                Log.i(TAG, "onClick: next");
                if (  App.songInMainActivityBean.position < App.songInMainActivityBean.playList.size()-1) {
                    App.songInMainActivityBean.position += 1;
                    binder.hash2Url(App.songInMainActivityBean.playList.get(App.songInMainActivityBean.position));
                }

                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LiveEventBus.get("UpDateUI", UpDateUI.class).removeObserver(observer);

    }
}
