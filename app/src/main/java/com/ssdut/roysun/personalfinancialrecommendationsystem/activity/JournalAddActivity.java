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
import com.ssdut.roysun.personalfinancialrecommendationsystem.db.manager.JournalManager;
import com.ssdut.roysun.personalfinancialrecommendationsystem.dialog.DialogCategory;
import com.ssdut.roysun.personalfinancialrecommendationsystem.dialog.DialogRemark;
import com.ssdut.roysun.personalfinancialrecommendationsystem.service.DongHuaYanChi;
import com.ssdut.roysun.personalfinancialrecommendationsystem.service.SDrw;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.PicUtils;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.TimeUtils;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by roysun on 16/3/12.
 * 记账界面
 */
public class JournalAddActivity extends PicBaseActivity implements View.OnClickListener {

    public static final String TAG = "JournalAddActivity";

    // 当前界面收到的消息表示（msg.what）
    public static final int CATEGORY_MSG = 1010, REMARK_MSG = 1020;
    // 当前选择的添加类别支出，收入，借贷
    public static final int EXPENDITURE = 2010, INCOME = 2020, CREDIT_DEBIT = 2030;
    public MessageHandler mMsgHandler;
    private TextView mAmount, mCategory, mDate, mTime, mRemark;  // TextView金额，类别，时间，备注。
    private FrameLayout mExpenditureTab, mIncomeTab, mCreditDebitTab, mBtnSave, mBtnCancel, mBtnDelete;  // FrameLayout支出，收入，借贷，保存，取消。
    private LinearLayout mPicArea, mNumInputArea;  // LinearLayout图片，底部数字按钮
    private ImageView mExpenditureTabPic, mIncomeTabPic, mCreitDebitTabPic, mPic;  // 顶部选中标识ImageView支出，收入，借贷，图片
    private Button mBtnNum_0, mBtnNum_1, mBtnNum_2, mBtnNum_3, mBtnNum_4, mBtnNum_5, mBtnNum_6, mBtnNum_7, mBtnNum_8, mBtnNum_9, mBtnPoint, mBtnNumDelete;  // 底部数字按钮
    private Button mBtnNum[] = new Button[]{mBtnNum_0, mBtnNum_1, mBtnNum_2, mBtnNum_3, mBtnNum_4, mBtnNum_5, mBtnNum_6, mBtnNum_7, mBtnNum_8, mBtnNum_9, mBtnPoint, mBtnNumDelete};  // 按钮集合
    private TextView mExpenditureTabName, mIncomeTabName, mCreditDebitTabName;  // 顶部支出 收入 借贷 文本，修改时需要改动文本内容
    private int mNowFlag = EXPENDITURE;  // 当前选择的类型
    private int mUpdateType, mUpdateId, mUpdateFlag;  // 更改的类型（支处 收入 借贷）
    private boolean isUpdate = false;  // 判断当前是初始创建还是二次更新
    private String mPicPath = "";// 文件路径
    private JournalManager mJournalDataHelper;  // 数据库操作
    /*
     * 如果是以修改的方式打开该界面
     */
    private Expenditure mExpenditure = new Expenditure();
    private Income mIncome = new Income();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_add);
        mMsgHandler = new MessageHandler();
        initView();
        initUpdate();
    }

    @Override
    protected void initView() {
        super.initView();
        // 金额数量
        mAmount = (TextView) findViewById(R.id.tv_amount);
        mAmount.setOnClickListener(new ItemClickListener());
        // 类别
        mCategory = (TextView) findViewById(R.id.tv_category);
        mCategory.setOnClickListener(new ItemClickListener());
        // 日期
        mDate = (TextView) findViewById(R.id.tv_date);
        mDate.setText(TimeUtils.getYear() + "-" + TimeUtils.getMonth() + "-" + TimeUtils.getDay());
        mDate.setOnClickListener(new ItemClickListener());
        // 时间
        mTime = (TextView) findViewById(R.id.tv_time);
        mTime.setText(TimeUtils.getHour() + ":" + TimeUtils.getMinute());
        mTime.setOnClickListener(new ItemClickListener());
        // 备注
        mRemark = (TextView) findViewById(R.id.tv_remark);
        mRemark.setOnClickListener(new ItemClickListener());
        // 图片
        mPic = (ImageView) findViewById(R.id.iv_photo_add);
        mPic.setOnClickListener(new ItemClickListener());
        // mPic linearlayout当选择收入或借贷时隐藏该选项
        mPicArea = (LinearLayout) findViewById(R.id.ll_photo_area);
        // 底部数字按钮
        mNumInputArea = (LinearLayout) findViewById(R.id.ll_num_input_area);
        mNumInputArea.setVisibility(View.GONE);

        DongHuaYanChi.dongHuaStart(mNumInputArea, this, mMsgHandler, R.anim.jz_menu_up, 400);

        mExpenditureTabPic = (ImageView) findViewById(R.id.iv_expenditure_tab_img);
        mIncomeTabPic = (ImageView) findViewById(R.id.iv_income_tab_img);
        mCreitDebitTabPic = (ImageView) findViewById(R.id.iv_credit_debit_tab_img);

        mExpenditureTab = (FrameLayout) findViewById(R.id.fl_expenditure_tab);
        mExpenditureTab.setOnClickListener(this);
        mIncomeTab = (FrameLayout) findViewById(R.id.fl_income_tab);
        mIncomeTab.setOnClickListener(this);
        mCreditDebitTab = (FrameLayout) findViewById(R.id.fl_credit_debit_tab);
        mCreditDebitTab.setOnClickListener(this);
        mBtnSave = (FrameLayout) findViewById(R.id.fl_save);
        mBtnSave.setOnClickListener(this);
        mBtnCancel = (FrameLayout) findViewById(R.id.fl_cancel);
        mBtnCancel.setOnClickListener(this);

        // 删除当前要修改的数据，只在修改时有效
        mBtnDelete = (FrameLayout) this.findViewById(R.id.fl_delete);
        mBtnDelete.setOnClickListener(this);
        mBtnDelete.setVisibility(View.INVISIBLE);

        int id[] = new int[]{R.id.btn_num_0, R.id.btn_num_1, R.id.btn_num_2, R.id.btn_num_3, R.id.btn_num_4, R.id.btn_num_5, R.id.btn_num_6, R.id.btn_num_7, R.id.btn_num_8, R.id.btn_num_9, R.id.btn_num_point, R.id.btn_num_delete};
        for (int i = 0; i < mBtnNum.length; i++) {
            mBtnNum[i] = (Button) this.findViewById(id[i]);
            mBtnNum[i].setOnClickListener(new NumberClickListener(mAmount, this));
        }
    }

    /**
     * intent含参数“update”(还含type、id等)，则为二次更新该新增账目页面，初始化该页面
     */
    private void initUpdate() {
        Intent _intent = getIntent();
        if (_intent.hasExtra("update")) {
            isUpdate = true;
            mExpenditureTabName = (TextView) this.findViewById(R.id.tv_expenditure_tab_name);
            mIncomeTabName = (TextView) this.findViewById(R.id.tv_income_tab_name);
            mCreditDebitTabName = (TextView) this.findViewById(R.id.tv_credit_debit_tab_name);
            mBtnDelete.setVisibility(View.VISIBLE);
            mUpdateType = _intent.getIntExtra("type", 0);
            mUpdateId = _intent.getIntExtra("id", 0);
            ArrayList<?> _detailList = JournalDetailAdapter.sDetailList;
            if (mUpdateType == EXPENDITURE) {
                for (Object object : _detailList) {
                    Expenditure _expenditure = (Expenditure) object;
                    if (mUpdateId == _expenditure.getId()) {
                        mExpenditure = _expenditure;
                        getExpenditureType(_expenditure);
                        return;
                    }
                }
            } else if (mUpdateType == INCOME) {
                for (Object object : _detailList) {
                    Income _income = (Income) object;
                    if (mUpdateId == _income.getId()) {
                        mIncome = _income;
                        getIncomeType(_income);
                        return;
                    }
                }
            }
        }
    }

    /*
     * 判断该支出类型是否为借贷，借出 和 还款 均属于支出
     */
    public void getExpenditureType(Expenditure expenditure) {
        mPicArea.setVisibility(View.GONE);
        if (expenditure.getCategory().equals(JournalItem.LEND) || expenditure.getCategory().equals(JournalItem.PAYMENT)) {
            mNowFlag = CREDIT_DEBIT;
            mCreditDebitTabName.setText("修改借贷");
            mCategory.setText(expenditure.getCategory());
            setTopTabBG(mNowFlag, mCreitDebitTabPic);
            mExpenditureTab.setVisibility(View.INVISIBLE);
            mIncomeTab.setVisibility(View.INVISIBLE);
        } else {
            mNowFlag = EXPENDITURE;
            mExpenditureTabName.setText("修改支出");
            mCategory.setText(expenditure.getCategory() + ">" + expenditure.getSubCategory());
            setTopTabBG(mNowFlag, mExpenditureTabPic);
            mCreditDebitTab.setVisibility(View.INVISIBLE);
            mIncomeTab.setVisibility(View.INVISIBLE);
            mPicPath = expenditure.getPic();  // 获取图片路径
            if (mPicPath != null && mPicPath.endsWith("jpg")) {
                File _filePic = new File(mPicPath);
                mPic.setImageBitmap(PicUtils.decodeFileAndCompress(_filePic));
            }
        }
        mUpdateFlag = EXPENDITURE;  // 用于删除当前数据功能
        mAmount.setText(expenditure.getAmount() + "");
        mDate.setText(expenditure.getYear() + "-" + expenditure.getMonth() + "-" + expenditure.getDay());
        mTime.setText(expenditure.getTime());
        mRemark.setText(expenditure.getRemark());
    }

    /*
     * 判断该收入类型是否为借贷，借入 和 收款 均属于收入
     */
    public void getIncomeType(Income income) {
        mPicArea.setVisibility(View.GONE);
        if (income.getCategory().equals(JournalItem.BORROW) || income.getCategory().equals(JournalItem.RECEIVEMENT)) {
            mNowFlag = CREDIT_DEBIT;  // 用于判断当前状态
            mCreditDebitTabName.setText("修改借贷");
            setTopTabBG(mNowFlag, mCreitDebitTabPic);
            mExpenditureTab.setVisibility(View.INVISIBLE);
            mIncomeTab.setVisibility(View.INVISIBLE);
        } else {
            mNowFlag = INCOME;
            mIncomeTabName.setText("修改收入");
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
                mCategory.setText("餐饮>晚餐");  //默认选项
                break;
            case R.id.fl_income_tab:  // 收入tab
                setTopTabBG(INCOME, mIncomeTabPic);
                mCategory.setText("工资");
                break;
            case R.id.fl_credit_debit_tab:  // 借贷tab
                setTopTabBG(CREDIT_DEBIT, mCreitDebitTabPic);
                mCategory.setText("借出");
                break;
            case R.id.fl_save:  // 保存按钮
                saveToDB();
                break;
            case R.id.fl_cancel:  // 取消按钮
                finish();
                break;
            case R.id.fl_delete:  // 删除按钮
                mJournalDataHelper = new JournalManager(this);
                switch (mUpdateFlag) {
                    case EXPENDITURE:
                        int _num1 = mJournalDataHelper.delExpenditureInfo(mUpdateId);
                        if (_num1 > 0) {
                            ToastUtils.showMsg(this, "删除成功");
                            finish();
                        } else {
                            ToastUtils.showMsg(this, "删除失败");
                        }
                        break;
                    case INCOME:
                        int _num2 = mJournalDataHelper.delIncomeInfo(mUpdateId);
                        if (_num2 > 0) {
                            ToastUtils.showMsg(this, "删除成功");
                            finish();
                        } else {
                            ToastUtils.showMsg(this, "删除失败");
                        }
                        break;
                }
                break;
        }
    }

    /*
     * 存储 支出、收入、借贷 到数据库
     */
    public void saveToDB() {
        mJournalDataHelper = new JournalManager(this);
        Expenditure _expenditure = new Expenditure();
        Income _income = new Income();
        // 类别
        String _category = mCategory.getText().toString().trim();
        String _items[] = _category.split(">");
        // 日期
        String _dateString = mDate.getText().toString().trim();
        String _dates[] = _dateString.split("-");
        // 时间
        String _timeString = mTime.getText().toString().trim();
        // 金额
        String _amountString = mAmount.getText().toString().trim();
        // 备注
        String _remarkString = mRemark.getText().toString().trim();

        if (_amountString.equals("0.00")) {
            ToastUtils.showMsg(this, "金额不能为零");
            return;
        }
        if (mNowFlag == EXPENDITURE) {
            _expenditure.setCategory(_items[0]);
            _expenditure.setSubCategory(_items[1]);
            _expenditure.setYear(Integer.parseInt(_dates[0]));
            _expenditure.setMonth(Integer.parseInt(_dates[1]));
            _expenditure.setDay(Integer.parseInt(_dates[2]));
            _expenditure.setTime(_timeString);
            _expenditure.setWeek(TimeUtils.getTheWeekOfYear(Integer.parseInt(_dates[0]), Integer.parseInt(_dates[1]), Integer.parseInt(_dates[2])));
            _expenditure.setPic(mPicPath);
            _expenditure.setAmount(Double.parseDouble(_amountString));
            _expenditure.setRemark(_remarkString);
            if (!isUpdate) {
                mJournalDataHelper.addExpenditureInfo(_expenditure);
                ToastUtils.showMsg(this, "该条支出存储成功");
                mPicPath = "";
            } else {
                mJournalDataHelper.updateExpenditureInfo(_expenditure, mExpenditure.getId());
                ToastUtils.showMsg(this, "该条支出修改成功");
            }
        } else if (mNowFlag == INCOME) {
            _income.setCategory(_category);
            _income.setYear(Integer.parseInt(_dates[0]));
            _income.setMonth(Integer.parseInt(_dates[1]));
            _income.setDay(Integer.parseInt(_dates[2]));
            _income.setTime(_timeString);
            _income.setWeek(TimeUtils.getTheWeekOfYear(Integer.parseInt(_dates[0]), Integer.parseInt(_dates[1]), Integer.parseInt(_dates[2])));
            _income.setAmount(Double.parseDouble(_amountString));
            _income.setRemark(_remarkString);
            if (!isUpdate) {
                mJournalDataHelper.addIncomeInfo(_income);
                ToastUtils.showMsg(this, "该条收入存储成功");
            } else {
                mJournalDataHelper.updateIncomeInfo(_income, mIncome.getId());
                ToastUtils.showMsg(this, "该条收入修改成功");
            }
        } else if (mNowFlag == CREDIT_DEBIT) {
            // 借贷中包含借入和借出 分别存储到支出和收入中
            if (_category.equals(JournalItem.LEND) || _category.equals(JournalItem.PAYMENT)) {
                _expenditure.setCategory(_category);
                _expenditure.setSubCategory("");
                _expenditure.setYear(Integer.parseInt(_dates[0]));
                _expenditure.setMonth(Integer.parseInt(_dates[1]));
                _expenditure.setDay(Integer.parseInt(_dates[2]));
                _expenditure.setTime(_timeString);
                _expenditure.setWeek(TimeUtils.getTheWeekOfYear(Integer.parseInt(_dates[0]), Integer.parseInt(_dates[1]), Integer.parseInt(_dates[2])));
                _expenditure.setPic("");
                _expenditure.setAmount(Double.parseDouble(_amountString));
                _expenditure.setRemark(_remarkString);
                if (!isUpdate) {
                    mJournalDataHelper.addExpenditureInfo(_expenditure);
                    ToastUtils.showMsg(this, "该条支出存储成功");
                } else {
                    mJournalDataHelper.updateExpenditureInfo(_expenditure, mExpenditure.getId());
                    ToastUtils.showMsg(this, "该条支出修改成功");
                }
            } else if (_category.equals(JournalItem.BORROW) || _category.equals(JournalItem.RECEIVEMENT)) {
                _income.setCategory(_category);
                _income.setYear(Integer.parseInt(_dates[0]));
                _income.setMonth(Integer.parseInt(_dates[1]));
                _income.setDay(Integer.parseInt(_dates[2]));
                _income.setTime(_timeString);
                _income.setWeek(TimeUtils.getTheWeekOfYear(Integer.parseInt(_dates[0]), Integer.parseInt(_dates[1]), Integer.parseInt(_dates[2])));
                _income.setAmount(Double.parseDouble(_amountString));
                _income.setRemark(_remarkString);
                if (!isUpdate) {
                    mJournalDataHelper.addIncomeInfo(_income);
                    ToastUtils.showMsg(this, "该条借贷存储成功");
                } else {
                    mJournalDataHelper.updateIncomeInfo(_income, mIncome.getId());
                    ToastUtils.showMsg(this, "该条借贷修改成功");
                }
            }
        }
        finish();
    }

    /*
     * 设置顶部切换按钮的背景及动画
     */
    private void setTopTabBG(int _nowFlag, ImageView iv) {
        mNowFlag = _nowFlag;  // 赋值给全局变量
        if (_nowFlag == EXPENDITURE) {
            if (!mPicArea.isShown()) {
                mPicArea.setVisibility(View.VISIBLE);
                mPicArea.setAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
            }
        } else if (_nowFlag == INCOME) {
            if (mPicArea.isShown()) {
                DongHuaYanChi.dongHuaEnd(mPicArea, this, mMsgHandler, R.anim.push_left_out, 400);
            }
        } else if (_nowFlag == CREDIT_DEBIT) {
            if (mPicArea.isShown()) {
                DongHuaYanChi.dongHuaEnd(mPicArea, this, mMsgHandler, R.anim.push_left_out, 400);
            }
        }
        mIncomeTabPic.setImageDrawable(null);
        mExpenditureTabPic.setImageDrawable(null);
        mCreitDebitTabPic.setImageDrawable(null);
        iv.setImageResource(R.drawable.jz_tab1_bt_bgs);
        iv.setAnimation(AnimationUtils.loadAnimation(this, R.anim.jz_top_right2left));
    }

    @Override
    protected void onResume() {
        overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
        super.onResume();
    }

    /*
     * 日期dialog
     */
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
                    DongHuaYanChi.dongHuaEnd(mNumInputArea, JournalAddActivity.this, mMsgHandler, R.anim.jz_menu_down, 300);
                    return false;
                } else {
                    finish();
                }
            }
        }
        return super.onKeyDown(kCode, kEvent);
    }

    /*
     * 选择图片的回传处理
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        File _file;
        Bitmap _bmp;
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PHOTO_FROM_CAMERA:// 获取拍摄的文件
                    mPicPath = captureFile.getAbsolutePath();
                    _file = new File(mPicPath);
                    _bmp = PicUtils.decodeFileAndCompress(_file);
                    mPic.setImageBitmap(_bmp);
                    break;
                case PHOTO_FROM_DATA:// 获取从图库选择的文件
                    Uri uri = data.getData();
                    String scheme = uri.getScheme();
                    if (scheme.equalsIgnoreCase("file")) {
                        mPicPath = uri.getPath();
                        System.out.println(mPicPath);
                        _file = new File(mPicPath);
                        _bmp = PicUtils.decodeFileAndCompress(_file);
                        mPic.setImageBitmap(_bmp);
                    } else if (scheme.equalsIgnoreCase("content")) {
                        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                        if (cursor != null) {
                            cursor.moveToFirst();
                            mPicPath = cursor.getString(1);
                            _file = new File(mPicPath);
                            _bmp = PicUtils.decodeFileAndCompress(_file);
                            mPic.setImageBitmap(_bmp);
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
                        DongHuaYanChi.dongHuaEnd(mNumInputArea, JournalAddActivity.this, mMsgHandler, R.anim.jz_menu_down, 300);
                    } else {
                        mNumInputArea.setAnimation(AnimationUtils.loadAnimation(JournalAddActivity.this, R.anim.jz_menu_up));
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
                    String _remarkString = mRemark.getText().toString();
                    if (_remarkString.equals("无备注")) {
                        new DialogRemark(JournalAddActivity.this, "");
                    } else {
                        new DialogRemark(JournalAddActivity.this, _remarkString);
                    }
                    break;
                case R.id.iv_photo_add:
                    if (mPicPath != null && mPicPath.endsWith("jpg")) {
                        Intent _intent = new Intent(Intent.ACTION_VIEW);
                        Uri uri = Uri.parse("file://" + mPicPath);
                        _intent.setDataAndType(uri, "image/*");
                        startActivity(_intent);
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
            Button _button = (Button) v;
            String _amount = tv.getText().toString().trim();
            if (v.getId() != R.id.btn_num_delete && _amount.length() > 9) {
                ToastUtils.showMsg(context, "你有这么多钱吗？");
                return;
            }
            if (v.getId() != R.id.btn_num_delete) {
                if (_amount.equals("0.00")) {
                    //第一次输入时
                    if (!_button.getText().equals(".") && !_button.getText().equals("0")) {
                        tv.setText(_button.getText());
                    }
                } else {
                    if (_amount.contains(".")) {
                        //金额中已经包含小数点
                        if (_button.getText().equals(".")) {// 输入的为小数点
                            ToastUtils.showMsg(context, "没学过数学呀？");
                            return;
                        }
                        //小数点后超过两位时
                        if ((_amount.length() - _amount.indexOf(".")) <= 2) {
                            tv.append(_button.getText());
                        } else {
                            ToastUtils.showMsg(context, "你有那么多零钱吗？");
                        }
                    } else {
                        tv.append(_button.getText());
                    }
                }
            } else {
                // 如果是删除键
                if (!_amount.equals("0.00")) {
                    if (_amount.length() > 1) {
                        String _str = _amount.substring(0, _amount.length() - 1);
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
