package com.ssdut.roysun.personalfinancialrecommendationsystem.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.User;

/**
 * Created by roysun on 16/4/30.
 * 创建用户表结构
 */
public class UserSqliteHelper extends SQLiteOpenHelper {

    public static final String TAG = "UserSqliteHelper";
    public static final String USER = "USER";

    private Context mContext;

    // 通过单例模式获取唯一一个UserManager，里面的context传的是getApplicationContext()
    public UserSqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    // 创建用户表，且只创建一次即只会调用一次onCreate，这里fake添加一个管理员账户数据，每次更改表结构都要清除缓存或卸载应用
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                USER + "(" + "ID" + " integer primary key," +
                User.NAME + " varchar," +
                User.PASSWORD + " varchar," +
                User.PIC + " varchar," +
                User.CREATE_TIME + " varchar," +
                User.UPDATE_TIME + " varchar," +
                User.QUESTION + " varchar," +
                User.ANSWER + " varchar," +
                User.BALANCE + " REAL," +
                User.IS_SPECIAL + " Integer" + ");");

        db.execSQL("INSERT INTO " +
                USER + "(" +
                User.NAME + ", " +
                User.PASSWORD + ", " +
                User.PIC + ", " +
                User.CREATE_TIME + ", " +
                User.UPDATE_TIME + ", " +
                User.QUESTION + ", " +
                User.ANSWER + ", " +
                User.BALANCE + ", " +
                User.IS_SPECIAL + ")" +
                " values" + "(" +
                "'admin', " +
                "'admin', " +
                "'', " +
                "'2016.05.01', " +
                "'2016.05.01', " +
                "'你第一次坐飞机是去哪里？', " +
                "'日本东京', " +
                Double.valueOf("2222.22") + ", " +
                1 + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + USER);
        onCreate(db);
    }
}
