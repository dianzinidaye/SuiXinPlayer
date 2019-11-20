package com.example.suixinplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.suixinplayer.R;
import com.example.suixinplayer.widget.RoundedImageView;

public class SongListExtendableListViewAdapter extends BaseExpandableListAdapter {
    private Context mcontext;
    public String[] groupString = {"自创歌单", "收藏歌单"};
    public String[][] childString = {
            {"默认收藏", "李荣浩", "汪峰", "英文歌"},
            {"骚", "非常骚", "超级骚", "无敌骚"},


    };

    @Override
    // 获取分组的个数
    public int getGroupCount() {
        return groupString.length;
    }

    //获取指定分组中的子选项的个数
    @Override
    public int getChildrenCount(int groupPosition) {
        return childString[groupPosition].length;
    }

    //        获取指定的分组数据
    @Override
    public Object getGroup(int groupPosition) {
        return groupString[groupPosition];
    }

    //获取指定分组中的指定子选项数据
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childString[groupPosition][childPosition];
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
     *
     * 获取显示指定组的视图对象
     *
     * @param groupPosition 组位置
     * @param isExpanded 该组是展开状态还是伸缩状态
     * @param convertView 重用已有的视图对象
     * @param parent 返回的视图对象始终依附于的视图组
     */
// 获取显示指定分组的视图    father item
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_first_extendablelistv,parent,false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.tv_songSheet_title = convertView.findViewById(R.id.tv_songSheet_title);
            groupViewHolder.iv_point = convertView.findViewById(R.id.iv_point);
            groupViewHolder.iv_add = convertView.findViewById(R.id.iv_add);
            convertView.setTag(groupViewHolder);
        }else {
            groupViewHolder = (GroupViewHolder)convertView.getTag();
        }
        groupViewHolder.tv_songSheet_title.setText(groupString[groupPosition]);
        return convertView;
    }
    /**
     *
     * 获取一个视图对象，显示指定组中的指定子元素数据。
     *
     * @param groupPosition 组位置
     * @param childPosition 子元素位置
     * @param isLastChild 子元素是否处于组中的最后一个
     * @param convertView 重用已有的视图(View)对象
     * @param parent 返回的视图(View)对象始终依附于的视图组
     * @return
     * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean, android.view.View,
     *      android.view.ViewGroup)
     */

    //取得显示给定分组给定子位置的数据用的视图  child item
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView==null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_second_extendablelistv,parent,false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.iv_album = convertView.findViewById(R.id.iv_album);
            childViewHolder.tv_song_sheet = (TextView)convertView.findViewById(R.id.tv_song_sheet);
            childViewHolder.tv_sheet_song_total = (TextView)convertView.findViewById(R.id.tv_sheet_song_total);
            convertView.setTag(childViewHolder);

        }else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        childViewHolder.iv_album.setImageResource(R.mipmap.love);
        childViewHolder.tv_song_sheet.setText(childString[groupPosition][childPosition]);
        childViewHolder.tv_sheet_song_total.setText("10");
        return convertView;
    }

    //指定位置上的子元素是否可选中
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class GroupViewHolder {
        ImageView iv_point;
        ImageView iv_add;
        TextView tv_songSheet_title;
    }

    static class ChildViewHolder {
        RoundedImageView iv_album;  //歌单的图片
        TextView tv_song_sheet; //歌单名字
        TextView tv_sheet_song_total;//该歌单的歌曲数目

    }
}

