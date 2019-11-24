package com.example.suixinplayer.bean;

import com.example.suixinplayer.R;

public class ChildItemBean {
    int iv_album = R.mipmap.love;  //歌单的图片
    String tv_song_sheet; //歌单名字
    int tv_sheet_song_total;//该歌单的歌曲数目

    public int getIv_album() {
        return iv_album;
    }

    public void setIv_album(int iv_album) {
        this.iv_album = iv_album;
    }

    public String getTv_song_sheet() {
        return tv_song_sheet;
    }

    public void setTv_song_sheet(String tv_song_sheet) {
        this.tv_song_sheet = tv_song_sheet;
    }

    public int getTv_sheet_song_total() {
        return tv_sheet_song_total;
    }

    public void setTv_sheet_song_total(int tv_sheet_song_total) {
        this.tv_sheet_song_total = tv_sheet_song_total;
    }
}
