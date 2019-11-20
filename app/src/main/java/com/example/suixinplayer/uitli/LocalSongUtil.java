package com.example.suixinplayer.uitli;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class LocalSongUtil {
//368ea9de56e4da0fe83515dfa93f7300/G013/M02/14/10/rYYBAFT9NkmAcPm5AEYxuCed8ko021.mp3
  /*  //获取本地MP3文件路径缓存
    public static void getLocalSongInformation(File file){
        Map<String, String> allSong =(Map <String, String>) sharedPreferences.getAll();
        if (!allSong.isEmpty()){
            for (Map.Entry<String,String> entry: allSong.entrySet()){

                String [] songandpath = entry.getValue().split("#@");
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("name", (   (songandpath[0].split("-"))[1].split("\\."))[0]);
                hashMap.put("path",songandpath[1]);
                // Log.i("hugea", "getLocalSongInformation: "+ a+"  "+b.length);
                listFileSong.add(hashMap);
            }
        }else {
            listFileSong(file);
            getLocalSongInformation(file);
        }
    }
    //遍历所有MP3文件
    public static void listFileSong(File file){
        File [] files = file.listFiles();
        try{
            for (File f : files) {
                if (!f.isDirectory()){
                    if (f.getName().endsWith(".mp3")){
                        if ((f.length() / (1024 * 1024))>3){
                            //   HashMap<String,String> hashMap = new HashMap<>();
                            //  hashMap.put("name", f.getName());
                            //  hashMap.put("path",f.getAbsolutePath());
                            editor = sharedPreferences.edit();
                            editor.putString(f.getName(), f.getName()+"#@"+f.getAbsolutePath());
                            editor.commit();
                            //  listFileSong.add(hashMap);

                        }


                    }
                }else if (f.isDirectory()){
                    //如果是目录，迭代进入该目录
                    listFileSong(f);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }*/
}
