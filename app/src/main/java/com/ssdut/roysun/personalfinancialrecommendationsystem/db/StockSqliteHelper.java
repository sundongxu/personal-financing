package com.ssdut.roysun.personalfinancialrecommendationsystem.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.Stock;

/**
 * Created by roysun on 16/5/19.
 * 创建Stock表结构
 */
public class StockSqliteHelper extends SQLiteOpenHelper {

    public static final String TAG = "StockSqliteHelper";
    public static final String STOCK = "STOCK";  // 表名

    private Context mContext;

    public StockSqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                STOCK + "(" + "ID" + " integer primary key," +
                Stock.USER_NAME + " varchar," +
                Stock.CODE + " varchar," +
                Stock.NAME + " varchar," +
                Stock.NOW_PRICE + " REAL," +
                Stock.INCREASE_PERSENTAGE + " REAL," +
                Stock.INCREASE_AMOUNT + " REAL" + ");");

        db.execSQL("INSERT INTO " +
                STOCK + "(" +
                Stock.USER_NAME + ", " +
                Stock.CODE + ", " +
                Stock.NAME + ", " +
                Stock.NOW_PRICE + ", " +
                Stock.INCREASE_PERSENTAGE + ", " +
                Stock.INCREASE_AMOUNT + ")" +
                " values" + "(" +
                "'admin', " +
                "'sz002185', " +
                "'华天科技', " +
                "'13.06', " +
                "'+1.48%', " +
                "'+0.19'" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + STOCK);
        onCreate(db);
    }
}
