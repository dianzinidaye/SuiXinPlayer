package com.example.suixinplayer.liveDataBus.event;

import java.io.Serializable;

public class PlayEvet implements Serializable {

    //0代表免费
    public   int isFree = 0;
    public String hash ;
    public String songName;
    public String author;
    public boolean isPrepare = false;
    public boolean isLocal = false;
    public String url;

}
