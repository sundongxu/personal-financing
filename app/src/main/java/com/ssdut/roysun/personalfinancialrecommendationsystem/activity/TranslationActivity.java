package com.ssdut.roysun.personalfinancialrecommendationsystem.activity;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.listener.SnackbarClickListener;
import com.ssdut.roysun.personalfinancialrecommendationsystem.network.binder.TranslationBinder;
import com.ssdut.roysun.personalfinancialrecommendationsystem.network.service.TranslationService;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.NetworkUtils;

/**
 * Created by roysun on 16/3/12.
 * 翻译页面，调用了有道翻译API，含网络操作
 */
public class TranslationActivity extends BaseActivity implements View.OnClickListener, ServiceConnection {

    public static final String TAG = "TranslationActivity";

    public MessageHandler mMsgHandler;  //Handler用来后台更新界面
    private TranslationBinder mBinder;

    private TextView mResultText;  //TextView显示翻译内容
    private Button mBtnTranslate;  //按钮：翻译
    private EditText mContentTranslated;  //翻译内容的编辑框
    private ProgressDialog mProgressDialog;  //搜索时的显示进度
    private LinearLayout mTranslationView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translation);
        initData();
        initView();
    }

    @Override
    protected void initData() {
        super.initData();
        mContext = this;
        mMsgHandler = new MessageHandler();
    }

    @Override
    protected void initView() {
        super.initView();
        if (mToolbar != null) {
            mToolbar.setTitle(R.string.title_translation_page);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mBtnTranslate = (Button) this.findViewById(R.id.btn_translate);
        mBtnTranslate.setOnClickListener(this);
        mResultText = (TextView) findViewById(R.id.tv_translation_result);
        mContentTranslated = (EditText) findViewById(R.id.et_content_translated);
        mContentTranslated.getBackground().setAlpha(200);
        mTranslationView = (LinearLayout) this.findViewById(R.id.ll_translation);
        mTranslationView.getBackground().setAlpha(150);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_translate:
                //翻译按钮
                mInputMethodManager.hideSoftInputFromWindow(mContentTranslated.getWindowToken(), 0);  //隐藏软键盘
                showProgress();  //显示进度
                //启动后台服务
                Intent intent = new Intent(this, TranslationService.class);
                bindService(intent, this, Context.BIND_AUTO_CREATE);  //注意获取结果后即要解绑
                break;
        }
    }

    private void showProgress() {
        mProgressDialog = ProgressDialog.show(this, "", "正在获取翻译，请稍后...", true, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 200, 0, "清除内容");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 200:
                mContentTranslated.setText("");
                mResultText.setText("");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        // 服务绑定成功时执行
        Log.v("TranslationService", "onServiceConnected !!!");
        mBinder = (TranslationBinder) service;
        mBinder.mContext = TranslationActivity.this;
        String strContent = mContentTranslated.getText().toString().trim().length() != 0 ? mContentTranslated.getText().toString().trim() : "";
        mBinder.getResult(strContent);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        // 服务所在进程崩溃或者被杀死时执行
        Log.v("TranslationService", "onServiceDisconnected !!!");
    }

    public class MessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NetworkUtils.ACCESS_SUCCESS:
                    Log.v(TAG, "msg.obj=" + msg.obj);
                    mResultText.setText((String) msg.obj);
                    Snackbar.make(mToolbar, R.string.translation_access_success, Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
                    unbindService(TranslationActivity.this);
                    break;
                case NetworkUtils.ACCESS_FAIL:
                    Snackbar.make(mToolbar, R.string.translation_access_failure, Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
                    unbindService(TranslationActivity.this);
                    break;
                case NetworkUtils.NO_INTERNET:
                    Snackbar.make(mToolbar, R.string.translation_no_internet, Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
                    unbindService(TranslationActivity.this);
                    break;
            }
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.cancel();
            }
            super.handleMessage(msg);
        }
    }

}
