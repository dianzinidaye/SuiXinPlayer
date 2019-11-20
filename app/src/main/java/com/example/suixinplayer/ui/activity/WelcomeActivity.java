package com.example.suixinplayer.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListener;

import com.example.suixinplayer.R;
import com.example.suixinplayer.base.BaseActivity;

import butterknife.BindView;


public class WelcomeActivity extends BaseActivity implements ViewPropertyAnimatorListener {
    @BindView(R.id.imgView_welcome_activity)
    ImageView imgViewWelcomeActivity;
    private String TAG = "WelcomeActivity";

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initView() {
         ViewCompat.animate(imgViewWelcomeActivity).scaleY(1.0f)
                 .scaleX(1.0f)
                 .setListener(this)
                 .setDuration(2000);//不用加.start()也可以自己开始
    }

    @Override
    public void onAnimationStart(View view) {

    }

    @Override
    public void onAnimationEnd(View view) {
        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
    }

    @Override
    public void onAnimationCancel(View view) {
    }
}
