package com.ssdut.roysun.personalfinancialrecommendationsystem.activity;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.CityCode;
import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.Weather;
import com.ssdut.roysun.personalfinancialrecommendationsystem.network.binder.WeatherBinder;
import com.ssdut.roysun.personalfinancialrecommendationsystem.network.service.ImageLoader;
import com.ssdut.roysun.personalfinancialrecommendationsystem.network.service.WeatherService;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.DialogUtils;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.NetworkUtils;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.TimeUtils;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.ToastUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by roysun on 16/3/12.
 * 天气页面
 */
public class WeatherActivity extends BaseActivity implements View.OnClickListener, ServiceConnection {

    public static final String TAG = "WeatherActivity";
    public static MessageHandler mMsgHandler;  //Handler用来后台更新界面
    private static HashMap<String, String> sHashMap;
    InputMethodManager mInputMethodManager = null;  //获取输入法管理器
    //存储城市列表代码
    ArrayList<CityCode> mCodeList = null;
    private WeatherBinder mBinder;
    private TextView mCity, mCondition, mTemperature, mWindSpeed, mTimeUpdate;  //TextView标题（城市名），当前天气状态，当前温度，当前风速，数据更新时间
    private ImageView mWeatherPic;  //当前天气图片
    private ImageButton mIbQuery;
    private ListView mFurtureWeatherList;  //最近四天天气list
    private EditText mCityInput;  //搜索城市的编辑框
    private LinearLayout mWeatherNowArea, mWeatherArea;  //当前天气的内容的LinerLayout
    private WeatherListAdapter mWeatherListAdapter = null;  //天气列表适配器
    private ProgressDialog mProgressDialog;  //搜索时的显示进度
    private ArrayList<Weather> mWeatherList;  //最近天气list集合
    private ImageLoader mImageLoader = null;  //异步加载天气图片

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        initData();
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        mFurtureWeatherList = (ListView) this.findViewById(R.id.lv_weather_list);
        mIbQuery = (ImageButton) this.findViewById(R.id.ib_query);
        mIbQuery.setOnClickListener(this);
        mCity = (TextView) this.findViewById(R.id.tv_city_now);
        mCondition = (TextView) this.findViewById(R.id.tv_condition_now);
        mTemperature = (TextView) this.findViewById(R.id.tv_temperature_now);
        mWindSpeed = (TextView) this.findViewById(R.id.tv_wind_speed_now);
        mTimeUpdate = (TextView) this.findViewById(R.id.tv_time_update);
        mWeatherPic = (ImageView) this.findViewById(R.id.iv_weather_now_pic);
        mWeatherNowArea = (LinearLayout) this.findViewById(R.id.ll_weather_condition_now);
        mWeatherNowArea.setVisibility(View.INVISIBLE);
        mWeatherArea = (LinearLayout) this.findViewById(R.id.ll_weather_condition);
        mWeatherArea.getBackground().setAlpha(200);
        mCityInput = (EditText) findViewById(R.id.et_city_input);
        mCityInput.getBackground().setAlpha(200);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    protected void initData() {
        super.initData();
        mImageLoader = new ImageLoader();
        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mMsgHandler = new MessageHandler();
        parseCodeFromText();
    }

