package com.ssdut.roysun.personalfinancialrecommendationsystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.adapter.JournalDetailAdapter;
import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.Expenditure;
import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.Income;
import com.ssdut.roysun.personalfinancialrecommendationsystem.service.DongHua3d;
import com.ssdut.roysun.personalfinancialrecommendationsystem.service.DongHuaYanChi;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.TimeUtils;

/**
 * Created by roysun on 16/3/12.
 * 账目明细页面
 */
public class JournalDetailActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    public static final String TAG = "JournalDetailActivity";

    //类别  支出和收入
    public static final int INCOME_FLAG = 4010;
    public static final int EXPENDITURE_FLAG = 4020;

    private RelativeLayout mBackArea, mNextArea, mIncomeTabArea, mExpenditureTabArea;  //顶部时间的上月和下月,支出收入按钮
    private LinearLayout mSumArea, mDetailListArea;  //底部汇总界面,列表界面
    private ListView mDetailList;  //list列表
    private TextView mCurrentMonth, mSum, mExpenditureAmount, mIncomeAmount, mAmountLeft;  //月份,汇总
    private int mYear, mMonth;  //当前年月
    private String mTimeNow;  //当前时间
    private int mFlag = 0;  //当前类别
    public MessageHandler mMsgHandler;
    private JournalDetailAdapter mJournalDetailAdapter;  //适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_detail);
        initData();
        initView();
    }

    @Override
    protected void initData() {
        super.initData();
        mMsgHandler = new MessageHandler();
        mJournalDetailAdapter = new JournalDetailAdapter(this);
    }

    @Override
    protected void initView() {
        mBackArea = (RelativeLayout) this.findViewById(R.id.rl_detail_back_to_last_month);
        mBackArea.setOnClickListener(this);
        mNextArea = (RelativeLayout) this.findViewById(R.id.rl_detail_move_to_next_month);
        mNextArea.setOnClickListener(this);
        mIncomeTabArea = (RelativeLayout) this.findViewById(R.id.rl_expenditure_tab);
        mIncomeTabArea.setOnClickListener(this);
        mExpenditureTabArea = (RelativeLayout) this.findViewById(R.id.rl_income_tab);
        mExpenditureTabArea.setOnClickListener(this);
        mSumArea = (LinearLayout) this.findViewById(R.id.ll_summerize_area);
        mSumArea.setVisibility(View.GONE);
        mDetailListArea = (LinearLayout) this.findViewById(R.id.ll_detail_list_area);
        mCurrentMonth = (TextView) this.findViewById(R.id.rl_detail_current_month);
        mSum = (TextView) this.findViewById(R.id.btn_summerize);
        mSum.setOnClickListener(this);
        mExpenditureAmount = (TextView) this.findViewById(R.id.tv_expenditure_this_month);
        mIncomeAmount = (TextView) this.findViewById(R.id.tv_income_this_month);
        mAmountLeft = (TextView) this.findViewById(R.id.tv_amount_left);
        mYear = TimeUtils.getYear();
        mMonth = TimeUtils.getMonth();
        if (mMonth < 10) {
            mTimeNow = mYear + "年0" + mMonth + "月";
        } else {
            mTimeNow = mYear + "年" + mMonth + "月";
        }
        mCurrentMonth.setText(mTimeNow);
        mDetailList = (ListView) findViewById(R.id.lv_detail_list);
        mFlag = EXPENDITURE_FLAG;  //默认先显示本月支出
        getSummerization();
        mDetailList.setAdapter(mJournalDetailAdapter);
        mDetailList.setOnItemClickListener(this);
        mDetailList.setLayoutAnimation(DongHua3d.listDongHua());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_detail_back_to_last_month:
                backMonth(mFlag);
                break;
            case R.id.rl_detail_move_to_next_month:
                nextMonth(mFlag);
                break;
            case R.id.rl_expenditure_tab:
                mFlag = EXPENDITURE_FLAG;
                yanChiShow(mDetailListArea);
                break;
            case R.id.rl_income_tab:
                mFlag = INCOME_FLAG;
                yanChiShow(mDetailListArea);
                break;
            case R.id.btn_summerize:
                if (mSumArea.isShown()) {
                    DongHuaYanChi.dongHuaEnd(mSumArea, JournalDetailActivity.this, mMsgHandler, R.anim.jz_menu_down, 300);
                } else {
                    mSumArea.setAnimation(AnimationUtils.loadAnimation(JournalDetailActivity.this, R.anim.jz_menu_up));
                    mSumArea.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    /*
     * 获取当前月汇总
     * */
    public void getSummerization() {
        double _count[] = mJournalDetailAdapter.getList(mYear, mMonth, 0, mFlag);
        mExpenditureAmount.setText(_count[0] + "");
        mIncomeAmount.setText(_count[1] + "");
        mAmountLeft.setText((_count[1] - _count[0]) + "");
        mJournalDetailAdapter.notifyDataSetChanged();
    }

    /*
     * 下一个月方法
     * */
    public void nextMonth(int flag) {
        mMonth++;
        if (mMonth > 12) {
            mMonth = 1;
            mYear++;
        }
        if (mMonth < 10) {
            mTimeNow = mYear + "年0" + mMonth + "月";
        } else {
            mTimeNow = mYear + "年" + mMonth + "月";
        }
        mCurrentMonth.setText(mTimeNow);
        yanChiShow(mDetailListArea);
    }

    /*
     * 上一个月方法
     * */
    public void backMonth(int flag) {
        mMonth--;
        if (mMonth < 1) {
            mMonth = 12;
            mYear--;
        }
        if (mMonth < 10) {
            mTimeNow = mYear + "年0" + mMonth + "月";
        } else {
            mTimeNow = mYear + "年" + mMonth + "月";
        }
        mCurrentMonth.setText(mTimeNow);
        yanChiShow(mDetailListArea);
    }

    /*
     * 切换动画，每次切换执行
     * */
    public void yanChiShow(final View v) {
        //顶部支出和收入按下后的背景
        if (mFlag == EXPENDITURE_FLAG) {
            mIncomeTabArea.setBackgroundResource(R.drawable.jz_bt_bg_s);
            mExpenditureTabArea.setBackgroundResource(R.drawable.blank);
        } else if (mFlag == INCOME_FLAG) {
            mExpenditureTabArea.setBackgroundResource(R.drawable.jz_bt_bg_s);
            mIncomeTabArea.setBackgroundResource(R.drawable.blank);
        }
        new Thread() {
            public void run() {
                try {
                    mMsgHandler.post(new Runnable() {
                        public void run() {
                            DongHua3d.applyRotation(v, 0, 180, 0);
                        }
                    });
                    sleep(400);
                    mMsgHandler.post(new Runnable() {
                        public void run() {
                            //下面方法封装getSummerization()为一个线程，否则直接放到这里会出现动画中丢帧现象
                            summerizationThread();
                            DongHua3d.applyRotation(v, 0, 180, 1);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    //下面方法封装getSummerization()为一个线程，否则直接放到这里会出现动画中丢帧现象
    public void summerizationThread() {
        new Thread() {
            public void run() {
                mMsgHandler.post(new Runnable() {
                    public void run() {
                        getSummerization();
                    }
                });
            }
        }.start();
    }

    @Override
    protected void onResume() {
        getSummerization();
        overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
        super.onResume();
    }

    public boolean onKeyDown(int kCode, KeyEvent kEvent) {
        switch (kCode) {
            case KeyEvent.KEYCODE_BACK: {
                finish();
                return false;
            }
        }
        return super.onKeyDown(kCode, kEvent);
    }

    public class MessageHandler extends Handler {
        public void handleMessage(Message msg) {
        }
    }

    /**
     * 点击明细页面任意条目则进入该条目编辑页面，即JournalAddActivity，不过在intent中传递参数“update”
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent _intent = new Intent(this, JournalAddActivity.class);
        if (mFlag == EXPENDITURE_FLAG) {
            Expenditure _expenditure = (Expenditure) view.getTag();
            _intent.putExtra("update", true);
            _intent.putExtra("type", JournalAddActivity.EXPENDITURE);
            _intent.putExtra("id", _expenditure.getId());
            startActivity(_intent);
        } else if (mFlag == INCOME_FLAG) {
            Income _income = (Income) view.getTag();
            _intent.putExtra("update", true);
            _intent.putExtra("type", JournalAddActivity.INCOME);
            _intent.putExtra("id", _income.getId());
            startActivity(_intent);
        }
    }

}
