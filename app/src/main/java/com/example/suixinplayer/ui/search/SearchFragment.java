package com.example.suixinplayer.ui.search;


import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.suixinplayer.R;
import com.example.suixinplayer.adapter.SearchRecyclerVierAdapter;
import com.example.suixinplayer.bean.SongSearchForResultListBean;
import com.example.suixinplayer.callback.HistoryRecyclerViewSelectOnclickListener;
import com.example.suixinplayer.db.DBUtil;
import com.example.suixinplayer.db.SongDB;
import com.example.suixinplayer.liveDataBus.MainActivityViewModel;
import com.example.suixinplayer.liveDataBus.event.PlayEvet;
import com.example.suixinplayer.network.ApiService;
import com.jeremyliao.liveeventbus.LiveEventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements HistoryRecyclerViewSelectOnclickListener, View.OnClickListener {

    private List<SongSearchForResultListBean.DataBean.InfoBean> list  = new ArrayList<>();
    private EditText etv_search;
    private RecyclerView mRecyclerView;
    private SearchRecyclerVierAdapter mSearchRecyclerVierAdapter;
    private MainActivityViewModel model;
    private SQLiteDatabase db;
    private SongDB mSongDB = new SongDB();


    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);
        db = DBUtil.creatDatabase("歌单", getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getView().findViewById(R.id.imageButton_back).setOnClickListener(this);
        getView().findViewById(R.id.imageButton_search).setOnClickListener(this);
        etv_search = getView().findViewById(R.id.etv_search);
        mRecyclerView = getView().findViewById(R.id.search_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mSearchRecyclerVierAdapter = new SearchRecyclerVierAdapter(list, this,getContext());
        mRecyclerView.setAdapter(mSearchRecyclerVierAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
    }

    @Override
    public void deal(int position, View view) {
        //处理搜索recyclerView item的点击事件
        commonPart(position);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButton_back:

                break;
            case R.id.imageButton_search:
                if (etv_search.getText().toString() != "") {
                    getSongSearchForResultListBean(etv_search.getText().toString());
                }

                break;

        }
    }

    //目的:获取关键字搜索的结果列表(主要是得到 hash)
    private void getSongSearchForResultListBean(String keyword) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://mobilecdn.kugou.com/")//基础URL 建议以 / 结尾
                .addConverterFactory(GsonConverterFactory.create())//设置 Json 转换器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//RxJava 适配器
                .build();
        ArrayMap<String, String> headMap = new ArrayMap<>();
        headMap.put("format", "json");
        headMap.put("keyword", keyword);
        headMap.put("page", "1");
        headMap.put("pagesize", "20");
        headMap.put("showtype", "1");
        ApiService rxjavaService = retrofit.create(ApiService.class);
        rxjavaService.getListBean(headMap)
                .subscribeOn(Schedulers.io())//IO线程加载数据
                .observeOn(AndroidSchedulers.mainThread())//主线程显示数据
                .subscribe(new Observer<SongSearchForResultListBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i("SearchFragment", "onSubscribe");
                    }

                    @Override
                    public void onNext(SongSearchForResultListBean songSearchForResultListBean) {
                        mSearchRecyclerVierAdapter.list = songSearchForResultListBean.getData().getInfo();
                        list = songSearchForResultListBean.getData().getInfo();
                        mSearchRecyclerVierAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("SearchFragment", "onError");
                    }

                    @Override
                    public void onComplete() {
                        Log.i("SearchFragment", "onComplete");
                    }
                });
    }


    void commonPart(int position) {
        if (list.get(position).getHash() != null) {
                //代码的公共部分
            model.getPlayEver().hash = list.get(position).getHash();
            model.getPlayEver().songName = list.get(position).getSongname();
            model.getPlayEver().author = list.get(position).getSingername();
            model.getPlayEver().paly = true;
            model.setPlayEver(model.getPlayEver());
            Log.i("TAG", "commonPart: hash="+list.get(position).getHash());
            LiveEventBus.get("Play", PlayEvet.class)
                    .post(model.getPlayEver());

            mSongDB.setHash(list.get(position).getHash());
            mSongDB.setAuthor(list.get(position).getSingername());
            if (list.get(position).getTrans_param().getHash_offset()==null){
            mSongDB.setIs_free_part(0);
            }else {
                mSongDB.setIs_free_part(1);
            }
            mSongDB.setSongName(list.get(position).getSongname());
            DBUtil.insert(db,"最近播放", mSongDB);

        }
    }
}
