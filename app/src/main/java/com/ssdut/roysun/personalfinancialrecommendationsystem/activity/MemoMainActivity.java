package com.ssdut.roysun.personalfinancialrecommendationsystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.adapter.MemoListAdapter;
import com.ssdut.roysun.personalfinancialrecommendationsystem.db.manager.MemoManager;
import com.ssdut.roysun.personalfinancialrecommendationsystem.dialog.MemoSetPasswordDialog;
import com.ssdut.roysun.personalfinancialrecommendationsystem.service.DongHua3d;
import com.ssdut.roysun.personalfinancialrecommendationsystem.service.DongHuaYanChi;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.DialogUtils;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.ToastUtils;

/**
 * Created by roysun on 16/3/12.
 * 备忘，menu三个按钮
 */
public class MemoMainActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ListView mMemoList;  // 主界面list
    private ImageButton mIBAddNewMemo;  // 添加新备忘按钮
    private MemoListAdapter mMemoListAdapter;  // 主界面list适配器
    private LinearLayout mMenu;  // 主界面底部多选删除界面
    private Button mBtnSelectAll, mBtnDelete, mBtnCancelSelect;  // 多选、删除、取消选择三个按钮，包含在mMenu(linearLayout)

    private Handler handler;
    private MemoManager mMemoManager;  // 创建数据库对象

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_main);
        initData();
        initView();
    }

    @Override
    protected void initData() {
        super.initData();
        mMemoListAdapter = new MemoListAdapter(this);
        handler = new Handler();
        mMemoManager = new MemoManager(this);
    }

    @Override
    protected void initView() {
        super.initView();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null){
            mToolbar.setTitle("备忘");
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mMenu = (LinearLayout) this.findViewById(R.id.ll_menu);
        mMenu.setVisibility(View.VISIBLE);  //初始不可见，在onMenuItemSelected方法触发后可见（即长按menu键）
        mMemoList = (ListView) this.findViewById(R.id.lv_memo);
        mIBAddNewMemo = (ImageButton) this.findViewById(R.id.ib_add);
        mIBAddNewMemo.setOnClickListener(this);
        mMemoList.setAdapter(mMemoListAdapter);
        mMemoList.setOnItemClickListener(this);
        initMenuEvent();
    }

    private void initMenuEvent() {
        mBtnSelectAll = (Button) this.findViewById(R.id.btn_selectall);
        mBtnSelectAll.setOnClickListener(this);
        mBtnDelete = (Button) this.findViewById(R.id.btn_delete);
        mBtnDelete.setOnClickListener(this);
        mBtnCancelSelect = (Button) this.findViewById(R.id.btn_cancelselect);
        mBtnCancelSelect.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        mMemoListAdapter.initList();  //adapte构造函数中已经getList一次了，这里重复调用导致数据库两次遍历该memo表
        mMemoListAdapter.notifyDataSetChanged();
        overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
        mMemoList.setLayoutAnimation(DongHua3d.listDongHua());
        super.onResume();
    }

    /**
     * 点击备忘ListView中的任意条目，相当于进入新建备忘Activity（可编辑）
     *
     * @param parent
     * @param view
     * @param position
     * @param id       备忘录item的id，随intent传入MemoNewActivity，然后加载该id对应的备忘录条目内容
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // 获取点击的Item的Tag（Tag设置为id）
        int content_id = (Integer) view.getTag();
        Intent intent = new Intent(this, MemoAddActivity.class);
        intent.putExtra("id", content_id);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_add:
                Intent intent = new Intent(this, MemoAddActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                break;
            case R.id.btn_selectall:
                mMemoListAdapter.isSelectAll = true;
                mMemoListAdapter.notifyDataSetChanged();  //通知观察者（checkbox）更新视图
                System.out.println("sdx---notifydatachanged 1 invoked!!!");
                break;
            case R.id.btn_delete:
                deleteMemo();
                break;
            case R.id.btn_cancelselect:
                mMemoListAdapter.isSelectAll = false;
                mMemoListAdapter.notifyDataSetChanged();  //通知观察者（checkbox）更新视图
                System.out.println("sdx---notifydatachanged 2 invoked!!!");
                break;
        }
    }

    private void deleteMemo() {
        if (mMemoListAdapter.mIdList != null && mMemoListAdapter.mIdList.size() > 0) {
            for (int id : mMemoListAdapter.mIdList) {
                mMemoManager.deleteMemoInfo(id);
            }
            mMemoListAdapter.initList();
            mMemoListAdapter.isShowCheck = false;  //每一次多选框标志位置位操作之后才能执行notifyDataSetChanged() -> getView()更新视图
            mMemoListAdapter.isSelectAll = false;
            mMemoListAdapter.notifyDataSetChanged();
            DongHuaYanChi.dongHuaEnd(mMenu, MemoMainActivity.this, handler, R.anim.jz_menu_down, 300);
        } else {
            ToastUtils.showMsg(this, "你还没有选择要删除项");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: {
                if (mMenu.isShown()) {
                    DongHuaYanChi.dongHuaEnd(mMenu, MemoMainActivity.this, handler, R.anim.jz_menu_down, 300);
                    mMemoListAdapter.isShowCheck = false;
                    mMemoListAdapter.notifyDataSetChanged();
                } else {
                    DialogUtils.showExitDialog(this, ACTIVITY_MEMO_MAIN);
                }
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 100, 0, "导出所有");
        menu.add(0, 200, 0, "选择删除");
        menu.add(0, 300, 0, "设置密码");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 100:
                //导出
                if (MemoListAdapter.sMemoList != null && MemoListAdapter.sMemoList.size() > 0) {
                    DialogUtils.showExportDialog(this);
                } else {
                    ToastUtils.showMsg(this, "没有可导出条目");
                }
                break;
            case 200:
                //选择删除
                if (!mMenu.isShown()) {
                    if (MemoListAdapter.sMemoList != null && MemoListAdapter.sMemoList.size() > 0) {
                        mMenu.setVisibility(View.VISIBLE);
                        mMenu.setAnimation(AnimationUtils.loadAnimation(this, R.anim.jz_menu_up));
                        mMemoListAdapter.isShowCheck = true;  //显示多选框
                        mMemoListAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtils.showMsg(this, "没有可选条目");
                    }
                } else {
                    DongHuaYanChi.dongHuaEnd(mMenu, MemoMainActivity.this, handler, R.anim.jz_menu_down, 300);
                    mMemoListAdapter.isShowCheck = false;
                    mMemoListAdapter.notifyDataSetChanged();
                }
                break;
            case 300:
                //设置密码
                new MemoSetPasswordDialog(this);
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
     * 点击图片以后的动画效果
     */
    public void dongHua(View v) {
        v.setAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
    }

}
