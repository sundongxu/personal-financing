package com.ssdut.roysun.personalfinancialrecommendationsystem.db.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.Stock;
import com.ssdut.roysun.personalfinancialrecommendationsystem.db.StockSqliteHelper;

import java.util.ArrayList;

/**
 * Created by roysun on 16/5/19.
 * 每只股票都会被设置关注用户名，三大股指则设为""
 * 股票管理，包括关注和购买两种情形
 * ① 关注：关注、取消关注
 * ② 购买：买进、卖出
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

    //
    // 根据条件selection字符串过滤，返回符合条件的股票List
    public ArrayList<Stock> getStockListFromDB(String selection) {
        ArrayList<Stock> _stockList = new ArrayList<Stock>();
        Cursor cursor = mSQLiteDB.query(StockSqliteHelper.STOCK, null, selection, null, null, null, "ID DESC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
            Stock _stock = new Stock();
            _stock.setId(cursor.getInt(0));
            _stock.setWatcherName(cursor.getString(1));
            _stock.setBuyNum(cursor.getInt(2));
            _stock.setCode(cursor.getString(3));
            _stock.setName(cursor.getString(4));
            _stock.setNowPrice(cursor.getDouble(5));
            _stock.setIncreasePersentage(cursor.getDouble(6));
            _stock.setIncreaseAmount(cursor.getDouble(7));
            _stockList.add(_stock);
            cursor.moveToNext();
        }
        cursor.close();
        return _stockList;
    }

    // 该方法存在的必要性 -- 根据股票代码和关注用户名得到该条目存储在数据库中的条目id
    public Stock getWatchedStockFromDB(String code, String watcherName) {
        Stock _stock = new Stock();
        boolean _isStockExist = false;
        Cursor cursor = mSQLiteDB.query(StockSqliteHelper.STOCK, null, "CODE ='" + code + "'"
                + " and " + "WATCHER_NAME ='" + watcherName + "'", null, null, null, null);
        _isStockExist = cursor.moveToFirst();
        while (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
            _stock.setId(cursor.getInt(0));
            _stock.setWatcherName(cursor.getString(1));
//            _stock.setBuyerName(cursor.getString(2));
            _stock.setBuyNum(cursor.getInt(2));
            _stock.setCode(cursor.getString(3));
            _stock.setName(cursor.getString(4));
            _stock.setNowPrice(cursor.getDouble(5));
            _stock.setIncreasePersentage(cursor.getDouble(6));
            _stock.setIncreaseAmount(cursor.getDouble(7));
            cursor.moveToNext();
            cursor.close();
        }
        return _isStockExist ? _stock : null;
    }

    // 增 -> 关注，使用前应有判重操作，调用isExistInWatchList
    public long watchStock(Stock stock) {
        ContentValues values = new ContentValues();
        values.put(Stock.WATCHER_NAME, stock.getWatcherName());
        values.put(Stock.BUY_NUMBER, stock.getBuyNum());
        values.put(Stock.CODE, stock.getCode());
        values.put(Stock.NAME, stock.getName());
        values.put(Stock.NOW_PRICE, stock.getNowPrice());
        values.put(Stock.INCREASE_PERSENTAGE, stock.getIncreasePersentage());
        values.put(Stock.INCREASE_AMOUNT, stock.getIncreaseAmount());
        Long _newRowId = mSQLiteDB.insert(StockSqliteHelper.STOCK, Stock.CODE, values);
        return _newRowId;
    }

    // 取消关注，即从自选列表中删除该关注记录
    public int deleteStockFromWatchList(int id) {
        // delete
        int _rowsAffectedByDelete = mSQLiteDB.delete(StockSqliteHelper.STOCK, "ID =" + id, null);
        return _rowsAffectedByDelete;
    }

    // 增 -> 购买，分两种情况，已有购买记录/没有购买记录，即同关注，使用前须有判重操作，调用hasBougnt
    public int buyStock(Stock stock) {
        return updateStockInfo(stock, stock.getId(), true);
    }

    // 一定是已有购买记录，才可以去卖出，卖出上限由Activity去管理，Snackbar提示最多可卖出
    // 卖出，也分两种情况，卖出一部分/全部卖出
    public int sellStock(Stock stock) {
        return updateStockInfo(stock, stock.getId(), true);
    }

    // 直接断点在这里看看谁在修改！！！Timer的锅！！！妈蛋
    public int updateStockInfo(Stock stock, int id, boolean isTrade) {
        //update
        ContentValues values = new ContentValues();
        if (isTrade) {
            // 股票买卖StockTradeActivity，此时需要更新buyNum持有股数量
            values.put(Stock.WATCHER_NAME, stock.getWatcherName());
//        values.put(Stock.BUYER_NAME, stock.getBuyerName());
            values.put(Stock.BUY_NUMBER, stock.getBuyNum());
            values.put(Stock.CODE, stock.getCode());
            values.put(Stock.NAME, stock.getName());
            values.put(Stock.NOW_PRICE, stock.getNowPrice());
            values.put(Stock.INCREASE_PERSENTAGE, stock.getIncreasePersentage());
            values.put(Stock.INCREASE_AMOUNT, stock.getIncreaseAmount());
        } else {
            // 自选股列表自动刷新StockMainActivity，此时不需要更新buyNum
            values.put(Stock.WATCHER_NAME, stock.getWatcherName());
            values.put(Stock.CODE, stock.getCode());
            values.put(Stock.NAME, stock.getName());
            values.put(Stock.NOW_PRICE, stock.getNowPrice());
            values.put(Stock.INCREASE_PERSENTAGE, stock.getIncreasePersentage());
            values.put(Stock.INCREASE_AMOUNT, stock.getIncreaseAmount());
        }

        int _rowsAffectedByUpdate = mSQLiteDB.update(StockSqliteHelper.STOCK, values, "ID ='" + id + "'", null);
        return _rowsAffectedByUpdate;
    }

    // 判断某一用户是否关注了该Stock作为自选股，这就是去重逻辑
    public boolean isExistInWatchList(Stock stock) {
        boolean flag = false;
        Cursor cursor = mSQLiteDB.query(StockSqliteHelper.STOCK, null, "WATCHER_NAME ='" + stock.getWatcherName() + "'"
                + " and " + "CODE ='" + stock.getCode() + "'", null, null, null, null);
        flag = cursor.moveToFirst();
        cursor.close();
        return flag;
    }
}
