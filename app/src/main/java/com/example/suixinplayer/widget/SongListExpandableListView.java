package com.example.suixinplayer.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

public class SongListExpandableListView extends ExpandableListView {
    public SongListExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
