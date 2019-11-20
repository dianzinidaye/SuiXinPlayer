package com.example.suixinplayer.db;

import org.litepal.crud.LitePalSupport;

public class SongDB extends LitePalSupport {
    //private String url;
    private String songName;
    private String author;
    private int is_free_part;      //1收费,0免费
    private String hash;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
//private String img;




    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getIs_free_part() {
        return is_free_part;
    }

    public void setIs_free_part(int payType) {
        this.is_free_part = payType;
    }


}
