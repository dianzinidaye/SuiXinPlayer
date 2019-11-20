package com.example.suixinplayer.bean;

import androidx.lifecycle.MutableLiveData;

import com.example.suixinplayer.liveDataBus.event.PlayEvet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//MainActivity所需的播放列表和单曲播放的url
public class SongInMainActivityBean implements Serializable {
    public  Map<String, List<String>> map;
    public PlayEvet playEvet;
    public SongInMainActivityBean(){
        map = new HashMap<>();
        playEvet = new PlayEvet();
    }
}
