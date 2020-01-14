package com.example.suixinplayer.uit;


/*
 * 酷狗API不会用到这个
 *
 * */
public class UrlUtil {

    static public String getSearchUrl(String songName){
        StringBuilder stringBuilder = new StringBuilder("http://mobilecdn.kugou.com/api/v3/search/song?format=json&keyword=");
        return stringBuilder.append(songName).append("&page=1&pagesize=20&showtype=1/").toString();

    }

    static public  String getPlayUrl(String hash){
        StringBuilder stringBuilder = new StringBuilder("http://www.kugou.com/yy/index.php?r=play/getdata&hash=");
        return  stringBuilder.append(hash).toString();
    }
}
