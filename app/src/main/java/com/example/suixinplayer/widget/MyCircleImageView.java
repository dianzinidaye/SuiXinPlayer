package com.example.suixinplayer.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyCircleImageView  extends CircleImageView {
    public MyCircleImageView(Context context) {
        super(context);
    }

    public MyCircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyCircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(heightMeasureSpec, heightMeasureSpec);
    }


}
