package com.example.suixinplayer.ui.fragment;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.suixinplayer.R;
import com.example.suixinplayer.adapter.SongListExtendableListViewAdapter;
import com.example.suixinplayer.widget.SongListExpandableListView;

/**
 * A simple {@link Fragment} subclass.
 */
public class FirstFragment extends Fragment {

    private String TAG = "TAG";
    private SongListExpandableListView expandableListView;
    private SongListExtendableListViewAdapter songListExtendableListViewAdapter;

    public FirstFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // Log.i(TAG, "onCreateView: ");
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // Log.i(TAG, "onActivityCreated:1 ");
        super.onActivityCreated(savedInstanceState);
        //Log.i(TAG, "FirstFragment onActivityCreated:  " + R.id.textView);
        expandableListView = getView().findViewById(R.id.expandable_listView);
        songListExtendableListViewAdapter = new SongListExtendableListViewAdapter(getActivity());
        expandableListView.setAdapter(songListExtendableListViewAdapter);
    }

    @Override
    public void onDestroy() {
       // Log.i(TAG, "onDestroy ");
        super.onDestroy();

    }
}
