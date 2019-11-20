package com.example.suixinplayer.network;

import com.example.suixinplayer.bean.SongSearchForResultListBean;


import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface ApiService {
    @GET("api/v3/search/song")
    Observable<SongSearchForResultListBean> getListBean(@QueryMap Map<String, String> map);
    //http://mobilecdn.kugou.com/api/v3/search/song?format=json&keyword=喜欢你page=1&pagesize=20&showtype=1

}
