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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.suixinplayer.R;
import com.example.suixinplayer.bean.SongSearchForResultListBean;
import com.example.suixinplayer.callback.HistoryRecyclerViewSelectOnclickListener;
import com.example.suixinplayer.liveDataBus.event.CanclePupUpWdEvent;
import com.jeremyliao.liveeventbus.LiveEventBus;

import java.util.ArrayList;
import java.util.List;

/*
 * 长按查询列表的弹窗ListView的Adapter
 * */
public class SearchRecyclerVierAdapter extends RecyclerView.Adapter<SearchRecyclerVierAdapter.VH> {

    public   List<SongSearchForResultListBean.DataBean.InfoBean> list;
    private PopupWindow popupWindowSongList;
    HistoryRecyclerViewSelectOnclickListener mHistoryRecyclerViewSelectOnclickListener;
    private float y;
    private float x;
    private PopupWindow popupWindow;
    private View view;
    private Context mContext;


    public SearchRecyclerVierAdapter( HistoryRecyclerViewSelectOnclickListener mHistoryRecyclerViewSelectOnclickListener, Context context) {
        this.list = new ArrayList<>();
        mContext = context;
        this.mHistoryRecyclerViewSelectOnclickListener = mHistoryRecyclerViewSelectOnclickListener;
        LiveEventBus
                .get("CanclePUW", CanclePupUpWdEvent.class)
                .observeForever(s -> {
                    Log.i("LiveEventBus", "onChanged:  CanclePUW");
                    if (popupWindow != null) {
                        popupWindow.dismiss();
                    }
                    LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View popRootView = inflater.inflate(R.layout.popupwindow_songlist, null, false);//引入弹窗布局
                    // PopUpWindow 传入 ContentView
                    popupWindowSongList = new PopupWindow(popRootView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    popupWindowSongList.setTouchable(true);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(popRootView.getContext());

                    RecyclerView recyclerView = popRootView.findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(new PopUpWindowRecyclerViewSongListAdapter(context,s.infoBean));
                    // 设置背景
                    popupWindowSongList.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

                    // 外部点击事件
                    popupWindowSongList.setOutsideTouchable(true);
                    popupWindowSongList.showAtLocation(popRootView, Gravity.BOTTOM, 0, 0);


                    // popupWindowSongList.showAsDropDown(view,0,0);
                });

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
            Log.i("TAG", "VH: ");
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

        holder.mTextViewSong.setText(list.get(position).getSongname());
        holder.mTextViewSinger.setText(list.get(position).getSingername());
        if (list.get(position).getTrans_param().getHash_offset()==null) {
            holder.vip.setVisibility(View.GONE);
        }

        /*
         * 点击事件,播放查询列表的音乐
         *
         * */
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TAG", "OnClick: ");
                mHistoryRecyclerViewSelectOnclickListener.deal(position, v);
            }
        });


        /*
         * 获取点击时的坐标,用作绘制弹窗
         * */
        holder.linearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("TAG", "onTouch: ");
                x = event.getRawX();
                y = event.getRawY();
                return false;
            }
        });



        /*
         * 长按事件,弹出窗口选择音质和下载.收藏到表单
         * */
        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                Log.i("TAG", "onLongClick: ");

                LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popRootView = inflater.inflate(R.layout.popupwindow, null, false);//引入弹窗布局
                // PopUpWindow 传入 ContentView
                popupWindow = new PopupWindow(popRootView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setTouchable(true);
                RecyclerView recyclerView = popRootView.findViewById(R.id.recyclerView);

                LinearLayoutManager layoutManager = new LinearLayoutManager(popRootView.getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.addItemDecoration(new DividerItemDecoration(v.getContext(), DividerItemDecoration.VERTICAL));
                recyclerView.setAdapter(new PopUpWindowRecyclerViewAdapter(list.get(position)));
                // 设置背景
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

                // 外部点击事件
                popupWindow.setOutsideTouchable(true);
                // 传入 AnchorView ，锚点实际为 Window
                // Gravity.TOP 在该锚点的正上方
                // Gravity.LEFT 在屏幕左侧
                // Gravity.NO_GRAVITY,在屏幕左上角
                // x 为坐标系 x轴方向的偏移量，左负右正
                // y 为坐标系 y轴方向的偏移量，上负下正

                //2、通过Resources获取获取屏幕宽高
                popupWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED); //这句代码必须要才能获得正确的popupwindow的宽度
                int popupwindowHeight = popupWindow.getContentView().getMeasuredHeight();
                int height = v.getContext().getResources().getDisplayMetrics().heightPixels;
                if (y > height / 2) {
                    popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, (int) x, (int) y - popupwindowHeight);
                } else {
                    popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, (int) x, (int) y);
                }
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
