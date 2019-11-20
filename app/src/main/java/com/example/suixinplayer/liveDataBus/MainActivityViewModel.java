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

    public void add2PlayList(PlayEvet playEvet) {
        playData.getValue().playList.add(playEvet);
        playData.setValue(playData.getValue());
    }

    public void delFromPlayList(int position) {
        playData.getValue().playList.remove(position);
        playData.setValue(playData.getValue());
    }

    public void changePlayList(List<PlayEvet> list){
        playData.getValue().playList = list;
        playData.setValue(playData.getValue());
    }

    public void setPosition(int position){
        playData.getValue().position = position;
        playData.setValue(playData.getValue());
    }


}
