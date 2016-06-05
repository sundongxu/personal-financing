package com.ssdut.roysun.personalfinancialrecommendationsystem.db.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.Expenditure;
import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.Income;
import com.ssdut.roysun.personalfinancialrecommendationsystem.db.JournalSqliteHelper;

import java.util.ArrayList;

/**
 * Created by roysun on 16/3/12.
 * INCOME、EXPENDITURE表，数据库增删改查操作
 */
public class JournalManager {

    public static final String TAG = "JournalManager";

    private static int DB_VERSION = 1;
    private static JournalManager sJournalManager;  // 股票管理员单例
    private String DB_NAME = "accounting.db";  //数据库名称
    private SQLiteDatabase mSQLiteDB;
    private JournalSqliteHelper mDBHelper;
    private Context mContext;


    private JournalManager(Context context) {
        mDBHelper = new JournalSqliteHelper(context, DB_NAME, null, DB_VERSION);
        mSQLiteDB = mDBHelper.getWritableDatabase();  // 打开数据库
        mContext = context;
    }

    //静态工厂方法
    public synchronized static JournalManager getInstance(Context context) {
        if (sJournalManager == null) {
            sJournalManager = new JournalManager(context);
        }
        return sJournalManager;
    }

    public void close() {
        mSQLiteDB.close();
        mDBHelper.close();
    }

    // 获取某用户支出数据列表
    public ArrayList<Expenditure> getExpenditureListFromDB(String selection) {
        ArrayList<Expenditure> expenditureList = new ArrayList<Expenditure>();
        Cursor cursor = mSQLiteDB.query(JournalSqliteHelper.EXPENDITURE, null, selection, null, null, null, "ID DESC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
            Expenditure expenditure = new Expenditure();
            expenditure.setId(cursor.getInt(0));
            expenditure.setUserName(cursor.getString(1));
            expenditure.setCategory(cursor.getString(2));
            expenditure.setSubCategory(cursor.getString(3));
            expenditure.setYear(cursor.getInt(4));
            expenditure.setMonth(cursor.getInt(5));
            expenditure.setWeek(cursor.getInt(6));
            expenditure.setDay(cursor.getInt(7));
            expenditure.setTime(cursor.getString(8));
            expenditure.setPic(cursor.getString(9));
            expenditure.setAmount(cursor.getDouble(10));
            expenditure.setRemark(cursor.getString(11));
            expenditureList.add(expenditure);
            cursor.moveToNext();
        }
        cursor.close();
        return expenditureList;
    }

    // 获取某用户收入数据列表
    public ArrayList<Income> getIncomeListFromDB(String selection) {
        ArrayList<Income> incomeList = new ArrayList<Income>();
        Cursor cursor = mSQLiteDB.query(JournalSqliteHelper.INCOME, null, selection, null, null, null, "ID DESC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
            Income income = new Income();
            income.setId(cursor.getInt(0));
            income.setUserName(cursor.getString(1));
            income.setCategory(cursor.getString(2));
            income.setYear(cursor.getInt(3));
            income.setMonth(cursor.getInt(4));
            income.setWeek(cursor.getInt(5));
            income.setDay(cursor.getInt(6));
            income.setTime(cursor.getString(7));
            income.setAmount(cursor.getDouble(8));
            income.setRemark(cursor.getString(9));
            incomeList.add(income);
            cursor.moveToNext();
        }
        cursor.close();
        return incomeList;
    }


    // 判断某条支出记录是否存在
    public boolean isExpenditureInfoExist(int id) {
        boolean flag = false;
        Cursor cursor = mSQLiteDB.query(JournalSqliteHelper.EXPENDITURE, null, "ID ='" + id + "'", null, null, null, null);
        flag = cursor.moveToFirst();
        cursor.close();
        return flag;
    }


    // 判断某条收入记录是否存在
    public boolean isIncomeInfoExist(int id) {
        boolean flag = false;
        Cursor cursor = mSQLiteDB.query(JournalSqliteHelper.INCOME, null, "ID ='" + id + "'", null, null, null, null);
        flag = cursor.moveToFirst();
        cursor.close();
        return flag;
    }


