package com.ssdut.roysun.personalfinancialrecommendationsystem.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.adapter.info.AppInfoListAdapter;
import com.ssdut.roysun.personalfinancialrecommendationsystem.adapter.info.InfoListBaseAdapter;
import com.ssdut.roysun.personalfinancialrecommendationsystem.component.DividerItemDecoration;

public class AppInfoActivity extends BaseActivity {

    public final String TAG = "AppInfoActivity";

    private RecyclerView mAppInfoList;
    private AppInfoListAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);
        initData();
        initView();
    }

    @Override
    protected void initData() {
        super.initData();
        mContext = this;
    }

    @Override
    protected void initView() {
        super.initView();
        if (mToolbar != null) {
            mToolbar.setTitle(R.string.info_app);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mAppInfoList = (RecyclerView) findViewById(R.id.rv_app_info_list);
        mAppInfoList.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mAppInfoList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mAppInfoList.setItemAnimator(new DefaultItemAnimator());
        mAppInfoList.setLayoutManager(mLayoutManager);
        mAdapter = new AppInfoListAdapter(this);
        mAdapter.setOnItemClickListener(new AppInfoListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int itemType) {
                switch (itemType) {
                    case AppInfoListAdapter.INFO_APP_INTRODUCTION:
                        Uri uri = Uri.parse(getString(R.string.readme));   //指定网址
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);           //指定Action
                        intent.setData(uri);                            //设置Uri
                        mContext.startActivity(intent);        //启动Activity
                        break;
                    case InfoListBaseAdapter.INFO_APP_VERSION:
                        new AlertDialog.Builder(mContext).setTitle("随心记的完成离不开开源项目的支持，向以下致谢：")
                                .setMessage("Google Support Design, " + "\n" +
                                        "Gson, Volley, " +
                                        "ahbottomnavigation, ObservableScrollView...")
                                .setPositiveButton("关闭", null)
                                .show();
                        break;
                    case AppInfoListAdapter.INFO_APP_RECOMMENDATION:
                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
                        sharingIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_txt));
                        startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_app)));
                        break;
                    case AppInfoListAdapter.INFO_APP_STAR:
                        new AlertDialog.Builder(mContext).setTitle("点赞")
                                .setMessage("去项目地址给作者个Star，鼓励下作者୧(๑•̀⌄•́๑)૭✧")
                                .setNegativeButton("复制", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        copyToClipboard(mToolbar, mContext.getResources().getString(R.string.app_html));
                                    }
                                })
                                .setPositiveButton("打开", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Uri uri = Uri.parse(getString(R.string.app_html));   //指定网址
                                        Intent intent = new Intent();
                                        intent.setAction(Intent.ACTION_VIEW);           //指定Action
                                        intent.setData(uri);                            //设置Uri
                                        mContext.startActivity(intent);        //启动Activity
                                    }
                                })
                                .show();
                        break;
                    case AppInfoListAdapter.INFO_APP_ENCOURAGE:
                        new AlertDialog.Builder(mContext).setTitle("请作者喝杯咖啡？(〃ω〃)")
                                .setMessage("点击按钮后，作者支付宝账号将会复制到剪切板，" + "你就可以使用支付宝转账给作者啦( •̀ .̫ •́ )✧")
                                .setPositiveButton("好叻", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        copyToClipboard(mToolbar, mContext.getResources().getString(R.string.alipay));
                                    }
                                })
                                .show();
                        break;
                    case AppInfoListAdapter.INFO_APP_BLOG:
                        copyToClipboard(mToolbar, mContext.getResources().getString(R.string.blog));
                        break;
                    case AppInfoListAdapter.INFO_APP_GITHUB:
                        copyToClipboard(mToolbar, mContext.getResources().getString(R.string.github));
                        break;
                    case AppInfoListAdapter.INFO_APP_EMAIL:
                        copyToClipboard(mToolbar, mContext.getResources().getString(R.string.email));
                        break;
                }
            }
        });
        mAppInfoList.setAdapter(mAdapter);
//        getFragmentManager().beginTransaction().replace(R.id.fl_app_info_area, new AboutFragment()).commit();
    }

    //复制黏贴板
    private void copyToClipboard(View view, String info) {
        ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("msg", info);
        manager.setPrimaryClip(clipData);
        Snackbar.make(view, "已经复制到剪切板啦( •̀ .̫ •́ )✧", Snackbar.LENGTH_SHORT).show();
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
}
