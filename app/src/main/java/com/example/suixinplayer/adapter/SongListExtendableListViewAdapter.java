package com.example.suixinplayer.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.suixinplayer.R;
import com.example.suixinplayer.bean.ChildItemBean;
import com.example.suixinplayer.db.DBUtil;
import com.example.suixinplayer.liveDataBus.event.AddSong;
import com.example.suixinplayer.liveDataBus.event.PlayEvet;
import com.example.suixinplayer.uitli.CommandUtil;
import com.example.suixinplayer.widget.CustomBaseDialog;
import com.example.suixinplayer.widget.RoundedImageView;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.MaterialDialog;
import com.flyco.dialog.widget.NormalListDialog;
import com.jeremyliao.liveeventbus.LiveEventBus;

import java.util.ArrayList;
import java.util.List;

import static com.example.suixinplayer.app.App.context;

public class SongListExtendableListViewAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    public String[] groupString = {"我的歌单"};
    public String[] ssss = {"我的歌单","1","2","3","4","5","6","1","2","3","4","5","6","1","2","3","4","5","6","1","2","3","4","5","6","1","2","3","4","5","6","1","2","3","4","5","6"};


    //父列表数据


    //各个子列表的数据

   private List<ChildItemBean> childItemBeansList = new ArrayList<>();
    private AlertDialog.Builder builder;
    private PopupWindow popupWindowSongList;

    public SongListExtendableListViewAdapter(Activity context) {
        mContext = context;
        initSongListDate();
        LiveEventBus.get("ADDSONG", AddSong.class).observeStickyForever(new Observer<AddSong>() {
            @Override
            public void onChanged(AddSong s) {
              //  Log.i("TAG", "onChanged: 收到了更新通知");
                initSongListDate();
               notifyDataSetChanged();

            }
        });


    }

    private void initSongListDate(){
        childItemBeansList.clear();
         List<String> list = DBUtil.getDBTablesList(mContext);
        List<Integer> countList = query(list);
        for (int i =0;i<list.size();i++){
            ChildItemBean childItemBean = new ChildItemBean();
            childItemBean.setTv_sheet_song_total(countList.get(i));
            childItemBean.setTv_song_sheet(list.get(i));
            childItemBeansList.add(childItemBean);
        }



    }


    private List<Integer>query(List<String> tableList){
        List<Integer> list = new ArrayList<>();
        for (String s:tableList) {
           list.add( DBUtil.querCount(DBUtil.getDatabase(mContext),s));
        }
        return list;
    }

    @Override
    // 获取分组的个数
    public int getGroupCount() {
        return groupString.length;
    }

    //获取指定分组中的子选项的个数
    @Override
    public int getChildrenCount(int groupPosition) {

        return childItemBeansList.size();
    }

    //        获取指定的分组数据
    @Override
    public Object getGroup(int groupPosition) {
        return groupString[groupPosition];
    }

    //获取指定分组中的指定子选项数据
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childItemBeansList.get(childPosition);
    }

    //获取指定分组的ID, 这个ID必须是唯一的
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    //获取子选项的ID, 这个ID必须是唯一的
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    //分组和子选项是否持有稳定的ID, 就是说底层数据的改变会不会影响到它们
    @Override
    public boolean hasStableIds() {
        return true;
    }

    /**
     * 获取显示指定组的视图对象
     *
     * @param groupPosition 组位置
     * @param isExpanded    该组是展开状态还是伸缩状态
     * @param convertView   重用已有的视图对象
     * @param parent        返回的视图对象始终依附于的视图组
     */
