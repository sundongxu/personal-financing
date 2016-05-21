package com.ssdut.roysun.personalfinancialrecommendationsystem.db.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.Stock;
import com.ssdut.roysun.personalfinancialrecommendationsystem.db.StockSqliteHelper;
import com.ssdut.roysun.personalfinancialrecommendationsystem.db.UserSqliteHelper;

import java.util.ArrayList;

/**
 * Created by roysun on 16/5/19.
 * 股票管理
 */
public class StockManager {

    public static final String TAG = "StockManager";
    private static int DB_VERSION = 1;  //数据库版本
    private static StockManager sStockManager;  // 股票管理员单例
    private String DB_NAME = "stock.db";  //数据库名称
    private SQLiteDatabase mSQLiteDB;
    private StockSqliteHelper mDBHelper;
    private Context mContext;

    private StockManager(Context context) {
        mDBHelper = new StockSqliteHelper(context, DB_NAME, null, DB_VERSION);
        mSQLiteDB = mDBHelper.getWritableDatabase();  // 打开数据库
        mContext = context;
    }

    //静态工厂方法
    public synchronized static StockManager getInstance(Context context) {
        if (sStockManager == null) {
            sStockManager = new StockManager(context);
        }
        return sStockManager;
    }

    // 关闭数据库，把握好时机
    public void close() {
        mSQLiteDB.close();
        mDBHelper.close();
    }

    // 管理员可以通过该方法获取所有已注册用户，点击第三个tab的第三个功能卡片，只有管理员才可以跳转到对应Activity
    public ArrayList<Stock> getStockListFromDB(String selection) {
        ArrayList<Stock> _stockList = new ArrayList<Stock>();
        Cursor cursor = mSQLiteDB.query(StockSqliteHelper.STOCK, null, selection, null, null, null, "ID DESC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
            Stock _stock = new Stock();
            _stock.setId(cursor.getInt(0));
            _stock.setUserName(cursor.getString(1));
            _stock.setCode(cursor.getString(2));
            _stock.setName(cursor.getString(3));
            _stock.setNowPrice(cursor.getDouble(4));
            _stock.setIncreasePersentage(cursor.getDouble(5));
            _stock.setIncreaseAmount(cursor.getDouble(6));
            _stockList.add(_stock);
            cursor.moveToNext();
        }
        cursor.close();
        return _stockList;
    }

    public Stock getStockFromDB(String code, String userName) {
        Stock _stock = new Stock();
        boolean _isStockExist = false;
        Cursor cursor = mSQLiteDB.query(UserSqliteHelper.USER, null, "CODE ='" + code + "'"
                + " and " + "USER_NAME ='" + userName + "'", null, null, null, null);
        _isStockExist = cursor.moveToFirst();
        while (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
            _stock.setId(cursor.getInt(0));
            _stock.setUserName(cursor.getString(1));
            _stock.setCode(cursor.getString(2));
            _stock.setName(cursor.getString(3));
            _stock.setNowPrice(cursor.getDouble(4));
            _stock.setIncreasePersentage(cursor.getDouble(5));
            _stock.setIncreaseAmount(cursor.getDouble(6));
            cursor.moveToNext();
            cursor.close();
        }
        return _isStockExist ? _stock : null;
    }

    public long addStock(Stock stock) {
        //insert、add，应该有判重逻辑
        ContentValues values = new ContentValues();
        values.put(Stock.USER_NAME, stock.getUserName());
        values.put(Stock.CODE, stock.getCode());
        values.put(Stock.NAME, stock.getName());
        values.put(Stock.NOW_PRICE, stock.getNowPrice());
        values.put(Stock.INCREASE_PERSENTAGE, stock.getIncreasePersentage());
        values.put(Stock.INCREASE_AMOUNT, stock.getIncreaseAmount());
        Long _newRowId = mSQLiteDB.insert(StockSqliteHelper.STOCK, Stock.CODE, values);
        return _newRowId;
    }

    public int deleteStock(int id) {
        //delete
        int _rowsAffectedByDelete = mSQLiteDB.delete(StockSqliteHelper.STOCK, "ID =" + id, null);
        return _rowsAffectedByDelete;
    }

    public int updateStock(Stock stock, int id) {
        //update
        ContentValues values = new ContentValues();
        values.put(Stock.USER_NAME, stock.getUserName());
        values.put(Stock.CODE, stock.getCode());
        values.put(Stock.NAME, stock.getName());
        values.put(Stock.NOW_PRICE, stock.getNowPrice());
        values.put(Stock.INCREASE_PERSENTAGE, stock.getIncreasePersentage());
        values.put(Stock.INCREASE_AMOUNT, stock.getIncreaseAmount());
        int _rowsAffectedByUpdate = mSQLiteDB.update(StockSqliteHelper.STOCK, values, "ID ='" + id + "'", null);
        return _rowsAffectedByUpdate;
    }

    // 判断某一用户是否添加了该Stock作为自选股，这就是去重逻辑
    public boolean isStockExists(Stock stock, String userName) {
        boolean flag = false;
        Cursor cursor = mSQLiteDB.query(StockSqliteHelper.STOCK, null, "USER_NAME ='" + userName + "'"
                + " and " + "CODE ='" + stock.getCode() + "'", null, null, null, null);
        flag = cursor.moveToFirst();
        cursor.close();
        return flag;
    }
}
