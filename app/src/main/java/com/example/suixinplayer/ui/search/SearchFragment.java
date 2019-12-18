package com.example.suixinplayer.ui.search;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.ArrayMap;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.suixinplayer.R;
import com.example.suixinplayer.adapter.SearchRecyclerVierAdapter;
import com.example.suixinplayer.app.App;
import com.example.suixinplayer.bean.SongSearchForResultListBean;
import com.example.suixinplayer.callback.HistoryRecyclerViewSelectOnclickListener;
import com.example.suixinplayer.db.DBUtil;
import com.example.suixinplayer.liveDataBus.event.PlayEvet;
import com.example.suixinplayer.network.ApiService;
import com.example.suixinplayer.uitli.CommandUtil;
import com.example.suixinplayer.uitli.HistorySearchUtil;
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


    private RecyclerView mRecyclerView;
    public SearchRecyclerVierAdapter mSearchRecyclerVierAdapter;
    private List<PlayEvet> playEvetListlist = new ArrayList<>();
    private EditText etv_search;
    private String searchString = "";

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        if (getArguments()!=null){
            searchString = getArguments().getString("KeyWorld");
            Log.i("TAG", "SearchFragment onCreateView: "+searchString);
        }else {
            Log.i("TAG", "SearchFragment onCreateView: 为空");
        }
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getView().findViewById(R.id.imageButton_back).setOnClickListener(this);
        getView().findViewById(R.id.imageButton_search).setOnClickListener(this);
        etv_search = getView().findViewById(R.id.etv_search);
        etv_search.setText(searchString);
        etv_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (etv_search.getText() != null && etv_search.getText().toString() != "") {
                        Log.i("TAG", "点击了键盘搜索按键 " + etv_search.getText().toString());
                        // HistorySearchUtil.addSearchHistory(getContext(), etv_search.getText().toString());
                        HistorySearchUtil.addSearchHistory(getContext(), etv_search.getText().toString());
                        getSongSearchForResultListBean(etv_search.getText().toString());
                        CommandUtil.singleHideSoftKeyboard(getContext(), etv_search);
                   /* if (isSearchHistoryFragment){
                        Navigation.findNavController(SearchActivity.this,R.id.fragment).navigate(R.id.action_searchHistoryFragment_to_searchFragment);
                        isSearchHistoryFragment = false;
                        SearchFragment fragment = (SearchFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
                        fragment.mSearchRecyclerVierAdapter.list = list;
                    }*/
                        //  Navigation.findNavController(v).navigate(R.id.action_searchHistoryFragment_to_searchFragment);


                        // Navigation.createNavigateOnClickListener(R.id.action_searchHistoryFragment_to_searchFragment);
                    }
                    return true;
                }
                return false;
            }
        });
        mRecyclerView = getView().findViewById(R.id.search_recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mSearchRecyclerVierAdapter = new SearchRecyclerVierAdapter(this, getActivity());
        mRecyclerView.setAdapter(mSearchRecyclerVierAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        if (!searchString.equals("")){
            getSongSearchForResultListBean(etv_search.getText().toString());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButton_back:
                getActivity().onBackPressed();
                break;
            case R.id.imageButton_search:
                etv_search.setText("");

                break;
        }
    }

    @Override
    public void deal(int position, View v) {
        commonPart(position);
    }

    void commonPart(int position) {
        if (playEvetListlist != null) {
            //代码的公共部分
            LiveEventBus.get("Play", PlayEvet.class)
                    .post(playEvetListlist.get(position));

       /*     mSongDB.setHash(playEvetListlist.get(position).hash);
            mSongDB.setAuthor(playEvetListlist.get(position).author);
            mSongDB.setIs_free_part(playEvetListlist.get(position).isFree);
            mSongDB.setSongName(playEvetListlist.get(position).songName);*/
            App.playingListBean.playList.add(playEvetListlist.get(position));
            DBUtil.historyAddDate(getContext(), playEvetListlist.get(position));


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
                        Log.i("SearchFragment", "onNext: ");
                        mSearchRecyclerVierAdapter.list = songSearchForResultListBean.getData().getInfo();
                        mSearchRecyclerVierAdapter.notifyDataSetChanged();
                        playEvetListlist.clear();
                        for (SongSearchForResultListBean.DataBean.InfoBean infobean : songSearchForResultListBean.getData().getInfo()) {
                            PlayEvet playEvent = new PlayEvet();
                            playEvent.hash = infobean.getHash();
                            playEvent.songName = infobean.getSongname();
                            playEvent.author = infobean.getSingername();
                            if (infobean.getTrans_param().getHash_offset() == null) {
                                playEvent.isFree = 0;
                            } else {
                                playEvent.isFree = 1;
                            }
                            playEvetListlist.add(playEvent);

                        }
                        // model.changePlayList(playEvetListlist);
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
}
