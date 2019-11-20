package com.example.suixinplayer.ui.fragment;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.suixinplayer.R;
import com.example.suixinplayer.adapter.SongListExtendableListViewAdapter;
import com.example.suixinplayer.widget.SongListExpandableListView;

/**
 * A simple {@link Fragment} subclass.
 */
public class FirstFragment extends Fragment {

   private String TAG = "TAG";
    private SongListExpandableListView expandableListView;

    public FirstFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i(TAG, "onCreateView: ");
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
       // Log.i(TAG, "onActivityCreated:1 ");
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "FirstFragment onActivityCreated:  "+R.id.textView);
        expandableListView = getView().findViewById(R.id.expandable_listView);
        SongListExtendableListViewAdapter songListExtendableListViewAdapter = new SongListExtendableListViewAdapter();
        expandableListView.setAdapter(songListExtendableListViewAdapter);
        //设置分组的监听
        expandableListView.setOnGroupClickListener((parent, v, groupPosition, id) -> {
            //Toast.makeText(getActivity(),songListExtendableListViewAdapter.groupString[groupPosition], Toast.LENGTH_SHORT).show();
            return false;
        });
        //设置子项布局监听
        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
          //  Toast.makeText(getActivity(), songListExtendableListViewAdapter.childString[groupPosition][childPosition], Toast.LENGTH_SHORT).show();
            return true;

        });


    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy ");
        super.onDestroy();

    }
}
