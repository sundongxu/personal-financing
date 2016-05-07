package com.ssdut.roysun.personalfinancialrecommendationsystem.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.MemoContent;

/**
 * Created by roysun on 16/3/12.
 * 创建MEMO表结构
 */
public class MemoSqliteHelper extends SQLiteOpenHelper {

    public static final String TAG = "MemoManager";
    public static final String MEMO = "MEMO";  //备忘表名

    public MemoSqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //getWritableDatabase()之后调用该方法，生成表结构
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                MEMO + "(" + "ID" + " integer primary key," +
                MemoContent.YEAR + " Integer," +
                MemoContent.MONTH + " Integer," +
                MemoContent.WEEK + " Integer," +
                MemoContent.DAY + " Integer," +
                MemoContent.TIME + " varchar," +
                MemoContent.CONTENT + " varchar," +
                MemoContent.PIC + " varchar," +
                MemoContent.COLOR + " Integer," +
                MemoContent.SIZE + " REAL" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + MEMO);
        onCreate(db);
    }

    public void updateColumn(SQLiteDatabase db, String oldColumn, String newColumn, String typeColumn) {
        try {
            db.execSQL("ALTER TABLE " + MEMO + " CHANGE " + oldColumn + " " + newColumn + " " + typeColumn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * 存储月预算
     * */
    public static void saveBudget(Context context, String filename, String name, int num) {
        SharedPreferences preference = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        // 获取编辑器
        SharedPreferences.Editor editor = preference.edit();
        // 数据暂时存放在内存中
        editor.putInt(name, num);
        // 提交修改，将内存中的数据保存至xawx.xml文件中
        editor.commit();
    }

    /*
     * 读取Preference参数
     */
    public static int readPreferenceFile(Context context, String filename, String name) {
        SharedPreferences preference = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        int num = preference.getInt(name, 0);
        return num;
    }
}
