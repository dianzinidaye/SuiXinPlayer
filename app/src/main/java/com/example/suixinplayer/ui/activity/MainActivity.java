package com.example.suixinplayer.ui.activity;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.suixinplayer.R;
import com.example.suixinplayer.adapter.MyFragmentPagerAdapter;
import com.example.suixinplayer.adapter.PopUpWindowRecyclerViewPresentListAdapter;
import com.example.suixinplayer.app.App;
import com.example.suixinplayer.base.BaseActivity;
import com.example.suixinplayer.callback.PopRecyclerViewSelectOnclickListener;
import com.example.suixinplayer.callback.ViewPageSelectCallback;
import com.example.suixinplayer.db.DBUtil;
import com.example.suixinplayer.listener.ViewPagerOnPageChangeListener;
import com.example.suixinplayer.liveDataBus.event.PlayEvet;
import com.example.suixinplayer.liveDataBus.event.UpDateUI;
import com.example.suixinplayer.service.MusicPlayService;
import com.example.suixinplayer.uitli.PermissionUtil;
import com.example.suixinplayer.uitli.SharPUtil;
import com.example.suixinplayer.widget.MyCircleImageView;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.roughike.bottombar.BottomBar;
import com.squareup.picasso.Picasso;


import static android.view.animation.Animation.INFINITE;


public class MainActivity extends BaseActivity implements ViewPageSelectCallback, View.OnClickListener, MediaPlayer.OnBufferingUpdateListener, PopRecyclerViewSelectOnclickListener {

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
    private SeekBar seekBar;
    private ConstraintLayout mConstraintLayout;
    private TextView songName, author;
    private Observer<UpDateUI> observer;
    private PopupWindow popupWindow;
    private RecyclerView recyclerView;
    private int position = 0;
    private PopUpWindowRecyclerViewPresentListAdapter adapter;
    private View popRootView;
    private final int UPDATE = 121;
    private ObjectAnimator animator;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        adapter = new PopUpWindowRecyclerViewPresentListAdapter(this, App.playingListBean, this);

        //第一次进入程序会默认创建一个"我喜欢"的歌单
        if (!SharPUtil.contains(this, "IsFirst")) {
            SQLiteDatabase db = DBUtil.getDatabase(this);
            DBUtil.createTable(db, "最近播放", this);
            DBUtil.createTable(db, "我喜欢", this);
            // DBUtil.createTable(db, "默认列表", this);
            //用来判断是否是第一次进入程序
            SharPUtil.putBoolean(this, "IsFirst", false);
        }

