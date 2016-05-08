package com.ssdut.roysun.personalfinancialrecommendationsystem.MD.activity;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.ssdut.roysun.personalfinancialrecommendationsystem.MD.activity.fragment.OthersFragmentMD;
import com.ssdut.roysun.personalfinancialrecommendationsystem.MD.adapter.DrawerMenuListAdapter;
import com.ssdut.roysun.personalfinancialrecommendationsystem.MD.adapter.SearchAdapter;
import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.AppInfoActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.BaseActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.CalculationActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.DeviceInfoActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.MemoMainActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.TranslationActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.WeatherActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.User;
import com.ssdut.roysun.personalfinancialrecommendationsystem.component.DividerItemDecoration;
import com.ssdut.roysun.personalfinancialrecommendationsystem.db.manager.UserManager;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.DialogUtils;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.SharedPreferenceUtils;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by roysun on 16/4/1
 * MD风格的主Activity，采用AHBottomNavigation底部导航栏，上滑时导航栏隐藏，下滑时出现
 * bug1：搜索框软键盘点击左上角返回键之后不能关闭软键盘
 * bug2：快速返回关闭Drawer后需返回两次才可以出现退出对话框
 * bug3：点击Drawer item跳转其它的Activity后出现RecycleView: No adapter attached
 */
public class MainActivityMD extends BaseActivity {

    public static final String TAG = "MainActivityMD";
    public final int DRAWER_OPEN = 0;
    public final int DRAWER_CLOSED = 1;
    private OthersFragmentMD mCurrentFragment;
    private ArrayList<AHBottomNavigationItem> mBottomNavigationItems = new ArrayList<>();
    private FragmentManager mFragmentManager = getFragmentManager();
    private AHBottomNavigation mBottomNavigation;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private boolean mCanCloseDrawer;  // 只要抽屉开始滑动了就可以返回键关闭
    private int mPreDrawerState;

    //侧边导航栏item，应该都在抽屉的适配器adapter中添加处理时间
    private CircleImageView mUserIconView;  // item0：侧边抽屉导航头像
    //        mUserIconView.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.main_bg));  //圆形CircleImageView动态设置icon
    private RecyclerView mDrawerList;  //包含侧边Drawer5个菜单项
    private DrawerMenuListAdapter mDrawerAdapter;

    private Dialog mDialog;
    private ArrayList<String> mThingList;

