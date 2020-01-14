package com.example.suixinplayer.ui.fragment;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.suixinplayer.R;
import com.example.suixinplayer.adapter.LocalSongRcyViewAdapter;
import com.example.suixinplayer.liveDataBus.event.PlayEvet;
import com.example.suixinplayer.uit.LocalSongUtil;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SecondeFragment extends Fragment {

/*private String TAG = "TAG";
    public SecondeFragment() {
        // Required empty public constructor
    }*/
private RecyclerView mRecyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seconde, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        List<PlayEvet> list = LocalSongUtil.getmusic(getContext());
        Log.i("TAG", "onActivityCreated: "+list.size());
        mRecyclerView = getView().findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(new LocalSongRcyViewAdapter(list));
    }
}
