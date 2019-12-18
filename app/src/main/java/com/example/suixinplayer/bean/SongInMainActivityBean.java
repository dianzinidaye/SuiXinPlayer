package com.example.suixinplayer.bean;

import androidx.lifecycle.MutableLiveData;

import com.example.suixinplayer.liveDataBus.event.PlayEvet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//MainActivity所需的播放列表和单曲播放的url
public class SongInMainActivityBean implements Serializable {
    public  List<PlayEvet> playList;

    public List<PlayEvet> getPlayList() {
        return playList;
    }

    public void setPlayList(List<PlayEvet> playList) {
        this.playList = playList;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int position = 0;
    public SongInMainActivityBean(){
        playList = new ArrayList<>();
    }
}
