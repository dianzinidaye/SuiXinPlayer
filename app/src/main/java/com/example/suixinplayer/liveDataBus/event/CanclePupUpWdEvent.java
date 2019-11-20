package com.example.suixinplayer.liveDataBus.event;

import com.example.suixinplayer.bean.SongSearchForResultListBean;

public class CanclePupUpWdEvent {

    public int whichPUW;
    public SongSearchForResultListBean.DataBean.InfoBean infoBean;

    public  CanclePupUpWdEvent(int whichPUW,SongSearchForResultListBean.DataBean.InfoBean infoBean){
        this.whichPUW = whichPUW;
        this.infoBean = infoBean;
    }

}
