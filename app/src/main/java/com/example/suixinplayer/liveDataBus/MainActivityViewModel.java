package com.example.suixinplayer.liveDataBus;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.suixinplayer.bean.SongInMainActivityBean;
import com.example.suixinplayer.liveDataBus.event.PlayEvet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivityViewModel extends ViewModel {
    private MutableLiveData<SongInMainActivityBean> playData;

    public MutableLiveData<SongInMainActivityBean> getData() {
        if (playData == null) {
            playData = new MutableLiveData<>();
        }
        return playData;
    }

    public void setSongSheetList(String songSheetName, List<String> songSheetList) {
        if (playData.getValue().map.containsKey(songSheetName)) {
            playData.getValue().map.remove(songSheetName);
            playData.getValue().map.put(songSheetName, songSheetList);
            playData.setValue(playData.getValue());
        }

    }

    public Map getSongSheetList() {
        return playData.getValue().map;
    }



    public PlayEvet getPlayEver(){
        return playData.getValue().playEvet;
    }
    public void setPlayEver(PlayEvet playEvet){
        playData.getValue().playEvet = playEvet;
        playData.setValue(playData.getValue());
    }


}
