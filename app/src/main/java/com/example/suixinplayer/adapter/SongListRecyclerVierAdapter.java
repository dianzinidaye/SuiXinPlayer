package com.example.suixinplayer.adapter;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.suixinplayer.R;
import com.example.suixinplayer.app.App;
import com.example.suixinplayer.bean.SongSearchForResultListBean;
import com.example.suixinplayer.callback.HistoryRecyclerViewSelectOnclickListener;
import com.example.suixinplayer.db.DBUtil;
import com.example.suixinplayer.db.SongDB;
import com.example.suixinplayer.liveDataBus.event.CanclePupUpWdEvent;
import com.example.suixinplayer.liveDataBus.event.PlayEvet;
import com.jeremyliao.liveeventbus.LiveEventBus;

import java.util.ArrayList;
import java.util.List;

/*
 * 长按查询列表的弹窗ListView的Adapter
 * */
public class SongListRecyclerVierAdapter extends RecyclerView.Adapter<SongListRecyclerVierAdapter.VH> {

    public   List<PlayEvet> list;
    private PopupWindow popupWindow;
    private View view;
    private Context mContext;
    private  SongDB mSongDB = new SongDB();


    public SongListRecyclerVierAdapter(List<PlayEvet> list, Context context,PopupWindow popupWindow) {
        this.list = list;
        mContext = context;
        this.popupWindow = popupWindow;

    }


    public class VH extends RecyclerView.ViewHolder {
        private TextView vip;
        TextView mTextViewSong;
        TextView mTextViewSinger;
        LinearLayout linearLayout;

        public VH(View view) {
            super(view);

            vip = view.findViewById(R.id.vip);
            mTextViewSong = view.findViewById(R.id.tv_item_history_song);
            mTextViewSinger = view.findViewById(R.id.tv_item_history_singer);
            linearLayout = (LinearLayout) view;
        }

    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //LayoutInflater.from指定写法
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false);
        view = v;
        return new VH(v);
    }


    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {

        holder.mTextViewSong.setText(list.get(position).songName);
        holder.mTextViewSinger.setText(list.get(position).author);
        if (list.get(position).isFree==0) {
            holder.vip.setVisibility(View.GONE);
        }

        /*
         * 点击事件,播放查询列表的音乐
         *
         * */
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //播放音乐
                Log.i("TAG", "onClick: 播放音乐");
                //代码的公共部分
                LiveEventBus.get("Play", PlayEvet.class)
                        .post(list.get(position));

                mSongDB.setHash(list.get(position).hash);
                mSongDB.setAuthor(list.get(position).author);
                mSongDB.setIs_free_part(list.get(position).isFree);
                mSongDB.setSongName(list.get(position).songName);
                App.songInMainActivityBean.playList.add(list.get(position));
                DBUtil.insert(DBUtil.getDatabase(mContext), "最近播放", mSongDB);
                popupWindow.dismiss();

            }
        });


        /*
         * 获取点击时的坐标,用作绘制弹窗
         * */
/*        holder.linearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                x = event.getRawX();
                y = event.getRawY();
                return false;
            }
        });*/



        /*
         * 长按事件,长按删除该收藏
         * */
        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {



                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
