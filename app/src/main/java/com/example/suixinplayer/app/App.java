package com.example.suixinplayer.app;


import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.suixinplayer.base.ActivityManager;
import com.example.suixinplayer.bean.SongInMainActivityBean;
import com.example.suixinplayer.db.DBUtil;
import com.example.suixinplayer.liveDataBus.event.PlayEvet;
import com.example.suixinplayer.other.videocache.HttpProxyCacheServer;
import com.example.suixinplayer.uitli.AndroidVideoCacheFileNameGenerator;
import com.example.suixinplayer.uitli.SharPUtil;
import com.jeremyliao.liveeventbus.LiveEventBus;

import org.litepal.LitePalApplication;

import java.util.List;


public class App extends Application {
    public static ActivityManager mActivityManager = ActivityManager.getInstence();
    public static Context context;

    public static SongInMainActivityBean playHistoryBean = new SongInMainActivityBean();
    public static SongInMainActivityBean favListBean = new SongInMainActivityBean();
    public static SongInMainActivityBean playingListBean = new SongInMainActivityBean();
    public static String playingListName = "最近播放";

    public static Context getContext() {
        return context;
    }

    /*
     * 播放历史列表添加歌曲,若有重复则删除再添加到第一
     * */
    public static void addSong2History(PlayEvet playEvet) {
        int i = 0;
        for (PlayEvet event : playHistoryBean.getPlayList()) {
            if (event.hash.equals(playEvet.hash)) {
                break;
            }
            i++;
        }
        boolean a = playHistoryBean.getPlayList().contains(playEvet);
        if (a) {
            PlayEvet b = playHistoryBean.getPlayList().remove(i);
            Log.i("TAG", "addSong: 删除是否成功 " + (b == null) + "  包含与否:" + a);
        }
        playHistoryBean.getPlayList().add(playEvet);
    }

    /*
     * 更改播放列表
     * */
    public static void changeList(String newListName) {
        //当前播放的歌曲列表名称
        playingListName = newListName;
        playingListBean.setPlayList(DBUtil.queryALL(DBUtil.getDatabase(getContext()), playingListName));
        playingListBean.setPosition(SharPUtil.getInt(getContext(), playingListName, 0));
        //每次更换播放列表都记录到SharedPreferences
        SharPUtil.putString(getContext(), "当前播放列表", playingListName);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LiveEventBus.config().autoClear(true);
        try {
            /*
             * 初始化歌曲列表数据
             * */
            // playHistoryBean.playList = DBUtil.queryALL(DBUtil.getDatabase( this), "最近播放");
            // playHistoryBean.setPlayList(DBUtil.queryALL(DBUtil.getDatabase(this), "最近播放"));
            //           favListBean.setPlayList(DBUtil.queryALL(DBUtil.getDatabase(this), "我喜欢"));
            // playingListBean.setPlayList(DBUtil.queryALL(DBUtil.getDatabase(this), "默认列表"));
            /*
             * 获取各个列表的
             * */
            //  playHistoryBean.setPosition(SharPUtil.getInt(this, "最近播放", 0));
            //         favListBean.setPosition(SharPUtil.getInt(this, "我喜欢", 0));

            //当前播放的歌曲列表名称
            String pList = SharPUtil.getString(this, "当前播放列表");
            Log.i("TAG", "当前播放列表: " + pList);
            if (!pList.equals("")) {
                playingListName = pList;
            }
            playingListBean.setPlayList(DBUtil.queryALL(DBUtil.getDatabase(this), playingListName));
            playingListBean.setPosition(SharPUtil.getInt(this, playingListName, 0));


        } catch (Exception e) {
            e.printStackTrace();
        }

    }



  /*  private HttpProxyCacheServer proxy;

    public static HttpProxyCacheServer getProxy(Context context) {
        App app = (App) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer.Builder(context)
                .maxCacheSize(1024 * 1024 * 1024) // 1 Gb for cache
                .maxCacheFilesCount(20)// 缓存文件最多20个
                .fileNameGenerator(new AndroidVideoCacheFileNameGenerator())
                .build();
    }*/


//    public SourceInfoStorage sourceInfoStorage = SourceInfoStorageFactory.newSourceInfoStorage(context);

    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        SharPUtil.putString(this, "当前播放列表", playingListName);
        SharPUtil.putInt(this, "当前播放位置", playingListBean.getPosition());
        super.onTerminate();
    }

}