    private Context mContext;
    private InputMethodManager mInputMethodManager;
    private UserManager mUserManager;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_md);
        initData();
        initView();
    }

    @Override
    protected void initData() {
        super.initData();
        mContext = this;
        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mUserManager = UserManager.getInstance(getApplicationContext());  // 以ApplicationContext获得UserManager单例
        mCanCloseDrawer = false;
        mPreDrawerState = DRAWER_CLOSED;
    }

    @Override
    protected void initView() {
        super.initView();
        initToolBar();
        initDrawer();
        mBottomNavigation = (AHBottomNavigation) findViewById(R.id.ahbn_bottom_bar);
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.tab_1, R.drawable.icon_bottom_tab_1, R.color.color_tab_1);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.tab_2, R.drawable.icon_bottom_tab_2, R.color.color_tab_2);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.tab_3, R.drawable.icon_bottom_tab_3, R.color.color_tab_3);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.tab_4, R.drawable.icon_bottom_tab_4, R.color.color_tab_4);
        mBottomNavigationItems.add(item1);
        mBottomNavigationItems.add(item2);
        mBottomNavigationItems.add(item3);
        mBottomNavigationItems.add(item4);
        mBottomNavigation.addItems(mBottomNavigationItems);
        mBottomNavigation.setAccentColor(Color.parseColor("#03A9F4"));  //tab被选中后的颜色，红
        mBottomNavigation.setInactiveColor(Color.parseColor("#747474"));  //tab未选中时的颜色，灰
        mBottomNavigation.setNotificationBackgroundColor(Color.parseColor("#F63D2B"));
        mBottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, boolean wasSelected) {
                if (!wasSelected) {
                    mCurrentFragment = OthersFragmentMD.newInstance(position);
                    mFragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                            .replace(R.id.fl_container, mCurrentFragment)
                            .commit();
                } else if (position > 0) {
                    mCurrentFragment.refresh();
                }
            }
        });

        mCurrentFragment = OthersFragmentMD.newInstance(0);  //初始化显示第一个tab
        mFragmentManager.beginTransaction()
                .replace(R.id.fl_container, mCurrentFragment)
                .commit();
    }

    @Override
    protected void onPause() {
        closeDrawer();
        super.onPause();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                DialogUtils.showExitDialog(this, BaseActivity.ACTIVITY_MAIN_MD);
                break;
            case R.id.action_search:
                showSearchDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // Activity的启动模式为singleInstance，即一直在栈中有唯一实例的Activity被重新call起的时候触发
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.v(TAG, "onNewIntent() invoked!!!");
        if (mDialog != null) {
            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }
        }
    }

    private void initToolBar() {
        if (mToolbar != null) {
            mToolbar.setTitle("随心记");
            setSupportActionBar(mToolbar);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // 显示左上角toolbar的回退箭头
        }
    }

    private void initDrawer() {
        mDrawer = (DrawerLayout) findViewById(R.id.dl_menu_side);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.open_txt, R.string.close_txt) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mCanCloseDrawer = true;
                mPreDrawerState = DRAWER_OPEN;
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                mCanCloseDrawer = false;
                mPreDrawerState = DRAWER_CLOSED;
            }

            // 很重要，监听到抽屉开始滑动，此时就可以通过物理返回键关闭
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (mPreDrawerState == DRAWER_OPEN) {
                    mCanCloseDrawer = false;
                } else {
                    mCanCloseDrawer = true;
                }
            }
        };
        mDrawer.addDrawerListener(mActionBarDrawerToggle);
        mDrawerList = (RecyclerView) findViewById(R.id.rv_drawer_menu_list);
        mDrawerAdapter = new DrawerMenuListAdapter(this);
        mDrawerAdapter.setOnItemClickListener(new DrawerMenuListAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                switch (position) {
                    case 3:
                        startActivity(new Intent(mContext, DeviceInfoActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(mContext, AppInfoActivity.class));
                        break;
                    case 5:
                        DialogUtils.showExitDialog(mContext, BaseActivity.ACTIVITY_MAIN_MD);
                        break;
                }
            }
        });
        mDrawerList.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mDrawerList.setLayoutManager(mLayoutManager);
        mDrawerList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mDrawerList.setItemAnimator(new DefaultItemAnimator());
        mDrawerList.setAdapter(mDrawerAdapter);
        setDrawerListHeader(mDrawerList);

        User _user = mUserManager.getCurUser();
        if (_user != null) {
            Log.v(TAG, _user.getPic());
        }

    }

    public void setDrawerListHeader(RecyclerView recyclerView) {
        View _header = LayoutInflater.from(this).inflate(R.layout.drawer_header, recyclerView, false);
        mUserIconView = (CircleImageView) _header.findViewById(R.id.civ_menu_user_icon);
        //设置用户头像
        mUserIconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mUserManager.isSignIn()) {
                    startActivity(new Intent(mContext, LoginActivityMD.class));
//                    closeDrawer();
                }
            }
        });
        mDrawerAdapter.setHeaderView(_header);
    }

    public void closeDrawer() {
        if (mDrawer != null && mCanCloseDrawer) {
            mCanCloseDrawer = false;
            mPreDrawerState = DRAWER_CLOSED;
            mDrawer.closeDrawer(Gravity.LEFT);
        }
    }

    public void showSearchDialog() {
        ArrayList<String> _searchContent = SharedPreferenceUtils.loadList(mContext, Utils.PRE_RECORDS, Utils.SEARCH_CONTENT);
        View view = getLayoutInflater().inflate(R.layout.view_toolbar_search, null);
        ImageView _imgToolBack = (ImageView) view.findViewById(R.id.img_tool_back);
        final EditText _edtToolSearch = (EditText) view.findViewById(R.id.edt_tool_search);
        ImageView _imgToolSearch = (ImageView) view.findViewById(R.id.img_tool_mic);
        final ListView _listSearch = (ListView) view.findViewById(R.id.list_search);
        final TextView txtEmpty = (TextView) view.findViewById(R.id.txt_empty);
        Utils.setListViewHeightBasedOnChildren(_listSearch);
        _edtToolSearch.setHint("你想要做什么");

        mDialog = new Dialog(mContext, R.style.MaterialSearch);
        mDialog.setContentView(view);
//        mDialog.setCancelable(false);  // 设置这里可让mDialog按下返回键也不消失
        mDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mDialog.getWindow().setGravity(Gravity.BOTTOM);
        mDialog.show();
        mDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);  // 搜索dialog一出来输入框就显示软键盘，去掉这句要点击输入框才出现
        _searchContent = (_searchContent != null && _searchContent.size() > 0) ? _searchContent : new ArrayList<String>();
        final SearchAdapter _searchAdapter = new SearchAdapter(mContext, _searchContent, false);
        _listSearch.setVisibility(View.VISIBLE);
        _listSearch.setAdapter(_searchAdapter);
        _listSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String _strInput = String.valueOf(adapterView.getItemAtPosition(position));
                if (_strInput.equals("删除历史记录")) {
                    SharedPreferenceUtils.deleteList(mContext, Utils.PRE_RECORDS);
                    _listSearch.setVisibility(View.GONE);
                } else {
                    SharedPreferenceUtils.addList(mContext, Utils.PRE_RECORDS, Utils.SEARCH_CONTENT, _strInput);
                    _edtToolSearch.setText(_strInput);
                    _listSearch.setVisibility(View.GONE);
                }
            }
        });
        _edtToolSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String[] _fun = mContext.getResources().getStringArray(R.array.fun_array);
                mThingList = new ArrayList<String>(Arrays.asList(_fun));
                _listSearch.setVisibility(View.VISIBLE);
                _searchAdapter.updateList(mThingList, true);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<String> filterList = new ArrayList<String>();
                boolean isNodata = false;
                if (s.length() > 0) {
                    for (int i = 0; i < mThingList.size(); i++) {
                        if (mThingList.get(i).toLowerCase().startsWith(s.toString().trim().toLowerCase())) {
                            filterList.add(mThingList.get(i));
                            _listSearch.setVisibility(View.VISIBLE);
                            _searchAdapter.updateList(filterList, true);
                            isNodata = true;
                        }
                    }
                    if (!isNodata) {
                        _listSearch.setVisibility(View.GONE);
                        txtEmpty.setVisibility(View.VISIBLE);
                        txtEmpty.setText("没有找到");
                    }
                } else {
                    _listSearch.setVisibility(View.GONE);
                    txtEmpty.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        _imgToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDialog != null) {
                    mDialog.dismiss();
                }