    //解析code.txt放入HashMap中方便查询
    public void parseCodeFromText() {
        mCodeList = new ArrayList<CityCode>();
        sHashMap = new HashMap<String, String>();
        new Thread() {
            public void run() {
                try {
                    AssetManager assetManager = getAssets();
                    InputStream inputStream = assetManager.open("code.txt");
                    byte data[] = WeatherBinder.readInputStream(inputStream);
                    String string = new String(data);
                    String s[] = string.split("\\|");
                    sHashMap.put("北京", "101010100");//北京在for循环中没有加入，在这手动加入
                    for (String code : s) {
                        String codeOne[] = code.split(",");
                        sHashMap.put(codeOne[0], codeOne[1]);
                        CityCode cityCode = new CityCode();
                        cityCode.setCity(codeOne[0]);
                        cityCode.setCode(codeOne[1]);
                        mCodeList.add(cityCode);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public String getCode(String city) {
        if (sHashMap.containsKey(city)) {
            return sHashMap.get(city);
        } else {
            if (city.length() >= 2) {
                for (CityCode code : mCodeList) {
                    if (code.getCity().endsWith(city)) {
                        return code.getCode();
                    }
                }
                return "";
            } else {
                return "";
            }
        }
    }

    @Override
    protected void onResume() {
        overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ib_query://搜索按钮
                //隐藏输入键盘
                mInputMethodManager.hideSoftInputFromWindow(mCityInput.getWindowToken(), 0);
                //启动后台服务，利用bindService绑定服务
                Intent _intent = new Intent(WeatherActivity.this, WeatherService.class);
                bindService(_intent, this, Context.BIND_AUTO_CREATE);
                break;
        }
    }

    public void showProgress(String info) {
        mProgressDialog = ProgressDialog.show(this, "", "正在搜索" + info + "，请稍后...", true, true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: {
                DialogUtils.showExitDialog(this, ACTIVITY_WEATHER);
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
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
                mCityInput.setText("");
                if (mWeatherListAdapter != null) {
                    mWeatherList.clear();
                    mWeatherListAdapter.notifyDataSetChanged();
                }
                mWeatherNowArea.setVisibility(View.INVISIBLE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.v("WeatherService", "onServiceConnected !!!");
        mBinder = (WeatherBinder) service;
        mBinder.mContext = WeatherActivity.this;
        //获取搜索城市名
        String _cityName = mCityInput.getText().toString().trim();
        showProgress("天气");//显示进度
        mCityInput.setText(_cityName);
        Editable _ea = mCityInput.getText();  //设置光标在文字末尾
        Selection.setSelection((Spannable) _ea, _ea.length());
        //如果当前列表集合中有数据则清空
        if (mWeatherList != null) {
            mWeatherList.clear();
        }
        //去掉城市名后面的市和县
        if ((_cityName.endsWith("市") || _cityName.endsWith("县")) && _cityName.length() >= 3) {
            _cityName = _cityName.substring(0, _cityName.length() - 1);
        }
        String _cityCode = getCode(_cityName);
        if (_cityCode == null || _cityCode.equals("")) {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.cancel();
            }
            ToastUtils.showMsg(WeatherActivity.this, "该城市不存在");
            unbindService(this);
            return;
        }
        mBinder.getWeather(this, _cityCode);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    public class MessageHandler extends Handler {
        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            //在非主线程更新UI的处理
            switch (msg.what) {
                case NetworkUtils.ACCESS_SUCCESS:
                    if (mProgressDialog != null && mProgressDialog.isShowing()) {
                        mProgressDialog.cancel();
                    }
                    mWeatherList = (ArrayList<Weather>) msg.obj;
                    if (mWeatherList == null || mWeatherList.size() <= 1) {
                        ToastUtils.showMsg(WeatherActivity.this, "搜索有误，请重新搜索");
                        return;
                    }
                    Weather nowWeather = mBinder.weathers.get(0);
                    mWeatherNowArea.setVisibility(View.VISIBLE);
                    mWeatherListAdapter = new WeatherListAdapter();
                    mFurtureWeatherList.setAdapter(mWeatherListAdapter);
                    mCity.setText(nowWeather.getLocation());
                    mCondition.setText("状态：" + nowWeather.getCondition());
                    mTemperature.setText("温度：" + nowWeather.getTemp());
                    mWindSpeed.setText("穿衣指数：" + nowWeather.getIndexClothes());
                    mTimeUpdate.setText("风力：" + nowWeather.getWindSpeed());
                    String imgurl = "";
                    if (nowWeather.getImg1().contains("99")) {
                        imgurl = nowWeather.getImg2();
                    } else {
                        imgurl = nowWeather.getImg1();
                    }
                    if (imgurl != null && imgurl.length() > 1) {
                        Drawable drawable = mImageLoader.loadDrawable("http://m.weather.com.cn/img" + imgurl, mWeatherPic, new ImageLoader.ImageCallback() {
                            public void imageLoaded(Drawable imageDrawable, ImageView imageView, String imageUrl) {
                                mWeatherPic.setImageDrawable(imageDrawable);
                            }
                        });
                        if (drawable == null) {
                        } else {
                            mWeatherPic.setImageDrawable(drawable);
                        }
                    }
                    unbindService(WeatherActivity.this);
                    break;
                case NetworkUtils.ACCESS_FAIL:
                    if (mProgressDialog != null && mProgressDialog.isShowing()) {
                        mProgressDialog.cancel();
                        unbindService(WeatherActivity.this);
                        ToastUtils.showMsg(WeatherActivity.this, "服务器出错，请稍后再试");
                    }
                    break;
                case NetworkUtils.NO_INTERNET:
                    ToastUtils.showMsg(WeatherActivity.this, "没有连接到网络");
                    if (mProgressDialog != null && mProgressDialog.isShowing()) {
                        mProgressDialog.cancel();
                    }
                    unbindService(WeatherActivity.this);
                    break;
            }
            super.handleMessage(msg);
        }
    }

    private class WeatherListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mWeatherList.size();
        }

        @Override
        public Object getItem(int position) {
            return mWeatherList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(WeatherActivity.this).inflate(R.layout.weather_list_item, null);
            TextView week = (TextView) convertView.findViewById(R.id.tv_item_week);
            TextView date = (TextView) convertView.findViewById(R.id.tv_item_date);
            TextView temp = (TextView) convertView.findViewById(R.id.tv_item_temperature);
            TextView condition = (TextView) convertView.findViewById(R.id.tv_item_condition);
            final ImageView pic = (ImageView) convertView.findViewById(R.id.iv_item_pic);
            Weather weather = mWeatherList.get(position);
            //因为返回之中没有日期，只能得到当前日期然后再算出这四天的日期
            int today = TimeUtils.getDay() + position;
            int cdyear = TimeUtils.getYear();
            int cdmonth;
            int cdday;
            if (TimeUtils.getMonth() == 4 || TimeUtils.getMonth() == 6 || TimeUtils.getMonth() == 11) {
                if (today > 30) {
                    cdmonth = TimeUtils.getMonth() + 1;
                    cdday = today - 30;
                } else {
                    cdmonth = TimeUtils.getMonth();
                    cdday = today;
                }
            } else if (TimeUtils.getMonth() == 2) {
                //判断二月时间（包括闰年）
                if (TimeUtils.getYear() % 4 != 0 && TimeUtils.getYear() % 1000 != 0) {
                    if (today > 28) {
                        cdmonth = TimeUtils.getMonth() + 1;
                        cdday = today - 28;
                    } else {
                        cdmonth = TimeUtils.getMonth();
                        cdday = today;
                    }
                } else {
                    if (today > 29) {
                        cdmonth = TimeUtils.getMonth() + 1;
                        cdday = today - 29;
                    } else {
                        cdmonth = TimeUtils.getMonth();
                        cdday = today;
                    }
                }
            } else {
                if (today > 31) {
                    cdmonth = TimeUtils.getMonth() + 1;
                    cdday = today - 31;
                } else {
                    cdmonth = TimeUtils.getMonth();
                    cdday = today;
                }
            }
            //设置时间 计算星期
            Calendar cd = Calendar.getInstance();
            cd.set(cdyear, cdmonth - 1, cdday - 1);  //计算星期  月和日必须减一
            int weekday = cd.get(Calendar.DAY_OF_WEEK);
            String day = "";
            if (weekday == 1) {
                day = "星期一";
            } else if (weekday == 2) {
                day = "星期二";
            } else if (weekday == 3) {
                day = "星期三";
            } else if (weekday == 4) {
                day = "星期四";
            } else if (weekday == 5) {
                day = "星期五";
            } else if (weekday == 6) {
                day = "星期六";
            } else if (weekday == 7) {
                day = "星期日";
            }
            date.setText(cdmonth + "月" + cdday + "日");
            week.setText(day);
            temp.setText(weather.getTemp());
            condition.setText(weather.getCondition());
            String imgurl = "";
            if (weather.getImg1().contains("99")) {
                imgurl = weather.getImg2();
            } else {
                imgurl = weather.getImg1();
            }
            if (imgurl != null && imgurl.length() > 1) {
                Drawable drawable = mImageLoader.loadDrawable("http://m.weather.com.cn/img" + imgurl, pic, new ImageLoader.ImageCallback() {
                    public void imageLoaded(Drawable imageDrawable, ImageView imageView, String imageUrl) {
                        pic.setImageDrawable(imageDrawable);
                    }
                });
                if (drawable == null) {
                } else {
                    pic.setImageDrawable(drawable);
                }
            }
            return convertView;
        }
    }
}
