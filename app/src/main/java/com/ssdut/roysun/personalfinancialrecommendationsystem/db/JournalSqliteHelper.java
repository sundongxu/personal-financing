package com.ssdut.roysun.personalfinancialrecommendationsystem.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.Expenditure;
import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.Income;

/**
 * Created by roysun on 16/3/12.
 * 创建记账表结构：（1）支出表EXPEDITURE （2）收入表INCOME
 */
public class JournalSqliteHelper extends SQLiteOpenHelper {

    public static final String TAG = "JournalSqliteHelper";

    public static final String EXPENDITURE = "EXPENDITURE";// 支出
    public static final String INCOME = "INCOME";// 收入
    public static final String BUDGET_MONTH = "BUDGET_MONTH";//月预算
    public static final String ISHIDDEN = "HIDDEN";

    private Context mContext;

    public JournalSqliteHelper(Context context, String itemName, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, itemName, factory, version);
        this.mContext = context;
    }

    /*
     * 存储月预算或者加密密码，sharedPreferences的使用
     * */
    public static void saveBudget(Context context, String fileName, String itemName, int n) {
        SharedPreferences preference = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();  // 获取编辑器
        editor.putInt(itemName, n);   // 数据暂时存放在内存中
        editor.commit();  // 提交修改，将内存中的数据保存至xawx.xml文件中
    }

    /*
     * 读取Preference参数
     */
    public static int readPreferenceFile(Context context, String fileName, String itemName) {
        SharedPreferences preference = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        int num = preference.getInt(itemName, 0);  //默认值为0，即sharedPreferences文件中没有itemName对应的键值对时，取值为0
        return num;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        saveBudget(mContext, BUDGET_MONTH, BUDGET_MONTH, 3000);
        //这个用来存储当前是否显示提醒   显示为1，不显示为0
        saveBudget(mContext, ISHIDDEN, ISHIDDEN, 1);
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                EXPENDITURE + "(" + "ID" + " integer primary key," +
                Expenditure.USER_NAME + " varchar," +
                Expenditure.CATEGORY + " varchar," +
                Expenditure.SUB_CATEGORY + " varchar," +
                Expenditure.YEAR + " Integer," +
                Expenditure.MONTH + " Integer," +
                Expenditure.WEEK + " Integer," +
                Expenditure.DAY + " Integer," +
                Expenditure.TIME + " varchar," +
                Expenditure.PIC + " varchar," +
                Expenditure.AMOUNT + " REAL," +
                Expenditure.REMARK + " varchar" + ");");

        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                INCOME + "(" + "ID" + " integer primary key," +
                Income.USER_NAME + " varchar," +
                Income.CATEGORY + " varchar," +
                Income.YEAR + " Integer," +
                Income.MONTH + " Integer," +
                Income.WEEK + " Integer," +
                Income.DAY + " Integer," +
                Income.TIME + " varchar," +
                Income.AMOUNT + " REAL," +
                Income.REMARK + " varchar" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + EXPENDITURE);
        db.execSQL("DROP TABLE IF EXISTS" + INCOME);
        onCreate(db);
    }
}
