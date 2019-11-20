package com.example.suixinplayer.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.example.suixinplayer.app.App;
import com.example.suixinplayer.ui.fragment.FirstFragment;
import com.example.suixinplayer.ui.fragment.SecondeFragment;
import com.example.suixinplayer.ui.fragment.ThirdFragment;

import java.util.ArrayList;
import java.util.List;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    public List<Fragment> fragmentList;
/*    public MyFragmentPagerAdapter() {

        fragmentList = new ArrayList<>();
        FirstFragment firstFragment = new FirstFragment();
        SecondeFragment secondeFragment = new SecondeFragment();
        ThirdFragment thirdFragment = new ThirdFragment();
        fragmentList.add(firstFragment);
        fragmentList.add(secondeFragment);
        fragmentList.add(thirdFragment);

    }*/

    public MyFragmentPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        fragmentList = new ArrayList<>();
        FirstFragment firstFragment = new FirstFragment();
        SecondeFragment secondeFragment = new SecondeFragment();
        ThirdFragment thirdFragment = new ThirdFragment();
        fragmentList.add(firstFragment);
        fragmentList.add(secondeFragment);
        fragmentList.add(thirdFragment);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }
}
