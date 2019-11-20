package com.example.suixinplayer.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.suixinplayer.R;
import com.example.suixinplayer.bean.SongSearchForResultListBean;
import com.example.suixinplayer.liveDataBus.event.CanclePupUpWdEvent;
import com.jeremyliao.liveeventbus.LiveEventBus;

import java.util.ArrayList;
import java.util.List;

public class PopUpWindowRecyclerViewAdapter extends RecyclerView.Adapter<PopUpWindowRecyclerViewAdapter.VH>{
    List<String> list = new ArrayList();
    SongSearchForResultListBean.DataBean.InfoBean infoBean;
    public PopUpWindowRecyclerViewAdapter(SongSearchForResultListBean.DataBean.InfoBean infoBean){
        this.infoBean = infoBean;
        list.add("标准音质");
        list.add("高音质");
        list.add("无损音质");
        list.add("下载");
        list.add("添加到歌单");
    }


    public class VH extends RecyclerView.ViewHolder {
        private TextView textView;
        public VH(@NonNull View itemView) {
            super(itemView);
           textView = itemView.findViewById(R.id.textView);
        }
    }

    @NonNull
    @Override
    public PopUpWindowRecyclerViewAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_popupwindow, parent, false);
        return new PopUpWindowRecyclerViewAdapter.VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.textView.setText(list.get(position));
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TAG", "onClick: 您选择了"+list.get(position));
                LiveEventBus
                        .get("CanclePUW")
                        .post(new CanclePupUpWdEvent(0,infoBean));

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
