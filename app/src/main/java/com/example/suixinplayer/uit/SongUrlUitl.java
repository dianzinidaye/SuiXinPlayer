package com.example.suixinplayer.uit;

import android.util.Log;

import com.example.suixinplayer.bean.SongBean;
import com.example.suixinplayer.bean.SongSearchForResultListBean;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
/*
* 酷狗API不会用到这个
*
* */
public class SongUrlUitl {
    private static SongSearchForResultListBean mSongSearchForResultListBean = null;
    private static List<String> listUrl = new ArrayList<>();
    private static SongBean mSongBean = null;

    //获取搜索返回的结果对象
    public static SongSearchForResultListBean getSongSearchForResultListBean(String songName) {


        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(UrlUtil.getSearchUrl(songName))
                .get()//默认就是GET请求，可以不写
                .build();
        Call call = okHttpClient.newCall(request);

        //同步

        try {
            //同步调用,返回Response,会抛出IO异常
            Response response = call.execute();
            //这里一定要定义一个变量保存返回的数据,因在调用response.body().string()之后数据流就会关闭了
            //所以第二次调用的时候就会出错.
            String result = response.body().string();
            Log.d("SongUrlUitl", "onResponse: " + result);
            Gson gson = new Gson();
            mSongSearchForResultListBean = gson.fromJson(result, SongSearchForResultListBean.class);
            Log.d("SongUrlUitl", "mSongSearchForResultListBean: " + mSongSearchForResultListBean.getData().getInfo().get(0).getFilename());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mSongSearchForResultListBean;

    }

    //返回获取带歌词的歌词详情地址list
    public static List<String> getSongDetailUrl(SongSearchForResultListBean songSearchForResultListBean) {
        List<SongSearchForResultListBean.DataBean.InfoBean> infoBeanList = songSearchForResultListBean.getData().getInfo();
        for (SongSearchForResultListBean.DataBean.InfoBean info : infoBeanList
        ) {
            listUrl.add(UrlUtil.getPlayUrl(info.getHash()));
        }
        return listUrl;
    }

    public static SongBean getSong(String songDetailUrl) {
        Log.d("SongUrlUitl", "SongUrlUitl  songDetailUrl: " + songDetailUrl);
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(UrlUtil.getPlayUrl(songDetailUrl))
                .get()//默认就是GET请求，可以不写
                .build();
        Call call = okHttpClient.newCall(request);

        //同步

        try {
            //同步调用,返回Response,会抛出IO异常
            Response response = call.execute();
            //这里一定要定义一个变量保存返回的数据,因在调用response.body().string()之后数据流就会关闭了
            //所以第二次调用的时候就会出错.
            String result = response.body().string();
            Log.d("SongUrlUitl", "onResponse: " + result);
            Gson gson = new Gson();
            mSongBean = gson.fromJson(result, SongBean.class);
            // Log.d("SongUrlUitl", "songPlayUrl: " +mSongBean.getData().getPlay_url());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mSongBean;
    }


}
