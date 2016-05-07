package com.ssdut.roysun.personalfinancialrecommendationsystem.db.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.Income;
import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.Expenditure;
import com.ssdut.roysun.personalfinancialrecommendationsystem.db.JournalSqliteHelper;

import java.util.ArrayList;

/**
 * Created by roysun on 16/3/12.
 * INCOME、EXPENDITURE表，数据库增删改查操作
 */
public class JournalManager {

    public static final String TAG = "JournalManager";

    private static int DB_VERSION = 1;
    private String DB_NAME = "accounting.db";  //数据库名称
    private SQLiteDatabase mSQLiteDB;
    private JournalSqliteHelper mDBHelper;
    private Context mContext;

    public JournalManager(Context context) {
        mContext = context;
        mDBHelper = new JournalSqliteHelper(context, DB_NAME, null, DB_VERSION);
        mSQLiteDB = mDBHelper.getWritableDatabase();
    }

    public void close() {
        mSQLiteDB.close();
        mDBHelper.close();
    }

    /**
     * 获取支出表中的所有数据
     * @param selection 选择条件
     * @return
     */
    public ArrayList<Expenditure> getExpenditureListFromDB(String selection) {
        ArrayList<Expenditure> _expenditureList = new ArrayList<Expenditure>();
        Cursor _cursor = mSQLiteDB.query(JournalSqliteHelper.EXPENDITURE, null, selection, null, null, null, "ID DESC");
        _cursor.moveToFirst();
        while (!_cursor.isAfterLast() && (_cursor.getString(1) != null)) {
            Expenditure _expenditure = new Expenditure();
            _expenditure.setId(_cursor.getInt(0));
            _expenditure.setCategory(_cursor.getString(1));
            _expenditure.setSubCategory(_cursor.getString(2));
            _expenditure.setYear(_cursor.getInt(3));
            _expenditure.setMonth(_cursor.getInt(4));
            _expenditure.setWeek(_cursor.getInt(5));
            _expenditure.setDay(_cursor.getInt(6));
            _expenditure.setTime(_cursor.getString(7));
            _expenditure.setPic(_cursor.getString(8));
            _expenditure.setAmount(_cursor.getDouble(9));
            _expenditure.setRemark(_cursor.getString(10));
            _expenditureList.add(_expenditure);
            _cursor.moveToNext();
        }
        _cursor.close();
        return _expenditureList;
    }

    /*
     * 获取收入表中的所有数据
     * */
    public ArrayList<Income> getIncomeListFromDB(String selection) {
        ArrayList<Income> _incomeList = new ArrayList<Income>();
        Cursor _cursor = mSQLiteDB.query(JournalSqliteHelper.INCOME, null, selection, null, null, null, "ID DESC");
        _cursor.moveToFirst();
        while (!_cursor.isAfterLast() && (_cursor.getString(1) != null)) {
            Income _income = new Income();
            _income.setId(_cursor.getInt(0));
            _income.setCategory(_cursor.getString(1));
            _income.setYear(_cursor.getInt(2));
            _income.setMonth(_cursor.getInt(3));
            _income.setWeek(_cursor.getInt(4));
            _income.setDay(_cursor.getInt(5));
            _income.setTime(_cursor.getString(6));
            _income.setAmount(_cursor.getDouble(7));
            _income.setRemark(_cursor.getString(8));
            _incomeList.add(_income);
            _cursor.moveToNext();
        }
        _cursor.close();
        return _incomeList;
    }

    /*
     * 判断某条支出记录是否存在
     * */
    public boolean isExpenditureInfoExist(int id) {
        boolean flag = false;
        Cursor _cursor = mSQLiteDB.query(JournalSqliteHelper.EXPENDITURE, null, "ID ='" + id + "'", null, null, null, null);
        flag = _cursor.moveToFirst();
        _cursor.close();
        return flag;
    }

    /*
     * 判断某条收入记录是否存在
     * */
    public boolean isIncomeInfoExist(int id) {
        boolean flag = false;
        Cursor _cursor = mSQLiteDB.query(JournalSqliteHelper.INCOME, null, "ID ='" + id + "'", null, null, null, null);
        flag = _cursor.moveToFirst();
        _cursor.close();
        return flag;
    }

    /*
     * 更新支出表的记录
     * */
    public int updateExpenditureInfo(Expenditure expenditure, int id) {
        ContentValues values = new ContentValues();
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
        int _rowsAffectedByUpdate = mSQLiteDB.update(JournalSqliteHelper.EXPENDITURE, values, "ID ='" + id + "'", null);
        close();
        return _rowsAffectedByUpdate;
    }

    /*
     * 更新收入表的记录
     * */
    public int updateIncomeInfo(Income income, int id) {
        ContentValues values = new ContentValues();
        values.put(Income.CATEGORY, income.getCategory());
        values.put(Income.YEAR, income.getYear());
        values.put(Income.MONTH, income.getMonth());
        values.put(Income.WEEK, income.getWeek());
        values.put(Income.DAY, income.getDay());
        values.put(Income.TIME, income.getTime());
        values.put(Income.AMOUNT, income.getAmount());
        values.put(Income.REMARK, income.getRemark());
        int _rowsAffectedByUpdate = mSQLiteDB.update(JournalSqliteHelper.INCOME, values, "ID ='" + id + "'", null);
        close();
        return _rowsAffectedByUpdate;
    }

    /*
     * 添加支出记录
     * */
    public Long addExpenditureInfo(Expenditure expenditure) {
        ContentValues values = new ContentValues();
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
        Long _newRowId = mSQLiteDB.insert(JournalSqliteHelper.EXPENDITURE, Expenditure.YEAR, values);
        close();
        return _newRowId;
    }

    /*
     * 添加收入记录
     * */
    public Long addIncomeInfo(Income income) {
        ContentValues values = new ContentValues();
        values.put(Income.CATEGORY, income.getCategory());
        values.put(Income.YEAR, income.getYear());
        values.put(Income.MONTH, income.getMonth());
        values.put(Income.WEEK, income.getWeek());
        values.put(Income.DAY, income.getDay());
        values.put(Income.TIME, income.getTime());
        values.put(Income.AMOUNT, income.getAmount());
        values.put(Income.REMARK, income.getRemark());
        Long _newRowId = mSQLiteDB.insert(JournalSqliteHelper.INCOME, Income.YEAR, values);
        close();
        return _newRowId;
    }

    /*
     * 删除支出表的记录
     * */
    public int delExpenditureInfo(int id) {
        int _rowsAffectedByDelete = mSQLiteDB.delete(JournalSqliteHelper.EXPENDITURE, "ID =" + id, null);
        close();
        return _rowsAffectedByDelete;
    }

    /*
     * 删除收入表的记录
     * */
    public int delIncomeInfo(int id) {
        int _rowsAffectedByDelete = mSQLiteDB.delete(JournalSqliteHelper.INCOME, "ID =" + id, null);
        close();
        return _rowsAffectedByDelete;
    }

    /*
     * 删除所有记录
     * */
    public void delExpenditureAndIncomeAll() {
        JournalSqliteHelper.saveBudget(mContext, JournalSqliteHelper.BUDGET_MONTH, JournalSqliteHelper.BUDGET_MONTH, 0);
        mSQLiteDB.delete(JournalSqliteHelper.EXPENDITURE, null, null);
        mSQLiteDB.delete(JournalSqliteHelper.INCOME, null, null);
    }


}
