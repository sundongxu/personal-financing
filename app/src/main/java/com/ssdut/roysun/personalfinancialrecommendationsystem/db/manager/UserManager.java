package com.ssdut.roysun.personalfinancialrecommendationsystem.db.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.User;
import com.ssdut.roysun.personalfinancialrecommendationsystem.db.UserSqliteHelper;

import java.util.ArrayList;

/**
 * Created by roysun on 16/4/30.
 * 用户管理
 * 数据库关闭的时机
 */
public class UserManager {

    public static final String TAG = "UserManager";
    private static int DB_VERSION = 1;  //数据库版本
    //    public UserManager(Context context) {
//        mDBHelper = new UserSqliteHelper(context, DB_NAME, null, DB_VERSION);
//        mSQLiteDB = mDBHelper.getWritableDatabase();  //在获取helper之后就可获取数据库实例
//    }
    //单例模式，饿汉模式，天生线程安全
    private static UserManager sUserManager;
    private boolean mIsSignIn;  //登录标志位
    private boolean mIsSpecialAccount;  //管理员账户标志位
    private User mCurUser;  //当前用户
    private String DB_NAME = "user.db";  //数据库名称
    private SQLiteDatabase mSQLiteDB;
    private UserSqliteHelper mDBHelper;
    private Context mContext;

    private UserManager(Context context) {
        mDBHelper = new UserSqliteHelper(context, DB_NAME, null, DB_VERSION);
        mSQLiteDB = mDBHelper.getWritableDatabase();  // 打开数据库
        mContext = context;
        mCurUser = null;
    }

    //静态工厂方法
    public synchronized static UserManager getInstance(Context context) {
        if (sUserManager == null) {
            sUserManager = new UserManager(context);
        }
        return sUserManager;
    }

    public void close() {
        mSQLiteDB.close();
        mDBHelper.close();
    }

    // 管理员可以通过该方法获取所有已注册用户，点击第三个tab的第三个功能卡片，只有管理员才可以跳转到对应Activity
    public ArrayList<User> getUserListFromDB(String selection) {
        ArrayList<User> _userList = new ArrayList<User>();
        Cursor cursor = mSQLiteDB.query(UserSqliteHelper.USER, null, selection, null, null, null, "ID DESC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
            User _user = new User();
            _user.setId(cursor.getInt(0));
            _user.setName(cursor.getString(1));
            _user.setPassword(cursor.getString(2));
            _user.setPic(cursor.getString(3));
            _user.setCreateTime(cursor.getString(4));
            _user.setUpdateTime(cursor.getString(5));
            _user.setQuestion(cursor.getString(6));
            _user.setAnswer(cursor.getString(7));
            _user.setSpecial(cursor.getInt(8));
            _userList.add(_user);
            cursor.moveToNext();
        }
        cursor.close();
        return _userList;
    }

//    public User getUserFromDB(String selection) {
//        User _user = new User();
//        ArrayList<User> _userList = getUserListFromDB(selection);
//        if (_userList.size() == 1) {
//            //唯一匹配
//            _user = _userList.get(0);
//            return _user;
//        } else {
//            return null;
//        }
//    }

    public User getUserFromDB(String userName) {
        User _user = new User();
        boolean _isUserExist = false;
        Cursor cursor = mSQLiteDB.query(UserSqliteHelper.USER, null, "NAME ='" + userName + "'", null, null, null, null);
        _isUserExist = cursor.moveToFirst();
        while (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
            _user.setId(cursor.getInt(0));
            _user.setName(cursor.getString(1));
            _user.setPassword(cursor.getString(2));
            _user.setPic(cursor.getString(3));
            _user.setCreateTime(cursor.getString(4));
            _user.setUpdateTime(cursor.getString(5));
            _user.setQuestion(cursor.getString(6));
            _user.setAnswer(cursor.getString(7));
            _user.setSpecial(cursor.getInt(8));
            cursor.moveToNext();
            cursor.close();
        }
        return _isUserExist ? _user : null;
    }

