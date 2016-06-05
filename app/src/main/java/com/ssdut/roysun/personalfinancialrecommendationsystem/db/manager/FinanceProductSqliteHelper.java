package com.ssdut.roysun.personalfinancialrecommendationsystem.db.manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.FinanceProduct;

/**
 * Created by roysun on 16/5/29.
 * 创建Product表结构
 */
public class FinanceProductSqliteHelper extends SQLiteOpenHelper {

    public static final String TAG = "FinanceProductSqliteHelper";
    public static final String FINANCE_PRODUCT = "FINANCE_PRODUCT";  // 表名

    private Context mContext;

    public FinanceProductSqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                FINANCE_PRODUCT + "(" + "ID" + " integer primary key," +
                FinanceProduct.NAME + " varchar," +
                FinanceProduct.TYPE + " varchar," +
                FinanceProduct.RETURN_RATE + " REAL," +
                FinanceProduct.INVESTMENT_CYCLE + " REAL," +
                FinanceProduct.MIN_PURCHASE_AMOUNT + " Integer," +
                FinanceProduct.EXTRA_FEE_PERCENTAGE + " REAL" + ");");

        db.execSQL("INSERT INTO " +
                FINANCE_PRODUCT + "(" +
                FinanceProduct.NAME + ", " +
                FinanceProduct.TYPE + ", " +
                FinanceProduct.RETURN_RATE + ", " +
                FinanceProduct.INVESTMENT_CYCLE + ", " +
                FinanceProduct.MIN_PURCHASE_AMOUNT + ", " +
                FinanceProduct.EXTRA_FEE_PERCENTAGE + ")" +
                " values" + "(" +
                "'盈盈乐', " +
                "'债券型', " +
                "'0.25', " +
                "'12', " +
                "'10000', " +
                "'0.025'" + ");");

        // 模拟插入更多数据

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + FINANCE_PRODUCT);
        onCreate(db);
    }
}
