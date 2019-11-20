package com.example.suixinplayer.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.Nullable;

import com.example.suixinplayer.R;
import com.example.suixinplayer.base.BaseActivity;
import com.example.suixinplayer.liveDataBus.event.PlayEvet;
import com.example.suixinplayer.service.MusicPlayService;
import com.jeremyliao.liveeventbus.LiveEventBus;

public class PlayActivity extends BaseActivity implements View.OnClickListener {
    private ImageView stop_play;
    private ImageView pre;
    private ImageView next;
    String TAG = "TAG";
   // boolean isPlaying = false;
    boolean isLoveSong;
    private ImageView back;
    private PlayEvet playEvet;
    private MusicPlayService.MyBinder binder;
    private TextView songName,author;

    @Override
    protected void onRestart() {
        super.onRestart();
        if (binder.getPlayState()) {
            Log.i(TAG, "onRestart: 在播放");
            stop_play.setImageResource(R.drawable.ic_pause_60dp);
        }else {
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
        playEvet = (PlayEvet) getIntent().getSerializableExtra("FActivity");
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
        }
    }

    @Override
    protected void initData() {
        Intent intent = new Intent(this, MusicPlayService.class);
        ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                binder = (MusicPlayService.MyBinder) service;
                if (binder.getPlayState()) {
                    Log.i(TAG, "onRestart: 在播放");
                    stop_play.setImageResource(R.drawable.ic_pause_60dp);
                }else {
                    Log.i(TAG, "onRestart: 不播放");
                    stop_play.setImageResource(R.drawable.ic_play_60dp);
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);

        LiveEventBus.get("Play", PlayEvet.class).observeForever(new androidx.lifecycle.Observer<PlayEvet>() {
            @Override
            public void onChanged(PlayEvet playEvet) {
                songName.setText(playEvet.songName);
                author.setText(playEvet.author);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.pre:
                Log.i(TAG, "onClick: pre");
                break;
            case R.id.stop_play:
                if (binder.getPlayState()) {
                    stop_play.setImageResource(R.drawable.ic_play_60dp);

                    LiveEventBus.get("Play", PlayEvet.class).post(playEvet);
                   // isPlaying = false;
                    playEvet.paly = false;
                } else {
                    stop_play.setImageResource(R.drawable.ic_pause_60dp);
                    LiveEventBus.get("Play", PlayEvet.class).post(playEvet);
                    playEvet.paly = true;
                }
                Log.i(TAG, "onClick: stop_play");
                break;
            case R.id.next:
                Log.i(TAG, "onClick: next");
                break;
        }

    }
}
