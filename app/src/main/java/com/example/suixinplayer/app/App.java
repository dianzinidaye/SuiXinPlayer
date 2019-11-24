package com.example.suixinplayer.app;


import android.app.Application;
import android.content.Context;

import com.example.suixinplayer.base.ActivityManager;
import com.example.suixinplayer.bean.SongInMainActivityBean;
import com.example.suixinplayer.db.DBUtil;
import com.example.suixinplayer.liveDataBus.event.PlayEvet;
import com.example.suixinplayer.other.videocache.HttpProxyCacheServer;
import com.example.suixinplayer.uitli.AndroidVideoCacheFileNameGenerator;
import com.jeremyliao.liveeventbus.LiveEventBus;

import org.litepal.LitePalApplication;

import java.util.List;


public class App extends Application {
    public static ActivityManager mActivityManager = ActivityManager.getInstence();
    public static Context context;
    public static SongInMainActivityBean songInMainActivityBean = new SongInMainActivityBean();


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
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LiveEventBus.config().autoClear(true);
        songInMainActivityBean.playList = DBUtil.queryALL(DBUtil.getDatabase( this), "最近播放");

    }

    public static Context getContext() {
        return context;
    }
}