    public User getCurUser() {
        return mCurUser;
    }

    public void signIn(String userName, String password) {
        if (mCurUser == null && isUserLegal(userName, password)) {
            // 已经获取到了当前用户mCurUser
            mIsSignIn = true;
        } else {
            mIsSignIn = false;
        }
    }

    public void signOut() {
        mCurUser = null;
        mIsSignIn = false;
        mIsSpecialAccount = false;
    }

    public boolean isSignIn() {
        return mIsSignIn;
    }

    public int isSpecialAccount() {
        return mCurUser.isSpecial();
    }

    // 用户增、删、改、查

    public Long register(User user) {
        //insert、add，应该有判重逻辑
        ContentValues values = new ContentValues();
        values.put(User.NAME, user.getName());
        values.put(User.PASSWORD, user.getPassword());
        values.put(User.PIC, user.getPic());
        values.put(User.CREATE_TIME, user.getCreateTime());
        values.put(User.UPDATE_TIME, user.getUpdateTime());
        values.put(User.QUESTION, user.getQuestion());
        values.put(User.ANSWER, user.getAnswer());
        values.put(User.IS_SPECIAL, user.isSpecial());
        Long _newRowId = mSQLiteDB.insert(UserSqliteHelper.USER, User.NAME, values);
        return _newRowId;
    }

    public int deleteUser(int id) {
        //delete
        int _rowsAffectedByDelete = mSQLiteDB.delete(UserSqliteHelper.USER, "ID =" + id, null);
        return _rowsAffectedByDelete;
    }

    public int updateUserInfo(User user, int id) {
        //update
//        mCurUser = user;
        ContentValues values = new ContentValues();
        values.put(User.NAME, user.getName());
        values.put(User.PASSWORD, user.getPassword());
        values.put(User.PIC, user.getPic());
        values.put(User.CREATE_TIME, user.getCreateTime());
        values.put(User.UPDATE_TIME, user.getUpdateTime());
        values.put(User.QUESTION, user.getQuestion());
        values.put(User.ANSWER, user.getAnswer());
        values.put(User.IS_SPECIAL, user.isSpecial());
        int _rowsAffectedByUpdate = mSQLiteDB.update(UserSqliteHelper.USER, values, "ID ='" + id + "'", null);
        return _rowsAffectedByUpdate;
    }

    // 登录失败的原因：（1）用户名或密码输入为空（2）用户名不存在（3）用户名存在，密码匹配失败
    public boolean isUserExists(String userName) {
        boolean flag = false;
        Cursor cursor = mSQLiteDB.query(UserSqliteHelper.USER, null, "NAME ='" + userName + "'", null, null, null, null);
        flag = cursor.moveToFirst();
        cursor.close();
        return flag;
    }

    //根据用户名、密码匹配，只有在登录时才会调用这里，所以同时获取当前用户mCuruser
    public boolean isUserLegal(String userName, String password) {
        //query
        boolean flag = false;
        Cursor cursor = mSQLiteDB.query(UserSqliteHelper.USER, null, "NAME ='" + userName + "'"
                + " and " + "PASSWORD ='" + password + "'", null, null, null, null);
        flag = cursor.moveToFirst();
        if (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
            if (mCurUser == null) {
                mCurUser = new User();
            }
            mCurUser.setId(cursor.getInt(0));
            mCurUser.setName(cursor.getString(1));
            mCurUser.setPassword(cursor.getString(2));
            mCurUser.setPic(cursor.getString(3));
            mCurUser.setCreateTime(cursor.getString(4));
            mCurUser.setUpdateTime(cursor.getString(5));
            mCurUser.setQuestion(cursor.getString(6));
            mCurUser.setAnswer(cursor.getString(7));
            mCurUser.setSpecial(cursor.getInt(8));
        }
        cursor.close();
        return flag;
    }
}