// 获取显示指定分组的视图    father item
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_first_extendablelistv, parent, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.tv_songSheet_title = convertView.findViewById(R.id.tv_songSheet_title);
            groupViewHolder.iv_point = convertView.findViewById(R.id.iv_point);
            groupViewHolder.iv_add = convertView.findViewById(R.id.iv_add);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        groupViewHolder.tv_songSheet_title.setText(groupString[groupPosition]);
        if (isExpanded) {
            groupViewHolder.iv_point.setImageResource(R.mipmap.up);
        } else {
            groupViewHolder.iv_point.setImageResource(R.mipmap.down);
        }
        groupViewHolder.iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TAG", "onClick: 你点击了添加歌单的图标");
                addSongList(groupPosition);
            }
        });
        return convertView;
    }

    /**
     * 获取一个视图对象，显示指定组中的指定子元素数据。
     *
     * @param groupPosition 组位置
     * @param childPosition 子元素位置
     * @param isLastChild   子元素是否处于组中的最后一个
     * @param convertView   重用已有的视图(View)对象
     * @param parent        返回的视图(View)对象始终依附于的视图组
     * @return
     * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean, android.view.View,
     * android.view.ViewGroup)
     */

    //取得显示给定分组给定子位置的数据用的视图  child item
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_second_extendablelistv, parent, false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.iv_album = convertView.findViewById(R.id.iv_album);
            childViewHolder.tv_song_sheet = convertView.findViewById(R.id.tv_song_sheet);
            childViewHolder.tv_sheet_song_total =  convertView.findViewById(R.id.tv_sheet_song_total);
            convertView.setTag(childViewHolder);


        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        childViewHolder.iv_album.setImageResource(R.mipmap.love);
        childViewHolder.tv_song_sheet.setText(childItemBeansList.get(childPosition).getTv_song_sheet());
        String s = String.valueOf(childItemBeansList.get(childPosition).getTv_sheet_song_total());
        childViewHolder.tv_sheet_song_total.setText(s);

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                deletSongList( childPosition);
                return true;
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TAG", "onActivityCreated: 点击了");
                List<PlayEvet> list =  DBUtil.queryALL(DBUtil.getDatabase(mContext),childItemBeansList.get(childPosition).getTv_song_sheet());
  /*              final NormalListDialog dialog = new NormalListDialog(mContext,ssss);
                dialog.title(childItemBeansList.get(childPosition).getTv_song_sheet())//
                        .titleTextSize_SP(18)//
                        .titleBgColor(Color.parseColor("#409ED7"))//
                        .itemPressColor(Color.parseColor("#85D3EF"))//
                        .itemTextColor(Color.parseColor("#303030"))//
                        .itemTextSize(14)//
                        .cornerRadius(5)//
                        .widthScale(0.8f)//
                        .show();

                dialog.setOnOperItemClickL(new OnOperItemClickL() {
                    @Override
                    public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                       // T.showShort(mContext, mMenuItems.get(position).mOperName);
                        dialog.dismiss();
                    }
                });*/

                LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popRootView = inflater.inflate(R.layout.popupwindow_songlist, null, false);//引入弹窗布局
                // PopUpWindow 传入 ContentView
                popupWindowSongList = new PopupWindow(popRootView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindowSongList.setTouchable(true);
                LinearLayoutManager layoutManager = new LinearLayoutManager(popRootView.getContext());

                RecyclerView recyclerView = popRootView.findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.addItemDecoration(new DividerItemDecoration(v.getContext(), DividerItemDecoration.VERTICAL));
                recyclerView.setAdapter(new SongListRecyclerVierAdapter(list,mContext,popupWindowSongList));
                // 设置背景
                popupWindowSongList.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

                // 外部点击事件
                popupWindowSongList.setOutsideTouchable(true);
                popupWindowSongList.showAtLocation(popRootView, Gravity.BOTTOM, 0, 0);


            }
        });
        return convertView;
    }

    //指定位置上的子元素是否可选中 返回true setOnItemClickListener才有效
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public class GroupViewHolder {
        ImageView iv_point;
        ImageView iv_add;
        TextView tv_songSheet_title;
    }

    public class ChildViewHolder {
        RoundedImageView iv_album;  //歌单的图片
        TextView tv_song_sheet; //歌单名字
        TextView tv_sheet_song_total;//该歌单的歌曲数目

    }

    /**
     * 原生自定义 dialog
     */
    private void addSongList(int groupPosition) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_add_songlist, null);
        final EditText etUsername = view.findViewById(R.id.editText);

        builder = new AlertDialog.Builder(mContext).setView(view).setTitle("新建歌单").setIcon(R.mipmap.ic_launcher)
                .setPositiveButton("确定", (dialogInterface, i) -> {
                    String string = etUsername.getText().toString();
                    if (!CommandUtil.isStartWithNumber(string)) {
                        DBUtil.createTable(DBUtil.getDatabase( mContext), string, mContext);
                       initSongListDate();
                        Log.i("TAG", "onClick: 您已经生成了一个新的歌单" + etUsername.getText()+"  ");
                        notifyDataSetChanged();

                    }else {
                        Toast.makeText(mContext, "创建失败,列表名不能以数字开头哦", Toast.LENGTH_LONG).show();
                    }
                }).setNegativeButton("取消", (dialog, which) -> Log.i("TAG", "onClick: 您已经取消了生成一个新的歌单"));

        builder.create().show();
    }


    /**
     * 原生自定义 dialog
     */
    private void deletSongList(int childPosition) {
/*        Log.i("TAG", "onClick: 删除的位置为" + "  "+childPosition+"   "+childItemBeansList.get(childPosition).getTv_song_sheet());
        builder = new AlertDialog.Builder(mContext).setTitle("删除该歌单")
                .setPositiveButton("确定", (dialogInterface, i) -> {
                   // String string = etUsername.getText().toString();
                  //  if (!CommandUtil.isStartWithNumber(string)) {
                    //    DBUtil.createTable(DBUtil.getDatabase( mContext), string, mContext);
                    DBUtil.dropTable(DBUtil.getDatabase(mContext),childItemBeansList.get(childPosition).getTv_song_sheet() , mContext);
                   initSongListDate();
                    notifyDataSetChanged();
                     //   Log.i("TAG", "onClick: 您已经生成了一个新的歌单" + etUsername.getText()+"  ");
                        notifyDataSetChanged();


                }).setNegativeButton("取消", (dialog, which) -> Log.i("TAG", "onClick: 您已经取消了生成一个新的歌单"));

        builder.create().show();*/


        final MaterialDialog dialog = new MaterialDialog(mContext);
        dialog.content(
                "您确定要删除该歌单吗?不可逆的操作哦!!!")//
                .btnText("取消", "确定")//
              //  .showAnim()//
              //  .dismissAnim(mBasOut)//
                .show();

        dialog.setOnBtnClickL(
                new OnBtnClickL() {//left btn click listener
                    @Override
                    public void onBtnClick() {
                        Log.i("TAG", "onClick: 您已经取消了生成一个新的歌单");
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {//right btn click listener
                    @Override
                    public void onBtnClick() {
                        DBUtil.dropTable(DBUtil.getDatabase(mContext),childItemBeansList.get(childPosition).getTv_song_sheet() , mContext);
                        initSongListDate();
                        notifyDataSetChanged();
                        dialog.dismiss();
                    }
                }
        );




    }
}

