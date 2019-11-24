package com.example.suixinplayer.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.suixinplayer.liveDataBus.event.PlayEvet;
import com.example.suixinplayer.uitli.SharPUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DBUtil {
    /*
     * 创建一个数据库
     * */
    static public SQLiteDatabase getDatabase(Context context) {
        // /data/data/包名/xxx.db
        File file = context.getExternalFilesDir(null);
        String path = file.getAbsolutePath();
        StringBuilder strB = new StringBuilder();
        path = strB.append(path).append("/database").toString();
        File file1 = new File(path);
        if (!file1.exists()) {
            file1.mkdir();

        } else {
            path = file1.getAbsolutePath() + "/" + "歌单" + ".db";
        }
        path = strB.append("/").append("歌单").append(".db").toString();
        return SQLiteDatabase.openOrCreateDatabase(path, null);
    }

    /*
     * 创建表
     * */
    static public void createTable(SQLiteDatabase db, String tableName, Context context) {
        //创建表SQL语句
        String stu_table = "create table" + " " + tableName + "(_id integer primary key autoincrement,songname text,author text,hash text,is_free_part INTEGER)";
        //执行SQL语句
        db.execSQL(stu_table);
        String s = SharPUtil.getString(context, "歌单");
        s = s + "###" + tableName;
        SharPUtil.putString(context, "歌单", s);
    }


    /*
     *删除表
     * */
    public static void dropTable(SQLiteDatabase db, String tableName, Context context) {
        //删除表的SQL语句
        String sql = "DROP TABLE " + tableName;
        //执行SQL
        db.execSQL(sql);
        String s = SharPUtil.getString(context, "歌单");
        String ss[] = s.split("###");
        StringBuffer stringBuffer = new StringBuffer();
        for (String a : ss) {
            if (!a.equals(tableName)){
            stringBuffer.append(a).append("###") ;
            }
        }
        Log.i("TAG", "dropTable: "+stringBuffer.toString());
        SharPUtil.putString(context, "歌单", stringBuffer.toString());
    }

    //加入数据
    public static void insert(SQLiteDatabase db, String tableName, SongDB songDB) {
        ContentValues cValue = new ContentValues();
        cValue.put("songname", songDB.getSongName());
        cValue.put("author", songDB.getAuthor());
        cValue.put("hash", songDB.getHash());
        cValue.put("is_free_part", songDB.getIs_free_part());
        db.insert(tableName, null, cValue);
        /*
        //用SQL语句插入
        String stu_sql="insert into stu_table(sname,snumber) values('xiaoming','01005')";
        db.execSQL(sql);*/
    }

    /*
     * 删除第i条数据,并且把后面的数据往前挪一位
     * */
    public static void delete(SQLiteDatabase db, String tableName, int i) {
        //删除条件
        String whereClause = "id=?";
        //删除条件参数
        String[] whereArgs = {String.valueOf(i)};
        //执行删除
        db.delete("stu_table", whereClause, whereArgs);
        String sql = "update" + " " + tableName + "set id = id-1 where id > " + "i";
        // sqLiteDatabase.execSQL("update data set myID=myID-1 where myID>currentCount");
        db.execSQL(sql);
    }

    // 获取当前数据库中的表列表
    public static List<String> getDBTablesList(Context context) {
        String s = SharPUtil.getString(context, "歌单");
        String ss[] = s.split("###");
        List<String> list = new ArrayList();
        for (int i = 0; i < ss.length; i++) {
            if (!ss[i].equals("")) {
                list.add(ss[i]);
            }
        }
        return list;
    }


    public static List<PlayEvet> queryALL(SQLiteDatabase db, String tableName) {
//查询获得游标
        int i = 0;
        Log.i("TAG", "queryALL: 进来了");
        List<PlayEvet> list = new ArrayList<>();
        try {
            Cursor cursor = db.query(tableName, null, null, null, null, null, null);

            while (cursor.moveToNext()) {

                i++;
                PlayEvet playEvet = new PlayEvet();
//获得songName  columnlndex 是从id列开始计,id列为0
                String songName = cursor.getString(1);
                Log.i("TAG", "queryALL22222222: " + cursor.getPosition() + "   " + songName);
                // Log.i("TAG", "queryALL: "+cursor.getPosition()+"  "+songName+"  "+cursor.getCount());
//获得作者
                String author = cursor.getString(2);
//获得hash
                String hash = cursor.getString(3);
//获取是否免费
                int isFree = cursor.getInt(4);
                playEvet.songName = songName;
                playEvet.author = author;
                playEvet.hash = hash;
                playEvet.isFree = isFree;
                list.add(playEvet);

            }


            // }
        } catch (Exception e) {

        }
        return list;
    }

    public static int querCount(SQLiteDatabase db, String tableName) {
//查询获得游标
        int i = 0;
        try {
            Cursor cursor = db.query(tableName, null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                i++;
            }

        } catch (Exception e) {

        }
        return i;
    }

}
