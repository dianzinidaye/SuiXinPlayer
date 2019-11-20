package com.example.suixinplayer.network;

import com.example.suixinplayer.bean.SongBean;
import com.example.suixinplayer.bean.SongSearchForResultListBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.QueryMap;

public interface ApiSongDetailService {
    @Headers({"Cookie:kg_mid=2333"} )  //一定要带上Cookie:kg_mid=2333,不然会请求失败


    @GET("yy/index.php")
    Observable<SongBean> getListBean(@QueryMap Map<String, String> map);
    //http://www.kugou.com/yy/index.php?r=play/getdata&hash=79b77708dca79378af538f9a518f1b76/
}