        // rxPermissionTest();
        PermissionUtil.rxPermissionTest(this);
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
        findViewById(R.id.profile_image).setOnClickListener(this);

    }

    @Override
    protected void initData() {
        /*
         * 绑定service
         * */
        Intent intent = new Intent(this, MusicPlayService.class);
        ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                binder = (MusicPlayService.MyBinder) service;
                MediaPlayer mediaPlayer = binder.getMediaPlayer();
                mediaPlayer.setOnBufferingUpdateListener(MainActivity.this);
                seekBar.setMax(binder.getDuration());
                Log.d("TAG", "onPrepared: 播放  主线程" + binder.getDuration());
                handler.sendEmptyMessage(UPDATE);

                /*
                 * 重启程序,恢复上次最后播放的歌曲
                 * */
                PlayEvet playEvet = DBUtil.querLast(DBUtil.getDatabase(MainActivity.this), "最近播放");
                playEvet.isPrepare = true;
                LiveEventBus.get("Play", PlayEvet.class)
                        .post(playEvet);
                //songName.setText(playEvet.songName);
                // author.setText(playEvet.author);
                //   Log.i("TAG", "onChanged:  不空" + upDateUI.duration);
                //   play_or_Stop.setImageResource(R.drawable.ic_mainactivity_stop);
                // mCircleImageView.startAnimation(rotateAnimation);
                //  animator.start();
                // adapter.notifyDataSetChanged();


            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        updateUi();
        dealView();
     /*   UpDateUI upDateUI = new UpDateUI();
        upDateUI.author = App.songInMainActivityBean.playList.get( App.songInMainActivityBean.position).author;
        upDateUI.hash = App.songInMainActivityBean.playList.get( App.songInMainActivityBean.position).hash;
        upDateUI.isFree = App.songInMainActivityBean.playList.get( App.songInMainActivityBean.position).isFree;
        upDateUI.songName = App.songInMainActivityBean.playList.get( App.songInMainActivityBean.position).songName;
        upDateUI.duration = 0;
        LiveEventBus.get("UpDateUI", UpDateUI.class).post(upDateUI);*/
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


        /*
         * 设置圆图的动画
         * */
        animator = ObjectAnimator.ofFloat(mCircleImageView, "rotation", 0f, 360.0f);
        animator.setRepeatCount(INFINITE);   //不断重复
        //animator.setRepeatMode(RESTART);  默认就是restart
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(20000);

/*
       //这里不能用RotateAnimation,因为不能暂停和继续播放
       rotateAnimation = new RotateAnimation(0,360,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        // 1. fromDegrees ：动画开始时 视图的旋转角度(正数 = 顺时针，负数 = 逆时针)
        // 2. toDegrees ：动画结束时 视图的旋转角度(正数 = 顺时针，负数 = 逆时针)
        // 3. pivotXType：旋转轴点的x坐标的模式
        // 4. pivotXValue：旋转轴点x坐标的相对值
        // 5. pivotYType：旋转轴点的y坐标的模式
        // 6. pivotYValue：旋转轴点y坐标的相对值
        // pivotXType = Animation.ABSOLUTE:旋转轴点的x坐标 =  View左上角的原点 在x方向 加上 pivotXValue数值的点(y方向同理)
        // pivotXType = Animation.RELATIVE_TO_SELF:旋转轴点的x坐标 = View左上角的原点 在x方向 加上 自身宽度乘上pivotXValue数值的值(y方向同理)
        // pivotXType = Animation.RELATIVE_TO_PARENT:旋转轴点的x坐标 = View左上角的原点 在x方向 加上 父控件宽度乘上pivotXValue数值的值 (y方向同理)
        rotateAnimation.setRepeatCount( INFINITE);
        rotateAnimation.setRepeatMode(RESTART);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(6000);
        //mCircleImageView.setAnimation(rotateAnimation);*/
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
                    play_or_Stop.setImageResource(R.drawable.ic_mainactivity_stop);
                }
            }
        });

    }

    /*
     * 后退键的处理
     * */
    @Override
    public void onBackPressed() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            return;
        }
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(previousFragment);
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commitNow();
        } else
            moveTaskToBack(true);//退到后台,不退出程序
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (binder != null) {
            //  Log.i("TAG", "onResume:  不空");
            if (binder.getPlayState()) {
                play_or_Stop.setImageResource(R.drawable.ic_mainactivity_stop);
            } else {
                play_or_Stop.setImageResource(R.drawable.ic_mainactivity_play);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LiveEventBus.get("UpDateUI", UpDateUI.class).removeObserver(observer);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:
                startActivity(new Intent(this, SearchActivity.class));
                break;

            case R.id.imageButton_playOrStop:
                if (binder.getPlayState()) {
                    binder.puase();
                    play_or_Stop.setImageResource(R.drawable.ic_mainactivity_play);
                } else {
                    binder.continuePlay();
                    play_or_Stop.setImageResource(R.drawable.ic_mainactivity_stop);
                }

                break;

            case R.id.imageButtonplay_list:
                if (popupWindow == null) {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    popRootView = inflater.inflate(R.layout.popupwindow_songlist, null, false);//引入弹窗布局
                    // PopUpWindow 传入 ContentView
                    int height = this.getResources().getDisplayMetrics().heightPixels;
                    popupWindow = new PopupWindow(popRootView, ViewGroup.LayoutParams.MATCH_PARENT, height / 5 * 3);
                    popupWindow.setTouchable(true);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(popRootView.getContext());

                    recyclerView = popRootView.findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adapter);

                    // 设置背景
                    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

                    // 外部点击事件
                    popupWindow.setOutsideTouchable(true);
                    popupWindow.setAnimationStyle(R.style.popwin_anim_style);
                    popupWindow.showAtLocation(popRootView, Gravity.BOTTOM, 0, 0);
                    recyclerView.scrollToPosition(position + 1);
                    // recyclerView.findViewHolderForAdapterPosition(position).itemView.setBackgroundColor(Color.RED);
                } else {
                    popupWindow.showAtLocation(popRootView, Gravity.BOTTOM, 0, 0);
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.bottom:
                Intent intent = new Intent(this, PlayActivity.class);
                startActivity(intent);
                break;
            case R.id.profile_image:
                Log.i("TAG", "onClick: profile_image");
                //DBUtil.delete(DBUtil.getDatabase(this), "最近播放", "635090888ee12e56c94ece803c41b8c8");
                break;
        }

    }

    private void updateUi() {
        observer = upDateUI -> {
            if (upDateUI.isPlaying.equals("仅播放")) {
                //  Log.i("TAG", "updateUi: 仅播放");
                play_or_Stop.setImageResource(R.drawable.ic_mainactivity_stop);
                if (animator.isPaused()) {
                    animator.resume();
                } else {
                    animator.start();
                }
            } else if (upDateUI.isPlaying.equals("仅暂停")) {
                // Log.i("TAG", "updateUi: 仅暂停");
                play_or_Stop.setImageResource(R.drawable.ic_mainactivity_play);
                animator.pause();
            } else {

                //  Log.i("TAG", "updateUi: 暂停或者播放");
                if (upDateUI.imaUri != null) {
                    Picasso.get().load(upDateUI.imaUri).into(mCircleImageView);
                    //  mCircleImageView.setImageBitmap();
                } else {
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.love);
                    mCircleImageView.setImageBitmap(bitmap);
                }
                songName.setText(upDateUI.songName);
                author.setText(upDateUI.author);
                Log.i("TAG", "onChanged:  不空" + upDateUI.duration);
                // mCircleImageView.startAnimation(rotateAnimation);
                seekBar.setMax(upDateUI.duration);
                if (!upDateUI.isPrepar) {
                    play_or_Stop.setImageResource(R.drawable.ic_mainactivity_stop);
                    animator.start();
                    adapter.notifyDataSetChanged();
                } else {
                    play_or_Stop.setImageResource(R.drawable.ic_mainactivity_play);
                }
            }
        };

        LiveEventBus.get("UpDateUI", UpDateUI.class).observeStickyForever(observer);
    }

    /*
     * mediapalyer缓存,更新seekbar
     * */
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        seekBar.setSecondaryProgress(percent * seekBar.getMax());
        Log.i("TAG", "onBufferingUpdate: MainActivity  ");
    }


    @Override
    public void deal(int position, View v) {
        App.playingListBean.position = position;
        this.position = position;
        adapter.notifyDataSetChanged();
    }

    /*
     * 更新进度条
     * */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == UPDATE) {
                seekBar.setProgress(binder.getMediaPlayer().getCurrentPosition());
                //   Log.i("TAG", "handleMessage: " + binder.getMediaPlayer().getCurrentPosition() + "  " + seekBar.getMax());

            }
            handler.sendEmptyMessageDelayed(UPDATE, 500);

        }
    };

}
