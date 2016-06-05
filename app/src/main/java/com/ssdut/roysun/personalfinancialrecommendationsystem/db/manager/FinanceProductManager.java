package com.ssdut.roysun.personalfinancialrecommendationsystem.db.manager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.FinanceProduct;

import java.util.ArrayList;

/**
 * Created by roysun on 16/5/29.
 * 理财产品管理
 */
public class FinanceProductManager {

    public static final String TAG = "FinanceProductManager";
    private static int DB_VERSION = 1;  //数据库版本
    private static FinanceProductManager sFinanceProductManager;  // 股票管理员单例
    private String DB_NAME = "finance_product.db";  //数据库名称
    private SQLiteDatabase mSQLiteDB;
    private FinanceProductSqliteHelper mDBHelper;
    private Context mContext;

    private FinanceProductManager(Context context) {
        mDBHelper = new FinanceProductSqliteHelper(context, DB_NAME, null, DB_VERSION);
        mSQLiteDB = mDBHelper.getWritableDatabase();  // 打开数据库
        mContext = context;
    }

    //静态工厂方法
    public synchronized static FinanceProductManager getInstance(Context context) {
        if (sFinanceProductManager == null) {
            sFinanceProductManager = new FinanceProductManager(context);
        }
        return sFinanceProductManager;
    }

    // 关闭数据库，把握好时机
    public void close() {
        mSQLiteDB.close();
        mDBHelper.close();
    }

    // 返回理财产品列表
    public ArrayList<FinanceProduct> getProductListFromDB(String selection) {
        ArrayList<FinanceProduct> _productList = new ArrayList<FinanceProduct>();
        Cursor cursor = mSQLiteDB.query(FinanceProductSqliteHelper.FINANCE_PRODUCT, null, selection, null, null, null, "ID DESC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
            FinanceProduct _product = new FinanceProduct();
            _product.setId(cursor.getInt(0));
            _product.setName(cursor.getString(1));
            _product.setType(cursor.getString(2));
            _product.setReturnRate(cursor.getDouble(3));
            _product.setInvestmentCircle(cursor.getDouble(4));
            _product.setMinPurchaseAmount(cursor.getInt(5));
            _product.setExtraFeePercentage(cursor.getDouble(6));
            _productList.add(_product);
            cursor.moveToNext();
        }
        cursor.close();
        return _productList;
    }

    public FinanceProduct getProductFromDB(String name) {
        FinanceProduct _product = new FinanceProduct();
        boolean _isProductExist = false;
        Cursor cursor = mSQLiteDB.query(FinanceProductSqliteHelper.FINANCE_PRODUCT, null, "NAME ='" + name + "'", null, null, null, null);
        _isProductExist = cursor.moveToFirst();
        while (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
            _product.setId(cursor.getInt(0));
            _product.setName(cursor.getString(1));
            _product.setType(cursor.getString(2));
            _product.setReturnRate(cursor.getDouble(3));
            _product.setInvestmentCircle(cursor.getDouble(4));
            _product.setMinPurchaseAmount(cursor.getInt(5));
            _product.setExtraFeePercentage(cursor.getDouble(6));
            cursor.moveToNext();
            cursor.close();
        }
        return _isProductExist ? _product : null;
    }
}
