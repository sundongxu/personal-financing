package com.ssdut.roysun.personalfinancialrecommendationsystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.adapter.journal.JournalDetailAdapter;
import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.Expenditure;
import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.Income;
import com.ssdut.roysun.personalfinancialrecommendationsystem.component.anim.Animation3D;
import com.ssdut.roysun.personalfinancialrecommendationsystem.component.anim.AnimationDelay;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.TimeUtils;

/**
 * Created by roysun on 16/3/12.
 * 账目明细页面
 */
public class JournalDetailActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    public static final String TAG = "JournalDetailActivity";

    //类别 支出和收入
    public static final int INCOME_FLAG = 4010;
    public static final int EXPENDITURE_FLAG = 4020;

    public MessageHandler mMsgHandler;
    private RelativeLayout mBackArea, mNextArea, mIncomeTabArea, mExpenditureTabArea;  // 顶部时间的上月和下月,支出收入按钮
    private LinearLayout mSumArea, mDetailListArea;  // 底部汇总界面,列表界面
    private ListView mDetailList;  // list列表
    private TextView mCurrentMonth, mExpenditureAmount, mIncomeAmount, mAmountLeft;  // 月份,汇总
    private int mYear, mMonth;  // 当前年月
    private String mTimeNow;  // 当前时间
    private int mFlag = 0;  // 当前类别

    private JournalDetailAdapter mJournalDetailAdapter;  // 适配器

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
        mContext = this;
        mMsgHandler = new MessageHandler();
        mJournalDetailAdapter = new JournalDetailAdapter(this);
    }

    @Override
    protected void initView() {
        super.initView();
        if (mToolbar != null) {
            mToolbar.setTitle(R.string.title_journal_detail_page);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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
        mDetailList.setLayoutAnimation(Animation3D.listDongHua());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_detail_back_to_last_month:
                lastMonth(mFlag);
                break;
            case R.id.rl_detail_move_to_next_month:
                nextMonth(mFlag);
                break;
            case R.id.rl_expenditure_tab:
                mFlag = EXPENDITURE_FLAG;
                delayShow(mDetailListArea);
                break;
            case R.id.rl_income_tab:
                mFlag = INCOME_FLAG;
                delayShow(mDetailListArea);
                break;
        }
    }

    // 获取月汇总
    public void getSummerization() {
        double count[] = mJournalDetailAdapter.getList(mYear, mMonth, 0, mFlag);
        mExpenditureAmount.setText(count[0] + "");
        mIncomeAmount.setText(count[1] + "");
        mAmountLeft.setText((count[1] - count[0]) + "");
        mJournalDetailAdapter.notifyDataSetChanged();
    }

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
        delayShow(mDetailListArea);
    }

    public void lastMonth(int flag) {
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
        delayShow(mDetailListArea);
    }

    // 切换动画，每次切换执行
    public void delayShow(final View v) {
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
                            Animation3D.applyRotation(v, 0, 180, 0);
                        }
                    });
                    sleep(400);
                    mMsgHandler.post(new Runnable() {
                        public void run() {
                            //下面方法封装getSummerization()为一个线程，否则直接放到这里会出现动画中丢帧现象
                            summerizationThread();
                            Animation3D.applyRotation(v, 0, 180, 1);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_journal_detail_summary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.journal_detail_summerize:
                if (mSumArea.isShown()) {
                    AnimationDelay.dongHuaEnd(mSumArea, JournalDetailActivity.this, mMsgHandler, R.anim.journal_main_menu_disappear, 300);
                } else {
                    mSumArea.setAnimation(AnimationUtils.loadAnimation(JournalDetailActivity.this, R.anim.journal_main_menu_appear));
                    mSumArea.setVisibility(View.VISIBLE);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
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
        Intent intent = new Intent(this, JournalAddActivity.class);
        if (mFlag == EXPENDITURE_FLAG) {
            Expenditure expenditure = (Expenditure) view.getTag();
            intent.putExtra("update", true);
            intent.putExtra("type", JournalAddActivity.EXPENDITURE);
            intent.putExtra("id", expenditure.getId());
            startActivity(intent);
        } else if (mFlag == INCOME_FLAG) {
            Income income = (Income) view.getTag();
            intent.putExtra("update", true);
            intent.putExtra("type", JournalAddActivity.INCOME);
            intent.putExtra("id", income.getId());
            startActivity(intent);
        }
    }

    public class MessageHandler extends Handler {
        public void handleMessage(Message msg) {

        }
    }

}