    // 更新支出表条目
    public int updateExpenditureInfo(Expenditure expenditure, int id) {
        ContentValues values = new ContentValues();
        values.put(Expenditure.USER_NAME, expenditure.getUserName());
        values.put(Expenditure.CATEGORY, expenditure.getCategory());
        values.put(Expenditure.SUB_CATEGORY, expenditure.getSubCategory());
        values.put(Expenditure.YEAR, expenditure.getYear());
        values.put(Expenditure.MONTH, expenditure.getMonth());
        values.put(Expenditure.WEEK, expenditure.getWeek());
        values.put(Expenditure.DAY, expenditure.getDay());
        values.put(Expenditure.TIME, expenditure.getTime());
        values.put(Expenditure.PIC, expenditure.getPic());
        values.put(Expenditure.AMOUNT, expenditure.getAmount());
        values.put(Expenditure.REMARK, expenditure.getRemark());
        int iRowsAffectedByUpdate = mSQLiteDB.update(JournalSqliteHelper.EXPENDITURE, values, "ID ='" + id + "'", null);
        return iRowsAffectedByUpdate;
    }

    // 更新收入表条目
    public int updateIncomeInfo(Income income, int id) {
        ContentValues values = new ContentValues();
        values.put(Income.USER_NAME, income.getUserName());
        values.put(Income.CATEGORY, income.getCategory());
        values.put(Income.YEAR, income.getYear());
        values.put(Income.MONTH, income.getMonth());
        values.put(Income.WEEK, income.getWeek());
        values.put(Income.DAY, income.getDay());
        values.put(Income.TIME, income.getTime());
        values.put(Income.AMOUNT, income.getAmount());
        values.put(Income.REMARK, income.getRemark());
        int iRowsAffectedByUpdate = mSQLiteDB.update(JournalSqliteHelper.INCOME, values, "ID ='" + id + "'", null);
        return iRowsAffectedByUpdate;
    }

    // 添加支出条目
    public Long addExpenditureInfo(Expenditure expenditure) {
        ContentValues values = new ContentValues();
        values.put(Expenditure.USER_NAME, expenditure.getUserName());
        values.put(Expenditure.CATEGORY, expenditure.getCategory());
        values.put(Expenditure.SUB_CATEGORY, expenditure.getSubCategory());
        values.put(Expenditure.YEAR, expenditure.getYear());
        values.put(Expenditure.MONTH, expenditure.getMonth());
        values.put(Expenditure.WEEK, expenditure.getWeek());
        values.put(Expenditure.DAY, expenditure.getDay());
        values.put(Expenditure.TIME, expenditure.getTime());
        values.put(Expenditure.PIC, expenditure.getPic());
        values.put(Expenditure.AMOUNT, expenditure.getAmount());
        values.put(Expenditure.REMARK, expenditure.getRemark());
        Long lNewRowId = mSQLiteDB.insert(JournalSqliteHelper.EXPENDITURE, Expenditure.YEAR, values);
        return lNewRowId;
    }

    // 添加收入条目
    public Long addIncomeInfo(Income income) {
        ContentValues values = new ContentValues();
        values.put(Income.USER_NAME, income.getUserName());
        values.put(Income.CATEGORY, income.getCategory());
        values.put(Income.YEAR, income.getYear());
        values.put(Income.MONTH, income.getMonth());
        values.put(Income.WEEK, income.getWeek());
        values.put(Income.DAY, income.getDay());
        values.put(Income.TIME, income.getTime());
        values.put(Income.AMOUNT, income.getAmount());
        values.put(Income.REMARK, income.getRemark());
        Long lNewRowId = mSQLiteDB.insert(JournalSqliteHelper.INCOME, Income.YEAR, values);
        return lNewRowId;
    }

    // 删除支出条目
    public int delExpenditureInfo(int id) {
        int iRowsAffectedByDelete = mSQLiteDB.delete(JournalSqliteHelper.EXPENDITURE, "ID =" + id, null);
        return iRowsAffectedByDelete;
    }

    // 删除收入条目
    public int delIncomeInfo(int id) {
        int iRowsAffectedByDelete = mSQLiteDB.delete(JournalSqliteHelper.INCOME, "ID =" + id, null);
        return iRowsAffectedByDelete;
    }

    // 清楚支出、收入表中全部记录
    public void delExpenditureAndIncomeAll() {
        JournalSqliteHelper.saveBudget(mContext, JournalSqliteHelper.BUDGET_MONTH, JournalSqliteHelper.BUDGET_MONTH, 0);
        mSQLiteDB.delete(JournalSqliteHelper.EXPENDITURE, null, null);
        mSQLiteDB.delete(JournalSqliteHelper.INCOME, null, null);
    }
}
