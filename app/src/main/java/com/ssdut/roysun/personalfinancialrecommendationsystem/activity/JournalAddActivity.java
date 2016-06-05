package com.ssdut.roysun.personalfinancialrecommendationsystem.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.adapter.journal.JournalDetailAdapter;
import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.Expenditure;
import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.Income;
import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.JournalItem;
import com.ssdut.roysun.personalfinancialrecommendationsystem.component.anim.AnimationDelay;
import com.ssdut.roysun.personalfinancialrecommendationsystem.db.manager.JournalManager;
import com.ssdut.roysun.personalfinancialrecommendationsystem.dialog.DialogCategory;
import com.ssdut.roysun.personalfinancialrecommendationsystem.dialog.DialogRemark;
import com.ssdut.roysun.personalfinancialrecommendationsystem.listener.SnackbarClickListener;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.PicUtils;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.SDrw;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.TimeUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by roysun on 16/3/12.
 * 记账界面
 */
public class JournalAddActivity extends PicBaseActivity implements View.OnClickListener {

    public static final String TAG = "JournalAddActivity";

    public static final int CATEGORY_MSG = 1010, REMARK_MSG = 1020;  // 当前界面收到的消息表示（msg.what）
    public static final int EXPENDITURE = 2010, INCOME = 2020, CREDIT_DEBIT = 2030;  // 当前选择的添加类别支出，收入，借贷
    public MessageHandler mMsgHandler;
    private TextView mAmount, mCategory, mDate, mTime, mRemark;  // TextView金额，类别，时间，备注。
    private FrameLayout mExpenditureTab, mIncomeTab, mCreditDebitTab; // FrameLayout支出，收入，借贷，保存，取消。
    private Button mBtnSave, mBtnCancel, mBtnDelete;
    private LinearLayout mPicArea, mNumInputArea;  // LinearLayout图片，底部数字按钮
    private ImageView mExpenditureTabPic, mIncomeTabPic, mCreitDebitTabPic, mPic;  // 顶部选中标识ImageView支出、收入、借贷、图片
    private Button mBtnNum_0, mBtnNum_1, mBtnNum_2, mBtnNum_3, mBtnNum_4, mBtnNum_5, mBtnNum_6, mBtnNum_7, mBtnNum_8, mBtnNum_9, mBtnPoint, mBtnNumDelete;  // 底部数字按钮
    private Button mBtnNum[];
    private TextView mExpenditureTabName, mIncomeTabName, mCreditDebitTabName;  // 顶部支出、收入、借贷，修改时需要改动文本内容
    private int mNowFlag;  // 当前选择的类型
    private int mUpdateType, mUpdateId, mUpdateFlag;  // 更改的类型（支处 收入 借贷）
    private boolean mIsUpdate;  // 判断当前是初始创建还是二次更新
    private String mPicPath;// 文件路径
    private Expenditure mExpenditure;
    private Income mIncome;
    private JournalManager mJournalManager;  // 数据库操作
    private String mCurUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_add);
        initData();
        initView();
        initUpdate();
    }

    @Override
    protected void initData() {
        super.initData();
        mContext = this;
        mBtnNum = new Button[]{mBtnNum_0, mBtnNum_1, mBtnNum_2, mBtnNum_3, mBtnNum_4, mBtnNum_5, mBtnNum_6, mBtnNum_7, mBtnNum_8, mBtnNum_9, mBtnPoint, mBtnNumDelete};  // 按钮集合
        mNowFlag = EXPENDITURE;
        mIsUpdate = false;
        mPicPath = "";
        mExpenditure = new Expenditure();
        mIncome = new Income();
        mJournalManager = JournalManager.getInstance(this);
        mMsgHandler = new MessageHandler();
        mCurUserName = mUserManager.getCurUser().getName();
    }

    @Override
    protected void initView() {
        super.initView();
        if (mToolbar != null) {
            mToolbar.setTitle(R.string.title_journal_add_page);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mAmount = (TextView) findViewById(R.id.tv_amount);
        mAmount.setOnClickListener(new ItemClickListener());
        mCategory = (TextView) findViewById(R.id.tv_category);
        mCategory.setOnClickListener(new ItemClickListener());
        mDate = (TextView) findViewById(R.id.tv_date);
        mDate.setText(TimeUtils.getYear() + "-" + TimeUtils.getMonth() + "-" + TimeUtils.getDay());
        mDate.setOnClickListener(new ItemClickListener());
        mTime = (TextView) findViewById(R.id.tv_time);
        mTime.setText(TimeUtils.getHour() + ":" + TimeUtils.getMinute());
        mTime.setOnClickListener(new ItemClickListener());
        mRemark = (TextView) findViewById(R.id.tv_remark);
        mRemark.setOnClickListener(new ItemClickListener());
        mPic = (ImageView) findViewById(R.id.iv_photo_add);
        mPic.setOnClickListener(new ItemClickListener());
        // mPic linearlayout当选择收入或借贷时隐藏该选项
        mPicArea = (LinearLayout) findViewById(R.id.ll_photo_area);
        mNumInputArea = (LinearLayout) findViewById(R.id.ll_num_input_area);
        mNumInputArea.setVisibility(View.GONE);

        AnimationDelay.startAnim(mNumInputArea, this, mMsgHandler, R.anim.journal_main_menu_appear, 400);

        mExpenditureTabPic = (ImageView) findViewById(R.id.iv_expenditure_tab_img);
        mIncomeTabPic = (ImageView) findViewById(R.id.iv_income_tab_img);
        mCreitDebitTabPic = (ImageView) findViewById(R.id.iv_credit_debit_tab_img);

        mExpenditureTab = (FrameLayout) findViewById(R.id.fl_expenditure_tab);
        mExpenditureTab.setOnClickListener(this);
        mIncomeTab = (FrameLayout) findViewById(R.id.fl_income_tab);
        mIncomeTab.setOnClickListener(this);
        mCreditDebitTab = (FrameLayout) findViewById(R.id.fl_credit_debit_tab);
        mCreditDebitTab.setOnClickListener(this);
        mBtnSave = (Button) findViewById(R.id.btn_journal_save);
        mBtnSave.setOnClickListener(this);
        mBtnCancel = (Button) findViewById(R.id.btn_journal_cancel);
        mBtnCancel.setOnClickListener(this);
        // 删除当前要修改的数据，只在修改时有效
        mBtnDelete = (Button) findViewById(R.id.btn_journal_delete);
        mBtnDelete.setOnClickListener(this);
        mBtnDelete.setVisibility(View.GONE);

        int id[] = new int[]{R.id.btn_num_0, R.id.btn_num_1, R.id.btn_num_2, R.id.btn_num_3, R.id.btn_num_4, R.id.btn_num_5, R.id.btn_num_6, R.id.btn_num_7, R.id.btn_num_8, R.id.btn_num_9, R.id.btn_num_point, R.id.btn_num_delete};
        for (int i = 0; i < mBtnNum.length; i++) {
            mBtnNum[i] = (Button) findViewById(id[i]);
            mBtnNum[i].setOnClickListener(new NumberClickListener(mAmount, this));
        }
    }

    // intent含参数“update”(还含type、id等)，则为二次更新该新增账目页面，初始化该页面
    private void initUpdate() {
        Intent intent = getIntent();
        if (intent.hasExtra("update")) {
            mIsUpdate = true;
            mExpenditureTabName = (TextView) findViewById(R.id.tv_expenditure_tab_name);
            mIncomeTabName = (TextView) findViewById(R.id.tv_income_tab_name);
            mCreditDebitTabName = (TextView) findViewById(R.id.tv_credit_debit_tab_name);
            mBtnDelete.setVisibility(View.VISIBLE);
            mUpdateType = intent.getIntExtra("type", 0);
            mUpdateId = intent.getIntExtra("id", 0);
            ArrayList<?> detailList = JournalDetailAdapter.sDetailList;
            if (mUpdateType == EXPENDITURE) {
                for (Object object : detailList) {
                    Expenditure expenditure = (Expenditure) object;
                    if (mUpdateId == expenditure.getId()) {
                        mExpenditure = expenditure;
                        getExpenditureType(expenditure);
                        return;
                    }
                }
            } else if (mUpdateType == INCOME) {
                for (Object object : detailList) {
                    Income income = (Income) object;
                    if (mUpdateId == income.getId()) {
                        mIncome = income;
                        getIncomeType(income);
                        return;
                    }
                }
            }
        }
    }

    // 判断该支出类型是否为借贷，借出和还款均属于支出
    public void getExpenditureType(Expenditure expenditure) {
        mPicArea.setVisibility(View.GONE);
        if (expenditure.getCategory().equals(JournalItem.LENDING) || expenditure.getCategory().equals(JournalItem.PAYMENT)) {
            mNowFlag = CREDIT_DEBIT;
            mCreditDebitTabName.setText(R.string.journal_update_debit_crebit);
            mCategory.setText(expenditure.getCategory());
            setTopTabBG(mNowFlag, mCreitDebitTabPic);
            mExpenditureTab.setVisibility(View.INVISIBLE);
            mIncomeTab.setVisibility(View.INVISIBLE);
        } else {
            mNowFlag = EXPENDITURE;
            mExpenditureTabName.setText(R.string.journal_update_expenditure);
            mCategory.setText(expenditure.getCategory() + ">" + expenditure.getSubCategory());
            setTopTabBG(mNowFlag, mExpenditureTabPic);
            mCreditDebitTab.setVisibility(View.INVISIBLE);
            mIncomeTab.setVisibility(View.INVISIBLE);
            mPicPath = expenditure.getPic();  // 获取图片路径
            if (mPicPath != null && mPicPath.endsWith("jpg")) {
                File filePic = new File(mPicPath);
                mPic.setImageBitmap(PicUtils.decodeFileAndCompress(filePic));
            }
        }
        mUpdateFlag = EXPENDITURE;  // 用于删除当前数据功能
        mAmount.setText(expenditure.getAmount() + "");
        mDate.setText(expenditure.getYear() + "-" + expenditure.getMonth() + "-" + expenditure.getDay());
        mTime.setText(expenditure.getTime());
        mRemark.setText(expenditure.getRemark());
    }

    // 判断该收入类型是否为借贷，借入和收款 均属于收入
    public void getIncomeType(Income income) {
        mPicArea.setVisibility(View.GONE);
        if (income.getCategory().equals(JournalItem.BORROWING) || income.getCategory().equals(JournalItem.RECEIVEMENT)) {
            mNowFlag = CREDIT_DEBIT;
            mCreditDebitTabName.setText(R.string.journal_update_debit_crebit);
            setTopTabBG(mNowFlag, mCreitDebitTabPic);
            mExpenditureTab.setVisibility(View.INVISIBLE);
            mIncomeTab.setVisibility(View.INVISIBLE);
        } else {
            mNowFlag = INCOME;
            mIncomeTabName.setText(R.string.journal_update_income);
            setTopTabBG(mNowFlag, mIncomeTabPic);
            mCreditDebitTab.setVisibility(View.INVISIBLE);
            mExpenditureTab.setVisibility(View.INVISIBLE);
        }
        mUpdateFlag = INCOME;  // 用于删除当前数据功能
        mAmount.setText(income.getAmount() + "");
        mCategory.setText(income.getCategory());
        mDate.setText(income.getYear() + "-" + income.getMonth() + "-" + income.getDay());
        mTime.setText(income.getTime());
        mRemark.setText(income.getRemark());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_expenditure_tab:  // 支出tab
                setTopTabBG(EXPENDITURE, mExpenditureTabPic);
                mCategory.setText(R.string.journal_add_category_expenditure_default);  // 默认支出类别
                break;
            case R.id.fl_income_tab:  // 收入tab
                setTopTabBG(INCOME, mIncomeTabPic);
                mCategory.setText(R.string.journal_add_category_income_default);  // 默认收入类别
                break;
            case R.id.fl_credit_debit_tab:  // 借贷tab
                setTopTabBG(CREDIT_DEBIT, mCreitDebitTabPic);
                mCategory.setText(R.string.journal_add_category_debit_crebit_default);
                break;
            case R.id.btn_journal_save:
                saveToDB();
                break;
            case R.id.btn_journal_cancel:
                finishSelf();
                break;
            case R.id.btn_journal_delete:
                switch (mUpdateFlag) {
                    case EXPENDITURE:
                        int n1 = mJournalManager.delExpenditureInfo(mUpdateId);
                        if (n1 > 0) {
                            Snackbar.make(mToolbar, R.string.delete_success, Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
                            finishSelf();
                        } else {
                            Snackbar.make(mToolbar, R.string.delete_failure, Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
                            mInputMethodManager.hideSoftInputFromWindow(mToolbar.getWindowToken(), 0);
                        }
                        break;
                    case INCOME:
                        int n2 = mJournalManager.delIncomeInfo(mUpdateId);
                        if (n2 > 0) {
                            Snackbar.make(mToolbar, R.string.delete_success, Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
                            finishSelf();
                        } else {
                            Snackbar.make(mToolbar, R.string.delete_failure, Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
                            mInputMethodManager.hideSoftInputFromWindow(mToolbar.getWindowToken(), 0);
                        }
                        break;
                }
                break;
        }
    }

    // 存储支出、收入、借贷到数据库
    public void saveToDB() {
        Expenditure expenditure = new Expenditure();
        Income income = new Income();
        String category = mCategory.getText().toString().trim();
        String categories[] = category.split(">");
        String date = mDate.getText().toString().trim();
        String dates[] = date.split("-");
        String time = mTime.getText().toString().trim();
        String amount = mAmount.getText().toString().trim();
        String remark = mRemark.getText().toString().trim();

        if (amount.equals("0.00")) {
            Snackbar.make(mToolbar, R.string.journal_add_amount_no_zero, Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
            return;
        }
        if (mNowFlag == EXPENDITURE) {
            expenditure.setUserName(mCurUserName);
            expenditure.setCategory(categories[0]);
            expenditure.setSubCategory(categories[1]);
            expenditure.setYear(Integer.parseInt(dates[0]));
            expenditure.setMonth(Integer.parseInt(dates[1]));
            expenditure.setDay(Integer.parseInt(dates[2]));
            expenditure.setTime(time);
            expenditure.setWeek(TimeUtils.getTheWeekOfYear(Integer.parseInt(dates[0]), Integer.parseInt(dates[1]), Integer.parseInt(dates[2])));
            expenditure.setPic(mPicPath);
            expenditure.setAmount(Double.parseDouble(amount));
            expenditure.setRemark(remark);
            if (!mIsUpdate) {
                mJournalManager.addExpenditureInfo(expenditure);
                Snackbar.make(mToolbar, R.string.journal_add_expenditure_save_success, Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
                mPicPath = "";
            } else {
                mJournalManager.updateExpenditureInfo(expenditure, mExpenditure.getId());
                Snackbar.make(mToolbar, R.string.journal_add_expenditure_update_success, Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
            }
        } else if (mNowFlag == INCOME) {
            income.setUserName(mCurUserName);
            income.setCategory(category);
            income.setYear(Integer.parseInt(dates[0]));
            income.setMonth(Integer.parseInt(dates[1]));
            income.setDay(Integer.parseInt(dates[2]));
            income.setTime(time);
            income.setWeek(TimeUtils.getTheWeekOfYear(Integer.parseInt(dates[0]), Integer.parseInt(dates[1]), Integer.parseInt(dates[2])));
            income.setAmount(Double.parseDouble(amount));
            income.setRemark(remark);
            if (!mIsUpdate) {
                mJournalManager.addIncomeInfo(income);
                Snackbar.make(mToolbar, R.string.journal_add_income_save_success, Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();

            } else {
                mJournalManager.updateIncomeInfo(income, mIncome.getId());
                Snackbar.make(mToolbar, R.string.journal_add_income_update_success, Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
            }
        } else if (mNowFlag == CREDIT_DEBIT) {
            // 借贷中包含借入和借出 分别存储到支出和收入中
            if (category.equals(JournalItem.LENDING) || category.equals(JournalItem.PAYMENT)) {
                expenditure.setUserName(mCurUserName);
                expenditure.setCategory(category);
                expenditure.setSubCategory("");
                expenditure.setYear(Integer.parseInt(dates[0]));
                expenditure.setMonth(Integer.parseInt(dates[1]));
                expenditure.setDay(Integer.parseInt(dates[2]));
                expenditure.setTime(time);
                expenditure.setWeek(TimeUtils.getTheWeekOfYear(Integer.parseInt(dates[0]), Integer.parseInt(dates[1]), Integer.parseInt(dates[2])));
                expenditure.setPic("");
                expenditure.setAmount(Double.parseDouble(amount));
                expenditure.setRemark(remark);
                if (!mIsUpdate) {
                    mJournalManager.addExpenditureInfo(expenditure);
                    Snackbar.make(mToolbar, R.string.journal_add_expenditure_save_success, Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
                } else {
                    mJournalManager.updateExpenditureInfo(expenditure, mExpenditure.getId());
                    Snackbar.make(mToolbar, R.string.journal_add_expenditure_update_success, Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
                }
            } else if (category.equals(JournalItem.BORROWING) || category.equals(JournalItem.RECEIVEMENT)) {
                income.setUserName(mCurUserName);
                income.setCategory(category);
                income.setYear(Integer.parseInt(dates[0]));
                income.setMonth(Integer.parseInt(dates[1]));
                income.setDay(Integer.parseInt(dates[2]));
                income.setTime(time);
                income.setWeek(TimeUtils.getTheWeekOfYear(Integer.parseInt(dates[0]), Integer.parseInt(dates[1]), Integer.parseInt(dates[2])));
                income.setAmount(Double.parseDouble(amount));
                income.setRemark(remark);
                if (!mIsUpdate) {
                    mJournalManager.addIncomeInfo(income);
                    Snackbar.make(mToolbar, R.string.journal_add_debit_crebit_save_success, Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
                } else {
                    mJournalManager.updateIncomeInfo(income, mIncome.getId());
                    Snackbar.make(mToolbar, R.string.journal_add_debit_crebit_update_success, Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
                }
            }
        }
        finish();
        mInputMethodManager.hideSoftInputFromWindow(mToolbar.getWindowToken(), 0);
    }

    // 设置顶部切换按钮的背景及动画
    private void setTopTabBG(int nowFlag, ImageView iv) {
        mNowFlag = nowFlag;  // 赋值给全局变量
        if (nowFlag == EXPENDITURE) {
            if (!mPicArea.isShown()) {
                mPicArea.setVisibility(View.VISIBLE);
                mPicArea.setAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
            }
        } else if (nowFlag == INCOME) {
            if (mPicArea.isShown()) {
                AnimationDelay.dongHuaEnd(mPicArea, this, mMsgHandler, R.anim.push_left_out, 400);
            }
        } else if (nowFlag == CREDIT_DEBIT) {
            if (mPicArea.isShown()) {
                AnimationDelay.dongHuaEnd(mPicArea, this, mMsgHandler, R.anim.push_left_out, 400);
            }
        }
        mIncomeTabPic.setImageDrawable(null);
        mExpenditureTabPic.setImageDrawable(null);
        mCreitDebitTabPic.setImageDrawable(null);
        iv.setImageResource(R.drawable.journal_main_tab_pic);
        iv.setAnimation(AnimationUtils.loadAnimation(this, R.anim.journal_main_top_right2left));
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == R.id.tv_date) {
            // 当点击按钮为R.id.button1显示该dialog
            Calendar c = Calendar.getInstance();
            DatePickerDialog.OnDateSetListener osl = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth + " ");
                }
            };
            new DatePickerDialog(this, 0, osl, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        } else if (id == R.id.tv_time) {
            Calendar c = Calendar.getInstance();
            TimePickerDialog.OnTimeSetListener otl = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    mTime.setText(hourOfDay + ":" + minute);
                }
            };
            new TimePickerDialog(this, 0, otl, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
        }
        return null;
    }

    public boolean onKeyDown(int kCode, KeyEvent kEvent) {
        switch (kCode) {
            case KeyEvent.KEYCODE_BACK: {
                if (mNumInputArea.isShown()) {
                    AnimationDelay.dongHuaEnd(mNumInputArea, JournalAddActivity.this, mMsgHandler, R.anim.journal_main_menu_disappear, 300);
                    return false;
                } else {
                    finish();
                    mInputMethodManager.hideSoftInputFromWindow(mToolbar.getWindowToken(), 0);
                }
            }
        }
        return super.onKeyDown(kCode, kEvent);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        File file;
        Bitmap bitmap;
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PHOTO_FROM_CAMERA:// 获取拍摄的文件
                    mPicPath = captureFile.getAbsolutePath();
                    file = new File(mPicPath);
                    bitmap = PicUtils.decodeFileAndCompress(file);
                    mPic.setImageBitmap(bitmap);
                    break;
                case PHOTO_FROM_DATA:// 获取从图库选择的文件
                    Uri uri = data.getData();
                    String scheme = uri.getScheme();
                    if (scheme.equalsIgnoreCase("file")) {
                        mPicPath = uri.getPath();
                        System.out.println(mPicPath);
                        file = new File(mPicPath);
                        bitmap = PicUtils.decodeFileAndCompress(file);
                        mPic.setImageBitmap(bitmap);
                    } else if (scheme.equalsIgnoreCase("content")) {
                        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                        if (cursor != null) {
                            cursor.moveToFirst();
                            mPicPath = cursor.getString(1);
                            file = new File(mPicPath);
                            bitmap = PicUtils.decodeFileAndCompress(file);
                            mPic.setImageBitmap(bitmap);
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

    private class ItemClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_amount:  //点击num显示数字按键
                    if (mNumInputArea.isShown()) {
                        AnimationDelay.dongHuaEnd(mNumInputArea, JournalAddActivity.this, mMsgHandler, R.anim.journal_main_menu_disappear, 300);
                    } else {
                        mNumInputArea.setAnimation(AnimationUtils.loadAnimation(JournalAddActivity.this, R.anim.journal_main_menu_appear));
                        mNumInputArea.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.tv_category:  //更改类别
                    new DialogCategory(JournalAddActivity.this, mNowFlag);
                    break;
                case R.id.tv_date:  //更改日期
                    onCreateDialog(R.id.tv_date);
                    break;
                case R.id.tv_time:  //更改时间
                    onCreateDialog(R.id.tv_time);
                    break;
                case R.id.tv_remark:  //添加备注
                    String strRemark = mRemark.getText().toString();
                    if (strRemark.equals("无备注")) {
                        new DialogRemark(JournalAddActivity.this, "");
                    } else {
                        new DialogRemark(JournalAddActivity.this, strRemark);
                    }
                    break;
                case R.id.iv_photo_add:
                    if (mPicPath != null && mPicPath.endsWith("jpg")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        Uri uri = Uri.parse("file://" + mPicPath);
                        intent.setDataAndType(uri, "image/*");
                        startActivity(intent);
                    } else {
                        choosePic(JournalAddActivity.this);
                    }
                    break;
            }
        }
    }

    private class NumberClickListener implements View.OnClickListener {
        private TextView tv;
        private Context context;

        public NumberClickListener(TextView tv, Context context) {
            this.tv = tv;
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            Button btn = (Button) v;
            String strAmount = tv.getText().toString().trim();
            if (v.getId() != R.id.btn_num_delete && strAmount.length() > 9) {
                Snackbar.make(mToolbar, R.string.too_much_money, Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
                return;
            }
            if (v.getId() != R.id.btn_num_delete) {
                if (strAmount.equals("0.00")) {
                    //第一次输入时
                    if (!btn.getText().equals(".") && !btn.getText().equals("0")) {
                        tv.setText(btn.getText());
                    }
                } else {
                    if (strAmount.contains(".")) {
                        //金额中已经包含小数点
                        if (btn.getText().equals(".")) {
                            // 输入的为小数点
                            Snackbar.make(mToolbar, R.string.journal_add_not_learn_math, Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
                            return;
                        }
                        //小数点后超过两位时
                        if ((strAmount.length() - strAmount.indexOf(".")) <= 2) {
                            tv.append(btn.getText());
                        } else {
                            Snackbar.make(mToolbar, R.string.journal_add_no_enough_cash, Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
                        }
                    } else {
                        tv.append(btn.getText());
                    }
                }
            } else {
                // 如果是删除键
                if (!strAmount.equals("0.00")) {
                    if (strAmount.length() > 1) {
                        String _str = strAmount.substring(0, strAmount.length() - 1);
                        tv.setText(_str);
                    } else {
                        tv.setText("0.00");
                    }
                }
            }
        }
    }

    public class MessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CATEGORY_MSG:
                    mCategory.setText((String) msg.obj);
                    break;
                case REMARK_MSG:
                    mRemark.setText((String) msg.obj);
                    break;
                default:
                    break;
            }
        }
    }
}
