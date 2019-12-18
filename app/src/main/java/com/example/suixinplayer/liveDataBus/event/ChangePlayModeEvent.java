package com.example.suixinplayer.liveDataBus.event;

public class ChangePlayModeEvent {

    //默认为顺序播放
    public MODE  mode = MODE.ORDER;

    public enum MODE {
        /*
         * 3种播放模式,单曲,顺序,随机
         * */
        SINGLE, ORDER, RANDOM
    }
}
