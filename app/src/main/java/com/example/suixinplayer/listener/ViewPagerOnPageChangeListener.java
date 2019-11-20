package com.example.suixinplayer.listener;

import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.suixinplayer.callback.ViewPageSelectCallback;

import java.util.List;

public class ViewPagerOnPageChangeListener implements ViewPager.OnPageChangeListener {

    private List<Fragment> viewList;
    private ViewPager viewPager;
    public ViewPageSelectCallback viewPageSelectCallback;

    public ViewPagerOnPageChangeListener(List<Fragment> viewList, ViewPager viewPager,ViewPageSelectCallback viewPageSelectCallback) {
        this.viewList = viewList;
        this.viewPager = viewPager;
        this.viewPageSelectCallback = viewPageSelectCallback;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        /*if (positionOffset == 0) {
            if (position == 0) {
                viewPager.setCurrentItem(viewList.size() - 2, false);
            } else if(position == (viewList.size() - 1)) {
                viewPager.setCurrentItem(1,false);
            }
        }*/
    }

    @Override
    public void onPageSelected(int position) {
        viewPageSelectCallback.dealOnPageSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


}
