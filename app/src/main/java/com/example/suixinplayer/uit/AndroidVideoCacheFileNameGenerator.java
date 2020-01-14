package com.example.suixinplayer.uit;

import android.net.Uri;
import android.util.Log;

import com.example.suixinplayer.other.videocache.file.FileNameGenerator;


/*
* 缓存文件命名规则
*
* */
public class AndroidVideoCacheFileNameGenerator implements FileNameGenerator {

   private String TAG="TAG";
    public String generate(String url) {
        Uri uri = Uri.parse(url);

        Log.i(TAG, "generate: "+uri.getQueryParameterNames().size()+"  "+"getQuery()"+uri.getQuery()+"   "+uri.toString());

        String videoId = uri.getQueryParameter("videoId");
        String song_name = uri.getQueryParameter("name");
        String mime = uri.getQueryParameter("mime");
        Log.i(TAG, "generate: "+song_name+"  "+videoId +"  "+url);
       // SharPUtil.putString(getApplication(), "LASTSONG",s.songName+"-"+s.author);

        //return uri.toString();
        return "123456";
    }
}


