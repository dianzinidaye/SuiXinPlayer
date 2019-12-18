package com.example.suixinplayer.ui.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListener;

import com.example.suixinplayer.R;
import com.example.suixinplayer.base.BaseActivity;
import com.example.suixinplayer.db.DBUtil;
import com.example.suixinplayer.liveDataBus.event.PlayEvet;

import java.util.List;

import butterknife.BindView;


public class WelcomeActivity extends BaseActivity implements ViewPropertyAnimatorListener {
    @BindView(R.id.imgView_welcome_activity)
    ImageView imgViewWelcomeActivity;
    private String TAG = "WelcomeActivity";

    @Override
    protected int getLayoutResId() {
        Log.i("TAGG", "getLayoutResId: ");
        return R.layout.activity_welcome;
    }

    @Override
    protected void initView() {
        Log.i("TAGG", "initView:WelcomeActivity ");
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
        Log.i("TAGG", "onAnimationEnd: ");
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onAnimationCancel(View view) {
    }
}
