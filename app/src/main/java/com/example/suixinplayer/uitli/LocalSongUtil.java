package com.example.suixinplayer.uitli;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.provider.MediaStore;
import android.util.Log;

import com.example.suixinplayer.db.SongDB;
import com.example.suixinplayer.liveDataBus.event.PlayEvet;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalSongUtil {
    //定义一个集合，存放从本地读取到的内容
    public static List<PlayEvet> list;

    public static PlayEvet playEvet;


    public static List<PlayEvet> getmusic(Context context) {
       // MediaScannerConnection.scanFile(context, new String[] {context.getExternalFilesDir(null).toString()},null, null);
        list = new ArrayList<>();


        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                , null, null, null, MediaStore.Audio.AudioColumns.IS_MUSIC);

        if (cursor != null) {
            while (cursor.moveToNext()) {

                playEvet = new PlayEvet();
                playEvet.songName = (cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
                playEvet.author = (cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                playEvet.isFree = (0);
                playEvet.hash = null;
                playEvet.isLocal = true;
                playEvet.isPrepare = false;
                playEvet.url = ( cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                long songSize = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));

//                把歌曲名字和歌手切割开
                if (songSize > 1000 * 800) {
                    if (playEvet.songName.contains("[mqms2].mp3")){

                        playEvet.songName =  playEvet.songName.replace("[mqms2].mp3", "HQ");
                    }
                    if (playEvet.songName.contains("[mqms].mp3")){
                        Log.i("TAG", "getmusic: [mqms].mp3");
                        playEvet.songName = playEvet.songName.replace("[mqms].mp3", "");
                    }
                    if (playEvet.songName.contains(".mp3")){
                        Log.i("TAG", "getmusic: 包含.mp3");
                        playEvet.songName = playEvet.songName.replace(".mp3", "");
                    }

                    if (playEvet.songName.contains("-")) {
                        String[] str = playEvet.songName.split("-");
                        playEvet.author = (str[0]);

                        playEvet.songName = (str[1]);
                    }
                    list.add(playEvet);
                }

            }
        }

        cursor.close();
        return list;

    }


    //    转换歌曲时间的格式
    public static String formatTime(int time) {
        if (time / 1000 % 60 < 10) {
            String tt = time / 1000 / 60 + ":0" + time / 1000 % 60;
            return tt;
        } else {
            String tt = time / 1000 / 60 + ":" + time / 1000 % 60;
            return tt;
        }
    }


}