//                mInputMethodManager.hideSoftInputFromWindow(edtToolSearch.getWindowToken(), 0);  //为什么无效？？？
            }
        });

        _imgToolSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //可以实现搜索跳转对应Activity功能
//                edtToolSearch.setText("");
//                SharedPreferenceUtils.deleteList(MainActivity.this, Utils.PRE_RECORDS); //清除历史
                String _strInput = String.valueOf(_edtToolSearch.getText());
                if (!_strInput.equals("")) {
                    //输入框非空才跳转
                    if (mThingList.contains(_strInput)) {
                        SharedPreferenceUtils.addList(mContext, Utils.PRE_RECORDS, Utils.SEARCH_CONTENT, _strInput);
                    }
                    switch (_strInput) {
                        case "备忘":
                            startActivity(new Intent(mContext, MemoMainActivity.class));
                            break;
                        case "天气":
                            startActivity(new Intent(mContext, WeatherActivity.class));
                            break;
                        case "翻译":
                            startActivity(new Intent(mContext, TranslationActivity.class));
                            break;
                        case "计算":
                            startActivity(new Intent(mContext, CalculationActivity.class));
                            break;
                    }
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                } else if (mDrawer != null && mCanCloseDrawer) {
                    closeDrawer();
                } else {
                    DialogUtils.showExitDialog(this, ACTIVITY_MAIN_MD);
                }
                break;
        }
        return true;  //声明对返回键按下事件的处理
    }

    public void updateBottomNavigationColor(boolean isColored) {
        mBottomNavigation.setColored(isColored);
    }

    public boolean isBottomNavigationColored() {
        return mBottomNavigation.isColored();
    }

    public void updateBottomNavigationItems(boolean isItemAdd) {
        AHBottomNavigationItem item5 = new AHBottomNavigationItem(getString(R.string.tab_5),
                ContextCompat.getDrawable(this, R.drawable.icon_bottom_tab_5),
                ContextCompat.getColor(this, R.color.color_tab_5));

        if (isItemAdd) {
            mBottomNavigation.addItem(item5);
            mBottomNavigation.setNotification(0, 0);
        } else {
            mBottomNavigation.removeAllItems();
            mBottomNavigation.addItems(mBottomNavigationItems);
        }
    }

    public int getBottomNavigationItemCount() {
        return mBottomNavigation.getItemsCount();
    }
}
