package com.example.suixinplayer.app;

import android.os.Environment;

public class Api {
    /**
     * 存取到手机内存的文件名
     */
    public static String STORAGE_IMG_FILE= App.getContext().getExternalFilesDir("") + "/suixinting/img/";
    public static String STORAGE_LRC_FILE= App.getContext().getExternalFilesDir("") + "/suixinting/lrc/";
    public static String STORAGE_SONG_FILE= Environment.getExternalStorageDirectory() + "/suixinting/download/";
}
