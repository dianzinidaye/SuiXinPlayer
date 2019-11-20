package com.example.suixinplayer.ui.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.example.suixinplayer.R;
import com.example.suixinplayer.adapter.MyFragmentPagerAdapter;
import com.example.suixinplayer.base.BaseActivity;
import com.example.suixinplayer.bean.SongInMainActivityBean;
import com.example.suixinplayer.callback.ViewPageSelectCallback;
import com.example.suixinplayer.db.DBUtil;
import com.example.suixinplayer.listener.ViewPagerOnPageChangeListener;
import com.example.suixinplayer.liveDataBus.MainActivityViewModel;
import com.example.suixinplayer.liveDataBus.event.PlayEvet;
import com.example.suixinplayer.service.MusicPlayService;
import com.example.suixinplayer.ui.search.SearchFragment;
import com.example.suixinplayer.uitli.SharPUtil;
import com.example.suixinplayer.widget.MyCircleImageView;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.roughike.bottombar.BottomBar;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends BaseActivity implements ViewPageSelectCallback, View.OnClickListener {

    private MyCircleImageView mCircleImageView;
    private ViewPager viewPager;
    private MyFragmentPagerAdapter fragmentPagerAdapter;
    private BottomBar bottomBar;
    private ImageButton play_or_Stop;
    private MusicPlayService.MyBinder binder;

    //前一个fragment
    private String previousFragment = "previousFragment";
    private Button btn_search;
    private ImageButton play_list;
    private MainActivityViewModel model;
    private SeekBar seekBar;
    private ConstraintLayout mConstraintLayout;
    private TextView songName,author;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        //第一次进入程序会默认创建一个"我喜欢"的歌单
        if (!SharPUtil.contains(this, "IsFirst")) {
            SQLiteDatabase db = DBUtil.creatDatabase("歌单", this);
            DBUtil.createTable(db, "最近播放", this);
            DBUtil.createTable(db, "我喜欢", this);
            //用来判断是否是第一次进入程序
            SharPUtil.putBoolean(this, "IsFirst", false);
        }

        rxPermissionTest();
        mCircleImageView = findViewById(R.id.profile_image);
        viewPager = findViewById(R.id.viewPager);
        bottomBar = findViewById(R.id.bottomBar);
        play_or_Stop = findViewById(R.id.imageButton_playOrStop);
        btn_search = findViewById(R.id.btn_search);
        play_list = findViewById(R.id.imageButtonplay_list);
        seekBar = findViewById(R.id.seekBar5);
        mConstraintLayout = findViewById(R.id.bottom);
        songName = findViewById(R.id.textView5);
        author = findViewById(R.id.textView6);
        bottomBar.setDefaultTabPosition(0);

    }

    @Override
    protected void initData() {
        model = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        SongInMainActivityBean mSongInMainActivityBean = new SongInMainActivityBean();
        model.getData().setValue(mSongInMainActivityBean);
        model.getData().observe(this, users -> {
            songName.setText(model.getPlayEver().songName);
            author.setText(model.getPlayEver().author);
            if (model.getPlayEver().paly == true) {
                play_or_Stop.setImageResource(R.drawable.ic_mainactivity_stop);
                Log.i("TAG", "initData: model.getPlayEver().paly==true");
            } else {
                play_or_Stop.setImageResource(R.drawable.ic_mainactivity_play);
                Log.i("TAG", "initData: model.getPlayEver().paly==false");
            }

        });
        Intent intent = new Intent(this, MusicPlayService.class);
        ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                binder = (MusicPlayService.MyBinder) service;
             MediaPlayer mediaPlayer = binder.getMediaPlayer();
             mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                 @Override
                 public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    // seekBar.set
                 }
             });

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        dealView();
/*        LiveEventBus.get("Play", PlayEvet.class).observeForever(new androidx.lifecycle.Observer<PlayEvet>() {
            @Override
            public void onChanged(PlayEvet playEvet) {
                songName.setText(playEvet.songName);
                author.setText(playEvet.author);
            }
        });*/
    }

    /*
     * viewpager翻页回调
     * */
    @Override
    public void dealOnPageSelected(int position) {
        bottomBar.selectTabAtPosition(position);
    }

    /*
     * 设置viewPager和bottomBar
     * */
    private void dealView() {
        fragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        if (null != fragmentPagerAdapter) {
            viewPager.setAdapter(fragmentPagerAdapter);
        }
        viewPager.addOnPageChangeListener(new ViewPagerOnPageChangeListener(fragmentPagerAdapter.fragmentList, viewPager, this));
        bottomBar.setOnTabSelectListener(tabId -> {
            switch (tabId) {
                case R.id.tab_first:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.tab_second:
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.tab_third:
                    viewPager.setCurrentItem(2);
                    break;
            }
        });
        play_or_Stop.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        play_list.setOnClickListener(this);
        mConstraintLayout.setOnClickListener(this);
        LiveEventBus
                .get("progress", Integer.class).observeForever(integer -> {
            seekBar.setSecondaryProgress(integer);
        });
    }

    /*
     * 后退键的处理
     * */
    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(previousFragment);
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commitNow();
        } else
            moveTaskToBack(true);//退到后台,不退出程序
    }

    //权限申请
    private void rxPermissionTest() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            RxPermissions rxPermissions = new RxPermissions(this);
            rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    .subscribe(new Observer<Boolean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Boolean aBoolean) {
                            if (aBoolean) {

                            } else {
                                //只有用户拒绝开启权限，且选了不再提示时，才会走这里，否则会一直请求开启
                                Toast.makeText(MainActivity.this, "主人，我被禁止啦，去设置权限设置那把我打开哟", Toast.LENGTH_LONG)
                                        .show();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
            model.getPlayEver().paly = binder.getPlayState();
        if (binder.getPlayState()) {
            play_or_Stop.setImageResource(R.drawable.ic_mainactivity_stop);
        }else {
            play_or_Stop.setImageResource(R.drawable.ic_mainactivity_play);
        }
       // model.setPlayEver(model.getPlayEver());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.mainlayout, new SearchFragment(), previousFragment).commitNow();
                break;
            case R.id.imageButton_playOrStop:
                model.getPlayEver().paly = binder.getPlayState();
                if (model.getPlayEver().paly == true) {
                    LiveEventBus.get("Play", PlayEvet.class).post(model.getPlayEver());
                    model.getPlayEver().paly = false;
                    model.setPlayEver(model.getPlayEver());


                } else {
                    if (model.getPlayEver().hash != null) {
                        LiveEventBus.get("Play", PlayEvet.class).post(model.getPlayEver());
                        model.getPlayEver().paly = true;
                        model.setPlayEver(model.getPlayEver());

                    }
                }

                break;
            case R.id.imageButtonplay_list:

                break;
            case R.id.bottom:
                Log.i("TAG", "onClick: 点击了底部bottom");
                Intent intent = new Intent(this, PlayActivity.class);
                if (model.getPlayEver() != null) {
                    intent.putExtra("FActivity", model.getPlayEver());
                    startActivity(intent);
                } else Log.i("TAG", "onClick: model.getPlayEver()为空");

                break;
        }

    }
}
