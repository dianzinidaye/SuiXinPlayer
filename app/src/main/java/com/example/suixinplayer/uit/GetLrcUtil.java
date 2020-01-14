package com.example.suixinplayer.uit;

import android.util.ArrayMap;
import android.widget.ImageView;

import com.example.suixinplayer.bean.LrcsBean;
import com.example.suixinplayer.network.LrcApiService;
import com.squareup.picasso.Picasso;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.wcy.lrcview.LrcView;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetLrcUtil {
    //用hash取获取包含歌词和播放Url的SongBean对象(还有付费信息和音质信息)  发送播放事件给service
    public static void getLrcUtil(String songName, String author, LrcView lrcView, ImageView imageView) {

        //pd.musicapp.migu.cn/MIGUM2.0/v1.0/content/search_all.do?&ua=Android_migu&version=5.0.1&text={关键词}&pageNo=1&pageSize=10&searchSwitch={"song":1,"album":0,"singer":0,"tagSong":0,"mvSong":0,"songlist":0,"bestShow":1}
        //http://www.kugou.com/yy/index.php?r=play/getdata&hash=79b77708dca79378af538f9a518f1b76/
        //http://mobilecdn.kugou.com/api/v3/search/song?format=json&keyword=喜欢你page=1&pagesize=20&showtype=1
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://pd.musicapp.migu.cn/")//基础URL 建议以 / 结尾
                .addConverterFactory(GsonConverterFactory.create())//设置 Json 转换器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//RxJava 适配器
                // .client(httpClient)
                .build();
        ArrayMap<String, String> headMap = new ArrayMap<>();
        headMap.put("ua", "Android_migu");
        headMap.put("version","5.0.1");
        if (author!=null){
            headMap.put("text",songName+" - "+author);
        }else {
            headMap.put("text",songName);
        }
        headMap.put("pageNo","1");
        headMap.put("pageSize","1");
        headMap.put("searchSwitch","{\"song\":1,\"album\":0,\"singer\":0,\"tagSong\":0,\"mvSong\":0,\"songlist\":0,\"bestShow\":1}");
        LrcApiService rxjavaService =  retrofit.create(LrcApiService.class);
        rxjavaService.getListBean(headMap)
                .subscribeOn(Schedulers.io())//IO线程加载数据
                .observeOn(AndroidSchedulers.mainThread())//主线程显示数据
                .subscribe(new Observer<LrcsBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        // Log.i("SearchFragment", "onSubscribe");
                    }

                    @Override
                    public void onNext(LrcsBean lrcs) {
                        LrcsBean lrcsBean = lrcs;
                        LrcsBean.SongResultDataBean.ResultBean resultBean = lrcsBean.getSongResultData().getResult().get(0);
                        if (resultBean!=null){
                           String lyricUrl = resultBean.getLyricUrl();
                            lrcView.loadLrcByUrl(lyricUrl);
                            Picasso.get().load(resultBean.getImgItems().get(0).getImg()).into(imageView);
                        }


                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


}
