package com.example.suixinplayer.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.suixinplayer.R;
import com.example.suixinplayer.app.App;
import com.example.suixinplayer.bean.SongInMainActivityBean;
import com.example.suixinplayer.bean.SongSearchForResultListBean;
import com.example.suixinplayer.callback.PopRecyclerViewSelectOnclickListener;
import com.example.suixinplayer.db.DBUtil;
import com.example.suixinplayer.db.SongDB;
import com.example.suixinplayer.liveDataBus.event.PlayEvet;
import com.jeremyliao.liveeventbus.LiveEventBus;

import java.util.ArrayList;
import java.util.List;

/*
 * 首页和播放页面的默认播放列表ListView的Adapter
 * */
public class PopUpWindowRecyclerViewPresentListAdapter extends RecyclerView.Adapter<PopUpWindowRecyclerViewPresentListAdapter.VH> {
    private Context mContext;
    private PopRecyclerViewSelectOnclickListener  popRecyclerViewSelectOnclickListener;

    public PopUpWindowRecyclerViewPresentListAdapter(Context context, SongInMainActivityBean songInMainActivityBean, PopRecyclerViewSelectOnclickListener  popRecyclerViewSelectOnclickListener) {
        mContext = context;
        DBUtil.getDBTablesList(mContext);
        this.popRecyclerViewSelectOnclickListener = popRecyclerViewSelectOnclickListener;
    }


    public class VH extends RecyclerView.ViewHolder {
        private TextView songName;
        private TextView author;
        private ImageView delect;
        public ConstraintLayout constraintLayout;

        public VH(@NonNull View itemView) {
            super(itemView);
            songName = itemView.findViewById(R.id.songName);
            author = itemView.findViewById(R.id.author);
            delect = itemView.findViewById(R.id.delect);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);
        }
    }

    @NonNull
    @Override
    public PopUpWindowRecyclerViewPresentListAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_popupwindow_presentlist, parent, false);
        return new PopUpWindowRecyclerViewPresentListAdapter.VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.songName.setText(App.playingListBean.playList.get(position).songName);
        holder.author.setText(App.playingListBean.playList.get(position).author);
        Log.i("TAG", "onBindViewHolder: position"+position+"   "+App.playingListBean.position);
        if (App.playingListBean.position==position){
            holder.songName.setTextColor(Color.BLUE);
            holder.author.setTextColor(Color.BLUE);
        }else {
            holder.songName.setTextColor(Color.BLACK);
            holder.author.setTextColor(Color.GRAY);
        }
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                 * 播放操作
                 * */
                Log.i("TAG", "onClick: 您选择了"+position+"  " + App.playingListBean.playList.get(position));
                LiveEventBus.get("Play", PlayEvet.class)
                        .post(App.playingListBean.playList.get(position));

                /*
                 * 数据库操作
                 * */
              //  DBUtil.historyAddDate(mContext, App.playingListBean.playList.get(position));
                popRecyclerViewSelectOnclickListener.deal(position, v);
                App.playingListBean.setPosition(position);
            }
        });

        holder.delect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                 * 删除操作
                 * */
            }
        });
    }

    @Override
    public int getItemCount() {
        return App.playingListBean.playList.size();
    }
}
