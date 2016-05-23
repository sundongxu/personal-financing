package com.ssdut.roysun.personalfinancialrecommendationsystem.activity;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.fragment.AccountFragment;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.fragment.BaseFragment;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.fragment.FinanceFragment;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.fragment.JournalFragment;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.fragment.OthersFragment;
import com.ssdut.roysun.personalfinancialrecommendationsystem.adapter.DrawerMenuListAdapter;
import com.ssdut.roysun.personalfinancialrecommendationsystem.adapter.SearchAdapter;
import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.User;
import com.ssdut.roysun.personalfinancialrecommendationsystem.component.DividerItemDecoration;
import com.ssdut.roysun.personalfinancialrecommendationsystem.listener.SnackbarClickListener;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.DialogUtils;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.SharedPreferenceUtils;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.ToastUtils;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by roysun on 16/4/1
 * MD风格的主Activity，采用AHBottomNavigation底部导航栏，上滑时导航栏隐藏，下滑时出现
 * bug1：快速返回关闭Drawer后需返回两次才可以出现退出对话框
 * bug2：点击Drawer item跳转其它的Activity后出现RecycleView: No adapter attached
 */
public class MainActivity extends BaseActivity {

    public static final String TAG = "MainActivity";

    public final int DRAWER_OPEN = 0;
    public final int DRAWER_CLOSED = 1;

    private BaseFragment mCurrentFragment;
    private ArrayList<AHBottomNavigationItem> mBottomNavigationItems = new ArrayList<>();
    private FragmentManager mFragmentManager = getFragmentManager();
    private AHBottomNavigation mBottomNavigation;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private boolean mCanCloseDrawer;  // 只要抽屉开始滑动了就可以返回键关闭
    private int mPreDrawerState;
    private int[] mPreScrollYList;

    // 侧边Drawer
    private CircleImageView mUserIconView;  // 导航头像
    private TextView mUserNameText;  // 用户名
    private RecyclerView mDrawerList;  // 包含Drawer5个菜单项
    private DrawerMenuListAdapter mDrawerAdapter;

