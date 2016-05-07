package com.ssdut.roysun.personalfinancialrecommendationsystem.service.binder;

import android.content.Context;
import android.os.Binder;
import android.os.Message;

import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.WeatherActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.Weather;
import com.ssdut.roysun.personalfinancialrecommendationsystem.service.SDrw;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.NetworkUtils;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by roysun on 16/3/12.
 * Binder的子类，用于Service与Activity之间通信
 */
public class WeatherBinder extends Binder {

    public static final String TAG = "WeatherBinder";

    public ArrayList<Weather> weathers = null;
    public Context mContext;

    public void getWeather(final Context context, final String citycode) {
        if (NetworkUtils.isNetworkAvailable(context)) {
            new Thread() {
                public void run() {
                    try {
                        File file = getInfo(citycode);
                        if (file != null && file.length() > 30) {
                            weathers = parseJSON(new FileInputStream(file));
                        }
                        if (weathers != null) {
                            Message msg = Message.obtain();
                            msg.what = NetworkUtils.ACCESS_SUCCESS;
                            msg.obj = weathers;
                            WeatherActivity.mMsgHandler.sendMessage(msg);
                        } else {
                            //注意到获取失败的情况handler会发两次错误请求，unbindService的时候注意只能unbind最后一次接受到失败response的时候
                            Message msg = Message.obtain();
                            msg.what = NetworkUtils.ACCESS_FAIL;
                            WeatherActivity.mMsgHandler.sendMessage(msg);
                        }
                    } catch (Exception e) {
                        Message msg = Message.obtain();
                        msg.what = NetworkUtils.ACCESS_FAIL;
                        WeatherActivity.mMsgHandler.sendMessage(msg);
                        e.printStackTrace();
                    }
                }
            }.start();
        } else {
            Message msg = Message.obtain();
            msg.what = NetworkUtils.NO_INTERNET;
            WeatherActivity.mMsgHandler.sendMessage(msg);
        }
    }

    private File getInfo(String city) throws Exception {
        File file = new File(SDrw.SDPATH + "weather/weather.txt");
        URL url = new URL("http://m.weather.com.cn/data/" + city + ".html");
        //通过网络请求方式之 HttpURLConnection，执行网络操作
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        // 设置连接超时为30秒
        conn.setConnectTimeout(30000);
        conn.connect();
        System.out.println("sdx---conn.getResponseCode()==" + conn.getResponseCode());
        if (200 == conn.getResponseCode()) {
            //获取成功
            InputStream is = conn.getInputStream();
            byte data[] = readInputStream(is);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data);
            fos.flush();
            fos.close();
            is.close();
            System.out.println("file==" + file.length());
            return file;
        } else {
            //获取失败
            Message msg = Message.obtain();
            msg.what = NetworkUtils.ACCESS_FAIL;
            WeatherActivity.mMsgHandler.sendMessage(msg);
        }
        return file;
    }

    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inputStream.read(buffer)) != -1) {
            byteOutputStream.write(buffer, 0, len);
        }
        inputStream.close();
        byte[] data = byteOutputStream.toByteArray();
        byteOutputStream.close();
        return data;
    }

    /*
     * 解析json
     */
    public ArrayList<Weather> parseJSON(InputStream inputStream) throws Exception {
        ArrayList<Weather> weathers = new ArrayList<Weather>();
        byte data[] = readInputStream(inputStream);
        String dataString = new String(data);
        JSONObject jsonData = new JSONObject(dataString);
        if (jsonData != null && jsonData.has("weatherinfo")) {
            Weather weather1 = new Weather();
            JSONObject info = jsonData.getJSONObject("weatherinfo");
            String day = info.getString("date_y");
            String week = info.getString("week");
            String chuanyi = info.getString("index_d");// 穿衣
            String chenlian = info.getString("index_cl");// 晨练
            String city = info.getString("city");// 城市
            weather1.setLocation(city);
            weather1.setDay(day);
            weather1.setWeek(week);
            weather1.setIndexClothes(chuanyi);
            weather1.setIndexTranning(chenlian);
            for (int i = 1; i <= 6; i++) {
                String tmepString = info.getString("temp" + i);
                String weatherString = info.getString("weather" + i);
                String img1String = info.getString("img" + (i * 2 - 1));
                String img2String = info.getString("img" + (i * 2));
                String windString = info.getString("wind" + i);
                if (i == 1) {
                    weather1.setTemp(tmepString);
                    weather1.setCondition(weatherString);
                    weather1.setImg1("/b" + img1String + ".gif");
                    weather1.setImg2("/b" + img2String + ".gif");
                    weather1.setWindSpeed(windString);
                    weathers.add(weather1);
                } else {
                    Weather weather = new Weather();
                    weather.setTemp(tmepString);
                    weather.setCondition(weatherString);
                    weather.setImg1("/b" + img1String + ".gif");
                    weather.setImg2("/b" + img2String + ".gif");
                    weather.setWindSpeed(windString);
                    weathers.add(weather);
                }
            }
        }
        return weathers;
    }
}
