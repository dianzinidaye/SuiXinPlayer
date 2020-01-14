package com.example.suixinplayer.ui.activity;


import androidx.fragment.app.Fragment;

import com.example.suixinplayer.R;
import com.example.suixinplayer.base.BaseActivity;
import com.example.suixinplayer.uit.CommandUtil;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchActivity extends BaseActivity {

  /*  private List<PlayEvet> playEvetListlist = new ArrayList<>();
    private EditText etv_search;

    public   List<SongSearchForResultListBean.DataBean.InfoBean> list = new ArrayList<>();
    private boolean isSearchHistoryFragment = true;*/


    @Override
    protected int getLayoutResId() {
        //把状态栏文字设置为黑色
        CommandUtil.setAndroidNativeLightStatusBar(this, true);
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {
      /*  findViewById(R.id.imageButton_back).setOnClickListener(this);
       findViewById(R.id.imageButton_search).setOnClickListener(this);
        etv_search = findViewById(R.id.etv_search);
*/


        //mHistoryRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));


    }

    /* @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_search, container, false);
    }*/

  /*  @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getView().findViewById(R.id.imageButton_back).setOnClickListener(this);
        getView().findViewById(R.id.imageButton_search).setOnClickListener(this);
        etv_search = getView().findViewById(R.id.etv_search);
        mRecyclerView = getView().findViewById(R.id.search_recyclerView);
        mHistoryRecyclerView = getView().findViewById(R.id.history_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mSearchRecyclerVierAdapter = new SearchRecyclerVierAdapter(this, getContext());
        mRecyclerView.setAdapter(mSearchRecyclerVierAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        //mHistoryRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));
        mHistoryRecyclerView.setLayoutManager(new FlexboxLayoutManager(getContext()));
        mHistoryRecyclerView.setAdapter(new HistorySearchRcyViewAdapter(HistorySearchUtil.getSearchHistory(getContext())));
    }*/



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //isSearchHistoryFragment = true;
    }

/*    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButton_back:
                onBackPressed();
                break;
            case R.id.imageButton_search:
                if (etv_search.getText().toString() != "") {
                    Log.i("TAG", "Navigation onClick: "+etv_search.getText().toString());
                    HistorySearchUtil.addSearchHistory(this, etv_search.getText().toString());
                    getSongSearchForResultListBean(etv_search.getText().toString());
                    if (isSearchHistoryFragment){
                        Navigation.findNavController(SearchActivity.this,R.id.fragment).navigate(R.id.action_searchHistoryFragment_to_searchFragment);
                        isSearchHistoryFragment = false;
                       SearchFragment fragment = (SearchFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
                       fragment.mSearchRecyclerVierAdapter.list = list;
                    }




                   // Navigation.createNavigateOnClickListener(R.id.action_searchHistoryFragment_to_searchFragment);
                }

                break;

        }
    }*/
/*

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
                        list = songSearchForResultListBean.getData().getInfo();

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
*/



}
