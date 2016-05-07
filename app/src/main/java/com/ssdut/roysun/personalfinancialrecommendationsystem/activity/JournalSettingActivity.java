package com.ssdut.roysun.personalfinancialrecommendationsystem.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.db.manager.JournalManager;
import com.ssdut.roysun.personalfinancialrecommendationsystem.db.JournalSqliteHelper;
import com.ssdut.roysun.personalfinancialrecommendationsystem.dialog.DialogAbout;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.ToastUtils;

/**
 * Created by roysun on 16/3/12.
 * 记账设置页面
 */
public class JournalSettingActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = "JournalSettingActivity";

    public static final String JOURNAL_PASSWORD = "JOURNAL_PASSWORD";

    private EditText mPsdInput, mPsdRepeat;  //密码编辑框
    private TextView mTitle;  //设置密码标题
    private Button mBtnSave, mBtnCancel, mBtnDelete;  //密码编辑界面的保存，取消，删除按钮
    private RelativeLayout mPsdTitleArea, qingchu_rl, guanyu_rl;  //设置选项  加密，清除数据，关于软件
    private LinearLayout mPsdSetArea;  //编辑加密界面
    private int mPsd = 0, mPsdTemp = -1;  //mPsd 当前密码  ，  tempmi密码标识
    private JournalManager mJournalDataHelper;  //记账数据库管理

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_setting);
        initData();
        initView();
    }

    @Override
    protected void initData() {
        super.initData();
        mJournalDataHelper = new JournalManager(this);
    }

    @Override
    protected void initView() {
        mPsdTitleArea = (RelativeLayout) this.findViewById(R.id.rl_psd_title);
        mPsdTitleArea.setOnClickListener(this);
        qingchu_rl = (RelativeLayout) this.findViewById(R.id.rl_delete_all);
        qingchu_rl.setOnClickListener(this);
        guanyu_rl = (RelativeLayout) this.findViewById(R.id.rl_about_software);
        guanyu_rl.setOnClickListener(this);
        mPsdSetArea = (LinearLayout) this.findViewById(R.id.ll_psd_set_area);
        mPsdSetArea.setVisibility(View.GONE);

        mPsdInput = (EditText) this.findViewById(R.id.et_new_psd);
        mPsdRepeat = (EditText) this.findViewById(R.id.et_psd_repeat);
        mBtnSave = (Button) this.findViewById(R.id.btn_psd_confirm);
        mBtnSave.setOnClickListener(this);
        mBtnCancel = (Button) this.findViewById(R.id.btn_cancel);
        mBtnCancel.setOnClickListener(this);
        mBtnDelete = (Button) this.findViewById(R.id.btn_psd_delete);
        mBtnDelete.setOnClickListener(this);
        mBtnDelete.setVisibility(View.INVISIBLE);

        mTitle = (TextView) this.findViewById(R.id.tv_psd_title);
        mPsd = JournalSqliteHelper.readPreferenceFile(this, JOURNAL_PASSWORD, JOURNAL_PASSWORD);
        if (mPsd == 0) {
            //密码为0则当前没密码
            mTitle.setText("程序加密");
        } else {
            mTitle.setText("修改或删除密码");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_psd_title:
                //程序加密选项，打开后再点击可收回（抽屉式）
                mPsd = JournalSqliteHelper.readPreferenceFile(this, JOURNAL_PASSWORD, JOURNAL_PASSWORD);
                if (mPsdSetArea.isShown()) {
                    mPsdSetArea.setVisibility(View.GONE);
                } else {
                    mPsdSetArea.setVisibility(View.VISIBLE);
                    if (mPsd == 0) {
                        //当没有密码时
                        mTitle.setText("程序加密");
                        mBtnDelete.setVisibility(View.INVISIBLE);
                    } else {
                        mPsdTemp = mPsd;
                        //当有密码时把密码赋值给temp，以便于点击确定按钮时正确判断
                        mTitle.setText("修改或删除密码");
                        mPsdInput.setHint("请输入旧密码");
                        mPsdRepeat.setVisibility(View.GONE);  //密码确认框不可见
                        mBtnDelete.setVisibility(View.INVISIBLE);  //删除密码按钮不可见
                    }
                }
                break;
            case R.id.rl_delete_all:
                showDialog();
                break;
            case R.id.rl_about_software:
                String text = "该记账器功能比较简单，使用方便。" +
                        "\r\n欢迎大家提出宝贵意见。" +
                        "\r\nQQ：371211947" +
                        "\r\n大连理工大学软件学院2016年毕业设计" +
                        "\r\n                      软件版本 v1.0" +
                        "\r\n                东东出品，必属精品！";
                new DialogAbout(this, text);
                break;
            case R.id.btn_psd_confirm:
                if (mPsd == 0 || mPsdTemp == -1) {
                    //当没有密码时
                    if (mPsdInput.getText().toString().length() == 0 || mPsdRepeat.getText().toString().length() == 0) {
                        ToastUtils.showMsg(this, "输入不能为空");
                        return;
                    }
                    if (mPsdInput.getText().toString().length() != 6 || mPsdRepeat.getText().toString().length() != 6) {
                        ToastUtils.showMsg(this, "密码必须为六位数");
                        return;
                    }
                    if (!mPsdInput.getText().toString().equals(mPsdRepeat.getText().toString())) {
                        ToastUtils.showMsg(this, "两次输入的密码不匹配");
                        return;
                    }
                    mPsd = Integer.parseInt(mPsdRepeat.getText().toString());
                    mPsdTemp = mPsd;
                    JournalSqliteHelper.saveBudget(this, JOURNAL_PASSWORD, JOURNAL_PASSWORD, mPsd);
                    mTitle.setText("修改或删除密码");
                    mPsdSetArea.setVisibility(View.GONE);
                    ToastUtils.showMsg(this, "加密成功");
                    mPsdInput.setText("");
                    mPsdRepeat.setText("");
                } else {
                    if (mPsdInput.getText().toString().length() == 0) {
                        ToastUtils.showMsg(this, "输入不能为空");
                        return;
                    }
                    mPsdTemp = Integer.parseInt(mPsdInput.getText().toString());
                    if (mPsd == mPsdTemp) {
                        //当密码输入正确时
                        mPsdTemp = -1;  //赋值为-1，修改密码后可以进入if(mPsd==0||mPsdTemp==-1)代码块
                        mBtnDelete.setVisibility(View.VISIBLE);
                        mPsdInput.setText("");
                        mPsdRepeat.setText("");
                        mPsdInput.setHint("请输入新密码（六位）");
                        mPsdRepeat.setVisibility(View.VISIBLE);
                    } else {
                        ToastUtils.showMsg(this, "与旧密码不匹配");
                    }
                }
                break;
            case R.id.btn_psd_delete:
                JournalSqliteHelper.saveBudget(this, JOURNAL_PASSWORD, JOURNAL_PASSWORD, 0);
                mTitle.setText("程序加密");
                mPsdSetArea.setVisibility(View.GONE);
                mBtnDelete.setVisibility(View.INVISIBLE);
                mPsd = 0;
                mPsdInput.setText("");
                mPsdRepeat.setText("");
                ToastUtils.showMsg(this, "删除成功");
                break;
            case R.id.btn_cancel:
                mPsdInput.setText("");
                mPsdRepeat.setText("");
                mPsdTemp = -1;
                mPsdSetArea.setVisibility(View.GONE);
                break;
        }
    }

    public void showDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("是否确认清除所有数据？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mJournalDataHelper.delExpenditureAndIncomeAll();
                ToastUtils.showMsg(JournalSettingActivity.this, "数据清除完成");
            }
        });
        builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }

    @Override
    protected void onResume() {
        overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
        super.onResume();
    }

}
