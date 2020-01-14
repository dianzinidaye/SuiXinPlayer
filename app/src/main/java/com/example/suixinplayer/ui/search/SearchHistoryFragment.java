package com.example.suixinplayer.ui.search;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.suixinplayer.R;
import com.example.suixinplayer.adapter.HistorySearchRcyViewAdapter;
import com.example.suixinplayer.uit.HistorySearchUtil;
import com.google.android.flexbox.FlexboxLayoutManager;


public class SearchHistoryFragment extends Fragment implements View.OnClickListener, HistorySearchRcyViewAdapter.RecycleViewOnclickListener {
    private RecyclerView mHistoryRecyclerView;
    private EditText etv_search;
    private HistorySearchRcyViewAdapter adapter;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    public SearchHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_history, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getView().findViewById(R.id.imageButton_back).setOnClickListener(this);
        getView().findViewById(R.id.imageButton_search).setOnClickListener(this);
        getView().findViewById(R.id.delet_history).setOnClickListener(this);
        etv_search = getView().findViewById(R.id.etv_search);
        mHistoryRecyclerView = getView().findViewById(R.id.history_recyclerView);

        mHistoryRecyclerView.setLayoutManager(new FlexboxLayoutManager(getActivity()));
        adapter = new HistorySearchRcyViewAdapter(HistorySearchUtil.getSearchHistory(getActivity()), this);
        mHistoryRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButton_back:
                getActivity().onBackPressed();
                break;
            case R.id.delet_history:
                HistorySearchUtil.cleanSearchHistory(getContext());
                adapter.ChangeList(HistorySearchUtil.getSearchHistory(getActivity()));
                adapter.notifyDataSetChanged();
                break;
            case R.id.imageButton_search:
                if (etv_search.getText() != null && !etv_search.getText().toString().equals("")) {
                    Log.i("TAG", "Navigation onClick: " + etv_search.getText().toString());
                    HistorySearchUtil.addSearchHistory(getContext(), etv_search.getText().toString());
                    // getSongSearchForResultListBean(etv_search.getText().toString());
                   /* if (isSearchHistoryFragment){
                        Navigation.findNavController(SearchActivity.this,R.id.fragment).navigate(R.id.action_searchHistoryFragment_to_searchFragment);
                        isSearchHistoryFragment = false;
                        SearchFragment fragment = (SearchFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
                        fragment.mSearchRecyclerVierAdapter.list = list;
                    }*/
                    Bundle bundle = new Bundle();
                    bundle.putString("KeyWorld", etv_search.getText().toString());
                    Navigation.findNavController(v).navigate(R.id.action_searchHistoryFragment_to_searchFragment, bundle);


                    // Navigation.createNavigateOnClickListener(R.id.action_searchHistoryFragment_to_searchFragment);
                }

                break;

        }
    }

    @Override
    public void onClick(View v, String s) {
        //etv_search.setText(s);
        HistorySearchUtil.addSearchHistory(getContext(), s);
       // adapter.list = HistorySearchUtil.getSearchHistory(getActivity());
        //adapter.notifyDataSetChanged();
        /*
        * 把光标移到最后
        *
        CharSequence cs=   etv_search.getText();
        if(cs != null && cs instanceof Spannable){
            Selection.setSelection((Spannable) cs, cs.length());
        }*/
        Bundle bundle = new Bundle();
        bundle.putString("KeyWorld", s);
        Navigation.findNavController(v).navigate(R.id.action_searchHistoryFragment_to_searchFragment, bundle);
    }
}
