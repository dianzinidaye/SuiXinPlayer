package com.example.suixinplayer.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.suixinplayer.R;
import com.example.suixinplayer.app.App;
import com.example.suixinplayer.bean.SongSearchForResultListBean;
import com.example.suixinplayer.db.SongDB;
import com.example.suixinplayer.liveDataBus.event.CanclePupUpWdEvent;
import com.example.suixinplayer.liveDataBus.event.PlayEvet;
import com.jeremyliao.liveeventbus.LiveEventBus;

import java.util.ArrayList;
import java.util.List;

public class LocalSongRcyViewAdapter extends RecyclerView.Adapter<LocalSongRcyViewAdapter.VH>{
  //  List<String> list = new ArrayList();
    List<PlayEvet> list;
    public LocalSongRcyViewAdapter(List<PlayEvet> list){
        this.list = list;

    }


    public class VH extends RecyclerView.ViewHolder {
        private TextView songName;
        private TextView author;
        private ConstraintLayout constraintLayout;
        public VH(@NonNull View itemView) {
            super(itemView);
            songName = itemView.findViewById(R.id.songName);
            author = itemView.findViewById(R.id.author);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);

        }
    }

    @NonNull
    @Override
    public LocalSongRcyViewAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcyview_localsong, parent, false);
        return new LocalSongRcyViewAdapter.VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.songName.setText(list.get(position).songName);
        holder.author.setText(list.get(position).author);
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TAG", "onClick: 您选择了"+list.get(position));
                App.playingListBean.playList = list;
                App.playingListBean.position = position;
                LiveEventBus
                        .get("Play",PlayEvet.class)
                        .post(list.get(position));

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