    private Dialog mDialog;
    private ArrayList<String> mThingList;
    private InputMethodManager mInputMethodManager;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolBar();
        initData();
        initView();
    }

    @Override
    protected void initData() {
        super.initData();
        mContext = this;
        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mCanCloseDrawer = false;
        mPreDrawerState = DRAWER_CLOSED;
        ArrayList<String> _userList = SharedPreferenceUtils.loadList(mContext, Utils.LOGIN_HISTORY, Utils.USERNAME_LAST_LOGIN);
        ArrayList<String> _passwordList = SharedPreferenceUtils.loadList(mContext, Utils.LOGIN_HISTORY, Utils.PASSWORD_LAST_LOGIN);
        if (_userList != null && _userList.size() <= SharedPreferenceUtils.MAX_SIZE && _passwordList != null && _passwordList.size() <= SharedPreferenceUtils.MAX_SIZE) {
            // 有记住上次登录的用户名
            String _userNameLastLogin = _userList.get(_userList.size() - 1);
            String _passwordLastLogin = _passwordList.get(_passwordList.size() - 1);
            mUserManager.signIn(_userNameLastLogin, _passwordLastLogin);
            if (mUserManager.isSignIn()) {
                if (mUserManager.isSpecialAccount() == 1) {
                    Snackbar.make(mToolbar, "管理员" + _userNameLastLogin + "登录成功！", Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
                } else {
                    Snackbar.make(mToolbar, "用户" + _userNameLastLogin + "登录成功！", Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
                }
            } else {
                //登录失败
                if (mUserManager.isUserExists(_userNameLastLogin)) {
                    ToastUtils.showMsg(mContext, "密码错误！");
                } else {
                    ToastUtils.showMsg(mContext, "用户名不存在！");
                }
            }
        }
        mPreScrollYList = new int[4];
    }

    @Override
    protected void initView() {
//        super.initView();
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
            public void onTabSelected(final int position, boolean wasSelected) {
                if (!wasSelected) {
                    Log.v(TAG, "position=" + position);
                    switch (position) {
                        case BaseFragment.TAB_JOURNAL:
                            mCurrentFragment = JournalFragment.newInstance();
                            break;
                        case BaseFragment.TAB_FINANCE:
                            mCurrentFragment = FinanceFragment.newInstance();
                            break;
                        case BaseFragment.TAB_ACCOUNT:
                            mCurrentFragment = AccountFragment.newInstance();
                            break;
                        case BaseFragment.TAB_OTHERS:
                            mCurrentFragment = OthersFragment.newInstance();
                            break;
                    }
                    mFragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                            .replace(R.id.fl_container, mCurrentFragment)
                            .commit();
                    mCurrentFragment.setSaveScrollYCallback(new BaseFragment.SaveScrollYCallback() {
                        @Override
                        public void notifyPreScrollY(int preScrollY) {
                            mPreScrollYList[position] = preScrollY;
                        }
                    });
                } else if (position > 0) {
                    mCurrentFragment.refresh();
                }
            }
        });
        mCurrentFragment = JournalFragment.newInstance();  //初始化显示第一个tab
        mCurrentFragment.setSaveScrollYCallback(new BaseFragment.SaveScrollYCallback() {
            @Override
            public void notifyPreScrollY(int preScrollY) {
                mPreScrollYList[BaseFragment.TAB_JOURNAL] = preScrollY;
            }
        });
        mFragmentManager.beginTransaction()
                .replace(R.id.fl_container, mCurrentFragment)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 触发情形（1）
        onLoginStateChanged(mUserManager.isSignIn());
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
                DialogUtils.showExitDialog(this, BaseActivity.ACTIVITY_MAIN);
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
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
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
                    case DrawerMenuListAdapter.ITEM_SIGN_OUT:
                        if (mUserManager.isSignIn()) {
                            mUserManager.signOut();
                            ToastUtils.showMsg(mContext, "用户已注销", Toast.LENGTH_SHORT);
                            // 触发情形（3）
                            onLoginStateChanged(false);
                        }
                        break;
                    case DrawerMenuListAdapter.ITEM_DEVICE_INFO:
                        startActivity(new Intent(mContext, DeviceInfoActivity.class));
                        break;
                    case DrawerMenuListAdapter.ITEM_APP_INFO:
                        startActivity(new Intent(mContext, AppInfoActivity.class));
                        break;
                    case DrawerMenuListAdapter.ITEM_EXIT:
                        DialogUtils.showExitDialog(mContext, BaseActivity.ACTIVITY_MAIN);
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

    /**
     * 三种情形触发：
     * （1）在LoginActivityMD中登录成功后触发，isLogin = true
     * （2）在记住密码后重启App，自动登录时触发，isLogin = true
     * （3）注销用户后触发，isLogin = false
     *
     * @param isLogin 登录标志位
     */
    private void onLoginStateChanged(boolean isLogin) {
        if (isLogin) {
            if (mUserIconView != null) {
                mUserIconView.setImageResource(R.drawable.icon_signin);
            }
            if (mUserNameText != null) {
                mUserNameText.setText(mUserManager.getCurUser().getName());
            }
        } else {
            if (mUserIconView != null) {
                mUserIconView.setImageResource(R.drawable.icon_not_signin);
            }
            if (mUserNameText != null) {
                mUserNameText.setText("未登录");
            }
        }
    }

    private void setDrawerListHeader(RecyclerView recyclerView) {
        View _header = LayoutInflater.from(this).inflate(R.layout.drawer_header, recyclerView, false);
        mUserIconView = (CircleImageView) _header.findViewById(R.id.civ_menu_user_icon);
        mUserNameText = (TextView) _header.findViewById(R.id.tv_menu_user_name);
        // 触发情形（2）
        onLoginStateChanged(mUserManager.isSignIn());
        //设置用户头像
        mUserIconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mUserManager.isSignIn()) {
                    startActivity(new Intent(mContext, LoginActivity.class));
//                    closeDrawer();
                }
            }
        });
        mDrawerAdapter.setHeaderView(_header);
    }

    private void closeDrawer() {
        if (mDrawer != null && mCanCloseDrawer) {
            mCanCloseDrawer = false;
            mPreDrawerState = DRAWER_CLOSED;
            mDrawer.closeDrawer(Gravity.LEFT);
        }
    }

    private void showSearchDialog() {
        ArrayList<String> _searchContent = SharedPreferenceUtils.loadList(mContext, Utils.SEARCH_HISTORY, Utils.SEARCH_CONTENT);
        View view = getLayoutInflater().inflate(R.layout.toolbar_search, null);
        ImageView _backIcon = (ImageView) view.findViewById(R.id.iv_tool_back);
        ImageView _searchIcon = (ImageView) view.findViewById(R.id.iv_tool_search);
        final EditText _searchView = (EditText) view.findViewById(R.id.et_tool_search);
        final ListView _searchList = (ListView) view.findViewById(R.id.lv_search_list);
        final TextView _notFoundText = (TextView) view.findViewById(R.id.tv_not_found);
        Utils.setListViewHeightBasedOnChildren(_searchList);
        _searchView.setHint("你想要做什么");

        mDialog = new Dialog(mContext, R.style.MaterialSearch);
        mDialog.setContentView(view);
//        mDialog.setCancelable(false);  // 设置这里可让mDialog按下返回键也不消失
        mDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mDialog.getWindow().setGravity(Gravity.BOTTOM);
        mDialog.show();
        mDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);  // 搜索dialog一出来输入框就显示软键盘，去掉这句要点击输入框才出现
        _searchContent = (_searchContent != null && _searchContent.size() > 0) ? _searchContent : new ArrayList<String>();
        final SearchAdapter _searchAdapter = new SearchAdapter(mContext, _searchContent, false);
        _searchList.setVisibility(View.VISIBLE);
        _searchList.setAdapter(_searchAdapter);
        _searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String _strInput = String.valueOf(adapterView.getItemAtPosition(position));
                if (_strInput.equals("删除历史记录")) {
                    SharedPreferenceUtils.deleteList(mContext, Utils.SEARCH_HISTORY);
                    _searchList.setVisibility(View.GONE);
                } else {
                    SharedPreferenceUtils.addToList(mContext, Utils.SEARCH_HISTORY, Utils.SEARCH_CONTENT, _strInput);
                    _searchView.setText(_strInput);
                    _searchList.setVisibility(View.GONE);
                }
            }
        });
        _searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String[] _fun = mContext.getResources().getStringArray(R.array.fun_array);
                mThingList = new ArrayList<String>(Arrays.asList(_fun));
                _searchList.setVisibility(View.VISIBLE);
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
                            _searchList.setVisibility(View.VISIBLE);
                            _searchAdapter.updateList(filterList, true);
                            isNodata = true;
                        }
                    }
                    if (!isNodata) {
                        _searchList.setVisibility(View.GONE);
                        _notFoundText.setVisibility(View.VISIBLE);
                        _notFoundText.setText("没有找到");
                    }
                } else {
                    _searchList.setVisibility(View.GONE);
                    _notFoundText.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        _backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);  //点击按钮就隐藏软键盘，特么居然跟这里执行顺序有关系
                if (mDialog != null) {
                    mDialog.dismiss();
                }
//                mInputMethodManager.hideSoftInputFromWindow(edtToolSearch.getWindowToken(), 0);  //为什么无效？？？
            }
        });

        _searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //可以实现搜索跳转对应Activity功能
//                edtToolSearch.setText("");
//                SharedPreferenceUtils.deleteList(MainActivity.this, Utils.SEARCH_HISTORY); //清除历史
                String _strInput = String.valueOf(_searchView.getText());
                if (!_strInput.equals("")) {
                    //输入框非空才跳转
                    if (mThingList.contains(_strInput)) {
                        SharedPreferenceUtils.addToList(mContext, Utils.SEARCH_HISTORY, Utils.SEARCH_CONTENT, _strInput);
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
                    DialogUtils.showExitDialog(this, ACTIVITY_MAIN);
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
            mBottomNavigation.setNotification(3, 4);
        } else {
            mBottomNavigation.removeAllItems();
            mBottomNavigation.addItems(mBottomNavigationItems);
        }
    }

    public int getBottomNavigationItemCount() {
        return mBottomNavigation.getItemsCount();
    }

    public int[] getPreScrollYList() {
        return mPreScrollYList;
    }
}
