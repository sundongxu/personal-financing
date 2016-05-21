package com.ssdut.roysun.personalfinancialrecommendationsystem.db.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.MemoContent;
import com.ssdut.roysun.personalfinancialrecommendationsystem.db.MemoSqliteHelper;

import java.util.ArrayList;

/**
 * Created by roysun on 16/3/12.
 * Memo表 数据库增删改查操作
 */
public class MemoManager {

    public static final String TAG = "MemoManager";
    private static int DB_VERSION = 1;  //数据库版本
    private String DB_NAME = "memoContent.db";  //数据库名称
    private SQLiteDatabase mSQLiteDB;
    private MemoSqliteHelper mDBHelper;

    public MemoManager(Context context) {
        mDBHelper = new MemoSqliteHelper(context, DB_NAME, null, DB_VERSION);
        mSQLiteDB = mDBHelper.getWritableDatabase();  //在获取helper之后就可获取数据库实例
    }

    public void close() {
        mSQLiteDB.close();
        mDBHelper.close();
    }

    /*
     * 从数据库中获取备忘表中的所有数据
     * */
    public ArrayList<MemoContent> getMemoListFromDB(String selection) {
        ArrayList<MemoContent> _memoList = new ArrayList<MemoContent>();
        Cursor cursor = mSQLiteDB.query(MemoSqliteHelper.MEMO, null, selection, null, null, null, "ID DESC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
            MemoContent _memoContent = new MemoContent();
            _memoContent.setId(cursor.getInt(0));
            _memoContent.setYear(cursor.getInt(1));
            _memoContent.setMonth(cursor.getInt(2));
            _memoContent.setWeek(cursor.getInt(3));
            _memoContent.setDay(cursor.getInt(4));
            _memoContent.setTime(cursor.getString(5));
            _memoContent.setContent(cursor.getString(6));
            _memoContent.setPic(cursor.getString(7));
            _memoContent.setColor(cursor.getInt(8));
            _memoContent.setSize(cursor.getFloat(9));
            _memoList.add(_memoContent);
            cursor.moveToNext();
        }
        cursor.close();
        close();
        return _memoList;
    }

    /*
     * 判断某条是否存在
     * */
    public boolean isMemoInfoExist(int id) {
        boolean flag = false;
        Cursor cursor = mSQLiteDB.query(MemoSqliteHelper.MEMO, null, "ID ='" + id + "'", null, null, null, null);
        flag = cursor.moveToFirst();
        cursor.close();
        return flag;
    }

    /*
     * 更新备忘表记录
     * */
    public int updateMemoInfo(MemoContent memoContent, int id) {
        ContentValues values = new ContentValues();
        values.put(MemoContent.YEAR, memoContent.getYear());
        values.put(MemoContent.MONTH, memoContent.getMonth());
        values.put(MemoContent.WEEK, memoContent.getWeek());
        values.put(MemoContent.DAY, memoContent.getDay());
        values.put(MemoContent.TIME, memoContent.getTime());
        values.put(MemoContent.CONTENT, memoContent.getContent());
        values.put(MemoContent.PIC, memoContent.getPic());
        values.put(MemoContent.BG_COLOR, memoContent.getColor());
        values.put(MemoContent.TEXT_SIZE, memoContent.getSize());
        int _rowsAffectedByUpdate = mSQLiteDB.update(MemoSqliteHelper.MEMO, values, "ID ='" + id + "'", null);
        close();
        return _rowsAffectedByUpdate;
    }

    /*
     * 新增备忘表记录
     * */
    public Long addMemoInfo(MemoContent memoContent) {
        ContentValues values = new ContentValues();
        values.put(MemoContent.YEAR, memoContent.getYear());
        values.put(MemoContent.MONTH, memoContent.getMonth());
        values.put(MemoContent.WEEK, memoContent.getWeek());
        values.put(MemoContent.DAY, memoContent.getDay());
        values.put(MemoContent.TIME, memoContent.getTime());
        values.put(MemoContent.CONTENT, memoContent.getContent());
        values.put(MemoContent.PIC, memoContent.getPic());
        values.put(MemoContent.BG_COLOR, memoContent.getColor());
        values.put(MemoContent.TEXT_SIZE, memoContent.getSize());
        Long _newRowId = mSQLiteDB.insert(MemoSqliteHelper.MEMO, MemoContent.YEAR, values);
        close();
        return _newRowId;
    }

    /*
     * 删除备忘表记录
     * */
    public int deleteMemoInfo(int id) {
        int _rowsAffectedByDelete = mSQLiteDB.delete(MemoSqliteHelper.MEMO, "ID =" + id, null);
        close();
        return _rowsAffectedByDelete;
    }
}
