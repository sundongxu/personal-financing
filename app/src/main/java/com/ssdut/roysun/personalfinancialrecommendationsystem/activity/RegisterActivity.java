package com.ssdut.roysun.personalfinancialrecommendationsystem.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListAdapter;

import com.iangclifton.android.floatlabel.FloatLabel;
import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.User;
import com.ssdut.roysun.personalfinancialrecommendationsystem.listener.SnackbarClickListener;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.PicUtils;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.SDrw;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.TimeUtils;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.ToastUtils;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by roysun on 16/4/29.
 * MD风格的登录界面
 * 残留一个bug，注册多个用户时MainActivity可能会由于GC操作自动onDestroy退栈，原因：应该是内存不够，MainActivity界面需优化（图片过多过大），必现
 */

public class RegisterActivity extends PicBaseActivity implements View.OnClickListener {

    public static final String TAG = "RegisterActivity";

    private CircleImageView mUserIconView;
    private FloatLabel mUserNameView;
    private FloatLabel mPasswordView;
    private FloatLabel mPasswordRepeatView;
    private FloatLabel mSecurityQuestionView;
    private FloatLabel mSecurityAnswerView;
    private ImageView mUserNameIcon;
    private ImageView mPasswordIcon;
    private ImageView mPasswordRepeatIcon;
    private ImageView mSecurityQuestionIcon;
    private ImageView mSecurityAnswerIcon;
    private CheckBox mSpecialAccountBox;
    private Button mBtnRegister;

    private String mPicPath;  // 文件路径
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
        mIsSpecialAccount = 0;  // 初始为非管理员
    }

    @Override
    protected void initView() {
        super.initView();
        if (mToolbar != null) {
            mToolbar.setTitle(R.string.title_register_page);  // 继承自父类的通用toolbar
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mUserIconView = (CircleImageView) findViewById(R.id.civ_user_icon);
        mUserIconView.setOnClickListener(this);

        mUserNameIcon = (ImageView) findViewById(R.id.iv_user_name_hint);
        mPasswordIcon = (ImageView) findViewById(R.id.iv_password_hint);
        mPasswordRepeatIcon = (ImageView) findViewById(R.id.iv_password_retype_hint);
        mSecurityQuestionIcon = (ImageView) findViewById(R.id.iv_security_question_hint);
        mSecurityAnswerIcon = (ImageView) findViewById(R.id.iv_security_answer_hint);

        mUserNameView = (FloatLabel) findViewById(R.id.et_user_name);
        mPasswordView = (FloatLabel) findViewById(R.id.et_password);
        mPasswordRepeatView = (FloatLabel) findViewById(R.id.et_password_retype);
        mSecurityQuestionView = (FloatLabel) findViewById(R.id.et_security_question);
        mSecurityAnswerView = (FloatLabel) findViewById(R.id.et_security_answer);

        mUserNameView.getEditText().setTextColor(R.color.black);
        mPasswordView.getEditText().setTextColor(R.color.black);
        mPasswordRepeatView.getEditText().setTextColor(R.color.black);
        mSecurityQuestionView.getEditText().setTextColor(R.color.black);
        mSecurityAnswerView.getEditText().setTextColor(R.color.black);

        mUserNameView.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mUserNameIcon.setImageResource(R.drawable.icon_username_hint_input);
                } else {
                    mUserNameIcon.setImageResource(R.drawable.icon_username_hint);
                }
            }
        });

        mPasswordView.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mPasswordIcon.setImageResource(R.drawable.icon_password_hint_input);
                } else {
                    mPasswordIcon.setImageResource(R.drawable.icon_password_hint);
                }
            }
        });

        mPasswordRepeatView.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mPasswordRepeatIcon.setImageResource(R.drawable.icon_password_hint_input);
                } else {
                    mPasswordRepeatIcon.setImageResource(R.drawable.icon_password_hint);
                }
            }
        });

        mSecurityQuestionView.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mSecurityQuestionIcon.setImageResource(R.drawable.icon_security_question_hint_input);
                } else {
                    mSecurityQuestionIcon.setImageResource(R.drawable.icon_security_question_hint);
                }
            }
        });

        mSecurityAnswerView.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mSecurityAnswerIcon.setImageResource(R.drawable.icon_security_answer_hint_input);
                } else {
                    mSecurityAnswerIcon.setImageResource(R.drawable.icon_security_answer_hint);
                }
            }
        });

        mSpecialAccountBox = (CheckBox) findViewById(R.id.cb_special_account);
        mSpecialAccountBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mIsSpecialAccount = 1;
                } else {
                    mIsSpecialAccount = 0;
                }
            }
        });
        mBtnRegister = (Button) findViewById(R.id.btn_register);
        mBtnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.civ_user_icon:
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
                String userName = mUserNameView.getEditText().getText().toString();
                String password = mPasswordView.getEditText().getText().toString();
                String pic = mPicPath;
                String createTime = TimeUtils.getYear() + "." + TimeUtils.getMonth() + "." + TimeUtils.getDay();
                String updateTime = createTime;
                String securityQuestion = mSecurityQuestionView.getEditText().getText().toString();
                String securityAnswer = mSecurityAnswerView.getEditText().getText().toString();
                int isSpecial = mIsSpecialAccount;
                if (mUserManager.isUserExists(userName)) {
                    Snackbar.make(mToolbar, R.string.username_already_exists, Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
                } else {
                    User user = new User();
                    user.setName(userName);
                    user.setPassword(password);
                    user.setPic(pic);
                    user.setCreateTime(createTime);
                    user.setUpdateTime(updateTime);
                    user.setQuestion(securityQuestion);
                    user.setAnswer(securityAnswer);
                    user.setBalance(0.0);
                    user.setBudget(0);
                    user.setSpecial(isSpecial);
                    if (mUserManager.register(user) != -1) {
                        ToastUtils.showMsg(mContext, R.string.register_success);
                        finishSelf();
                    } else {
                        //数据库insert操作出错
                        Snackbar.make(mToolbar, R.string.register_failure, Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
                    }
                }
                break;
        }
    }

    //接收图片信息，更改用户头像
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        File file;
        Bitmap bmp;
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PHOTO_FROM_CAMERA:  // 获取拍摄的文件
                    mPicPath = captureFile.getAbsolutePath();
                    file = new File(mPicPath);
                    bmp = PicUtils.decodeFileAndCompress(file);
                    mUserIconView.setImageBitmap(bmp);
                    break;
                case PHOTO_FROM_DATA:  // 获取从图库选择的文件
                    Uri uri = data.getData();
                    String scheme = uri.getScheme();
                    if (scheme.equalsIgnoreCase("file")) {
                        mPicPath = uri.getPath();
                        file = new File(mPicPath);
                        bmp = PicUtils.decodeFileAndCompress(file);
                        mUserIconView.setImageBitmap(bmp);
                    } else if (scheme.equalsIgnoreCase("content")) {
                        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                        if (cursor != null) {
                            cursor.moveToFirst();
                            mPicPath = cursor.getString(1);
                            file = new File(mPicPath);
                            bmp = PicUtils.decodeFileAndCompress(file);
                            mUserIconView.setImageBitmap(bmp);
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
