package com.example.suixinplayer.base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.suixinplayer.R;
import com.example.suixinplayer.app.App;

import butterknife.ButterKnife;


public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

      //  setStatusBarColor(statusBarColor);

       //设置状态栏颜色
      // Window window = getWindow();
       // window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
      //  window.setStatusBarColor(getResources().getColor(R.color.search_button));
        setContentView(getLayoutResId());
  /*      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorAccent));
        }*/
          //  ButterKnife.bind(this);
        App.mActivityManager.attach(this);
        initView();
        initData();

    }

/*    public void setStatusBarColor(int statusBarColorID) {
        //设置状态栏颜色
        Window window = getWindow();
        // window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.setStatusBarColor(statusBarColorID);
    }*/

    protected abstract int getLayoutResId();
    protected void initView(){
    }
    protected void initData(){
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.mActivityManager.detach(this);
    }


}
