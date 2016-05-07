package com.ssdut.roysun.personalfinancialrecommendationsystem.MD.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.PicBaseActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.User;
import com.ssdut.roysun.personalfinancialrecommendationsystem.db.manager.UserManager;
import com.ssdut.roysun.personalfinancialrecommendationsystem.service.SDrw;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.PicUtils;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.TimeUtils;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.ToastUtils;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by roysun on 16/4/29.
 * MD风格的登录界面
 */

public class RegisterActivity extends PicBaseActivity implements View.OnClickListener {

    public static final String TAG = "RegisterActivity";

    private CircleImageView mUserIconView;
    private TextView mUserNameText;
    private EditText mUserNameView;
    private TextView mPasswordText;
    private EditText mPasswordView;
    private TextView mSecurityQuestionText;
    private EditText mSecurityQuestionView;
    private TextView mSecurityAnswerText;
    private EditText mSecurityAnswerView;
    private CheckBox mSpecialAccountBox;
    private Button mBtnRegister;

    private String mPicPath;// 文件路径
    private Context mContext;
    private UserManager mUserManager;
    private int mIsSpecialAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initData();
        initView();
    }

    @Override
    protected void initData() {
        super.initData();
        mPicPath = "";
        mContext = this;
        mUserManager = UserManager.getInstance(getApplicationContext());
        mIsSpecialAccount = 0;  // 初始为非管理员
    }

    @Override
    protected void initView() {
        super.initView();
        if (mToolbar != null) {
            mToolbar.setTitle("用户注册");  // 继承自父类的通用toolbar
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mUserIconView = (CircleImageView) findViewById(R.id.civ_user_icon);
        mUserIconView.setOnClickListener(this);
        mUserNameView = (EditText) findViewById(R.id.et_user_name);
        mPasswordView = (EditText) findViewById(R.id.et_password);
        mSecurityQuestionView = (EditText) findViewById(R.id.et_security_question);
        mSecurityAnswerView = (EditText) findViewById(R.id.et_password);
        mSpecialAccountBox = (CheckBox) findViewById(R.id.cb_remember_me);
        mSpecialAccountBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mIsSpecialAccount = 1;
                }
            }
        });
        mBtnRegister = (Button) findViewById(R.id.btn_register);
        mBtnRegister.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.civ_user_icon:
                // 怎么才能更换图片？
                if (mPicPath != null && mPicPath.endsWith("jpg")) {
                    final Context dialogContext = new ContextThemeWrapper(v.getContext(), android.R.style.Theme_Light);
                    String[] choices;
                    choices = new String[2];
                    choices[0] = "                     查看头像";
                    choices[1] = "                     更换头像";
                    final ListAdapter adapter = new ArrayAdapter<String>(dialogContext, android.R.layout.simple_list_item_1, choices);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(dialogContext);
                    builder.setTitle("                    头像操作");
                    builder.setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            switch (which) {
                                case 0:
                                    Intent _intent = new Intent(Intent.ACTION_VIEW);
                                    Uri uri = Uri.parse("file://" + mPicPath);
                                    _intent.setDataAndType(uri, "image/*");
                                    startActivity(_intent);
                                    break;
                                case 1:
                                    choosePic(RegisterActivity.this);
                                    break;
                            }
                        }
                    });
                    builder.create().show();

                } else {
                    choosePic(RegisterActivity.this);
                }
                break;
            case R.id.btn_register:
                String _userName = mUserNameView.getText().toString();
                String _password = mPasswordView.getText().toString();
                String _pic = mPicPath;
                String _createTime = TimeUtils.getYear() + "." + TimeUtils.getMonth() + "." + TimeUtils.getDay();
                String _updateTime = _createTime;
                String _securityQuestion = mSecurityQuestionView.getText().toString();
                String _securityAnswer = mSecurityAnswerView.getText().toString();
                int _isSpecial = mIsSpecialAccount;
                if (mUserManager.isUserExists(_userName)) {
                    ToastUtils.showMsg(mContext, "用户名已存在，请更改！");
                } else {
                    User _user = new User();
                    _user.setName(_userName);
                    _user.setPassword(_password);
                    _user.setPic(_pic);
                    _user.setCreateTime(_createTime);
                    _user.setUpdateTime(_updateTime);
                    _user.setQuestion(_securityQuestion);
                    _user.setAnswer(_securityAnswer);
                    _user.setSpecial(_isSpecial);
                    if (mUserManager.register(_user) != -1){
                        ToastUtils.showMsg(mContext, "注册成功！密码是："+_user.getPassword());

                    } else {
                        //数据库insert操作出错
                        ToastUtils.showMsg(mContext, "注册失败！");
                    }
                }
                break;
        }
    }

    //接收图片信息，更改用户头像
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        File _file;
        Bitmap _bmp;
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PHOTO_FROM_CAMERA:// 获取拍摄的文件
                    mPicPath = captureFile.getAbsolutePath();
                    _file = new File(mPicPath);
                    _bmp = PicUtils.decodeFileAndCompress(_file);
                    mUserIconView.setImageBitmap(_bmp);
                    break;
                case PHOTO_FROM_DATA:// 获取从图库选择的文件
                    Uri uri = data.getData();
                    String scheme = uri.getScheme();
                    if (scheme.equalsIgnoreCase("file")) {
                        mPicPath = uri.getPath();
                        _file = new File(mPicPath);
                        _bmp = PicUtils.decodeFileAndCompress(_file);
                        mUserIconView.setImageBitmap(_bmp);
                    } else if (scheme.equalsIgnoreCase("content")) {
                        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                        if (cursor != null) {
                            cursor.moveToFirst();
                            mPicPath = cursor.getString(1);
                            _file = new File(mPicPath);
                            _bmp = PicUtils.decodeFileAndCompress(_file);
                            mUserIconView.setImageBitmap(_bmp);
                        }
                    }
                    break;
            }
            // 存放照片的路径
            String savePath = SDrw.SDPATH + "journal/imgcache/";
            mPicPath = PicUtils.compressPic(mPicPath, savePath);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
