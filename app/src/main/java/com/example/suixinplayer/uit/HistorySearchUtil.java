package com.example.suixinplayer.uit;

import android.content.Context;
import android.util.Log;

public class HistorySearchUtil {
    public static SetList<String> getSearchHistory(Context context) {
        SetList<String> list = new SetList<>();
        String mString = SharPUtil.getString(context, "搜索历史");
        String[] s = mString.split("###");
        for (String string :
                s) {
            if (!string.equals("")){
                list.add(string);
            }
        }
        return list;
    }

    public static void addSearchHistory(Context context, String searchString) {
        String string = SharPUtil.getString(context, "搜索历史");
        SetList<String> list = HistorySearchUtil.getSearchHistory(context);
        list.noRepeatAdd(searchString);
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < list.size() && i < 10; i++) {
                buffer.append(list.get(i)).append("###");
        }
        Log.i("TAG", "addSearchHistory: " + buffer.toString());
        SharPUtil.putString(context, "搜索历史", buffer.toString());
    }

    public static void cleanSearchHistory(Context context) {

        SharPUtil.putString(context, "搜索历史", "");
    }
}
