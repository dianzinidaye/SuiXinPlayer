package com.example.suixinplayer.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.suixinplayer.R;
import com.example.suixinplayer.bean.SongSearchForResultListBean;
import com.example.suixinplayer.db.DBUtil;
import com.example.suixinplayer.db.SongDB;
import com.example.suixinplayer.liveDataBus.event.AddSong;
import com.example.suixinplayer.liveDataBus.event.CanclePupUpWdEvent;
import com.jeremyliao.liveeventbus.LiveEventBus;

import java.util.ArrayList;
import java.util.List;

/*
 * 歌单列表ListView的Adapter
 * */
public class PopUpWindowRecyclerViewSongListAdapter extends RecyclerView.Adapter<PopUpWindowRecyclerViewSongListAdapter.VH> {
    List<String> list = new ArrayList();
    private Context mContext;
    SongSearchForResultListBean.DataBean.InfoBean infoBean;
    private PopupWindow popUpWindow;

    public PopUpWindowRecyclerViewSongListAdapter(Context context, SongSearchForResultListBean.DataBean.InfoBean infoBean, PopupWindow popUpWindow) {
        mContext = context;
        this.infoBean = infoBean;
        list = DBUtil.getDBTablesList(mContext);
        this.popUpWindow = popUpWindow;
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
    public PopUpWindowRecyclerViewSongListAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_popupwindow_songlist, parent, false);
        return new PopUpWindowRecyclerViewSongListAdapter.VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.textView.setText(list.get(position));
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.i("TAG", "onClick: 您选择了" + list.get(position));
                SQLiteDatabase db = DBUtil.getDatabase(mContext);
                SongDB songDB = new SongDB();
                songDB.setSongName(infoBean.getSongname());
                if (infoBean.getTrans_param().getHash_offset() != null) {
                    songDB.setIs_free_part(1);
                } else {
                    songDB.setIs_free_part(0);
                }
                songDB.setAuthor(infoBean.getSingername());
                songDB.setHash(infoBean.getHash());
                DBUtil.insert(db, list.get(position), songDB);
                popUpWindow.dismiss();
                LiveEventBus.get("ADDSONG", AddSong.class).post(new AddSong());
               /* LiveEventBus
                        .get("CanclePUW")
                        .post(new CanclePupUpWdEvent(1));*/

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
