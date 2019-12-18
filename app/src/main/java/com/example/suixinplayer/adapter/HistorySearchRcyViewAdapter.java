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
import com.example.suixinplayer.liveDataBus.event.PlayEvet;
import com.jeremyliao.liveeventbus.LiveEventBus;

import java.util.List;

public class HistorySearchRcyViewAdapter extends RecyclerView.Adapter<HistorySearchRcyViewAdapter.VH>{
  //  List<String> list = new ArrayList();
   private List<String> list;
   private RecycleViewOnclickListener listener;
    public HistorySearchRcyViewAdapter(List<String> list,RecycleViewOnclickListener listener){
        this.list = list;
        this.listener = listener;

    }

    public void ChangeList(List<String> list) {
        this.list = list;
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
    public HistorySearchRcyViewAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_search, parent, false);
        return new HistorySearchRcyViewAdapter.VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.textView.setText(list.get(position));
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TAG", "onClick: 您选择了"+list.get(position));
                listener.onClick(v,list.get(position));

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface RecycleViewOnclickListener{
        void onClick(View v,String s);
    }
}
