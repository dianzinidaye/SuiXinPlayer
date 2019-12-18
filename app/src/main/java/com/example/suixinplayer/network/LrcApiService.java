package com.example.suixinplayer.network;

import com.example.suixinplayer.bean.LrcsBean;
import com.example.suixinplayer.bean.SongSearchForResultListBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface LrcApiService {

    //pd.musicapp.migu.cn/MIGUM2.0/v1.0/content/search_all.do?&ua=Android_migu&version=5.0.1&text={关键词}&pageNo=1&pageSize=10&searchSwitch={"song":1,"album":0,"singer":0,"tagSong":0,"mvSong":0,"songlist":0,"bestShow":1}
    //http://www.kugou.com/yy/index.php?r=play/getdata&hash=79b77708dca79378af538f9a518f1b76/
    //http://mobilecdn.kugou.com/api/v3/search/song?format=json&keyword=喜欢你page=1&pagesize=20&showtype=1
    @GET("MIGUM2.0/v1.0/content/search_all.do")
    Observable<LrcsBean> getListBean(@QueryMap Map<String, String> map);
    //http://mobilecdn.kugou.com/api/v3/search/song?format=json&keyword=喜欢你page=1&pagesize=20&showtype=1

}
