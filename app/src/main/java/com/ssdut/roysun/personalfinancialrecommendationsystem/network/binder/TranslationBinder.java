package com.ssdut.roysun.personalfinancialrecommendationsystem.network.binder;

import android.content.Context;
import android.os.Binder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.TranslationActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by roysun on 16/4/18.
 * Binder的子类，用于Service与Activity之间通信
 * 包含网络操作，HttpURLConnection方式
 */
public class TranslationBinder extends Binder {

    public static final String TAG = "TranslationBinder";

    public Context mContext;

    public void getResult(final String content) {
        if (mContext instanceof TranslationActivity) {
            if (NetworkUtils.isNetworkAvailable(mContext)) {
                new Thread() {
                    public void run() {
                        try {
                            String result = getInfo(content);
                            if (result != null && !result.equals("")) {
                                //结果非空，获取成功
                                Message msg = Message.obtain();
                                msg.what = NetworkUtils.ACCESS_SUCCESS;
                                msg.obj = result;
                                ((TranslationActivity) mContext).mMsgHandler.sendMessage(msg);
                            } else {
                                //结果为空，获取失败（但并不是没有网络的情况）
                                Message msg = Message.obtain();
                                msg.what = NetworkUtils.ACCESS_FAIL;
                                ((TranslationActivity) mContext).mMsgHandler.sendMessage(msg);
                            }
                        } catch (Exception e) {
                            Message msg = Message.obtain();
                            msg.what = NetworkUtils.ACCESS_FAIL;
                            ((TranslationActivity) mContext).mMsgHandler.sendMessage(msg);
                            e.printStackTrace();
                        }
                    }
                }.start();
            } else {
                Message msg = Message.obtain();
                msg.what = NetworkUtils.NO_INTERNET;
                ((TranslationActivity) mContext).mMsgHandler.sendMessage(msg);
            }
        }
    }

    private String getInfo(String content) throws Exception {
        // http://fanyi.youdao.com/openapi.do?keyfrom=personal-financing&_key=807921974&type=_data&doctype=json&version=1.1&q=要翻译的文本
        URL url = new URL("http://fanyi.youdao.com/openapi.do?keyfrom=personal-financing" +
                "&key=807921974&type=data&doctype=json&version=1.1&q=" + URLEncoder.encode(content));
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(20000);  // 设置连接超时为20秒
        conn.connect();
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream is = conn.getInputStream();
            byte data[] = readInputStream(is);
            JSONArray jsonArray = new JSONArray("[" + new String(data) + "]");
            String message = null;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject != null) {
                    String errorCode = jsonObject.getString("errorCode");
                    if (errorCode.equals("20")) {
                        Toast.makeText(mContext, "翻译的文本过长", Toast.LENGTH_SHORT).show();
                    } else if (errorCode.equals("30 ")) {
                        Toast.makeText(mContext, "无法进行有效的翻译", Toast.LENGTH_SHORT).show();
                    } else if (errorCode.equals("40")) {
                        Toast.makeText(mContext, "不支持的语言类型", Toast.LENGTH_SHORT).show();
                    } else if (errorCode.equals("50")) {
                        Toast.makeText(mContext, "无效的key", Toast.LENGTH_SHORT).show();
                    } else {
                        // 要翻译的内容
                        message = jsonObject.getString("query");
                        // 翻译内容
                        String translation = jsonObject.getString("translation");
                        message = message + "\n" + translation;
                        // 有道词典-基本词典
                        if (jsonObject.has("basic")) {
                            JSONObject basic = jsonObject.getJSONObject("basic");
                            if (basic.has("phonetic")) {
                                String phonetic = basic.getString("phonetic");
                                message += "\n\n" + "音标：[" + phonetic + "]";
                            }
                            if (basic.has("explains")) {
                                String explains = basic.getString("explains");
                                message += "\n\n基础释义：\n" + explains;
                            }
                        }
                        // 有道词典-网络释义
                        if (jsonObject.has("web")) {
                            String web = jsonObject.getString("web");
                            JSONArray webString = new JSONArray("[" + web + "]");
                            message += "\n\n网络释义：";
                            JSONArray webArray = webString.getJSONArray(0);
                            int count = 0;
                            while (!webArray.isNull(count)) {
                                if (webArray.getJSONObject(count).has("key")) {
                                    String key = webArray.getJSONObject(count)
                                            .getString("key");
                                    message += "\n\t<" + (count + 1) + ">"
                                            + key;
                                }
                                if (webArray.getJSONObject(count).has("value")) {
                                    String value = webArray
                                            .getJSONObject(count).getString("value");
                                    message += "\n\t   " + value;
                                }
                                count++;
                            }
                        }
                    }
                }
            }
            Log.v(TAG, "message=" + message);
            return message;
        } else {
            Message msg = Message.obtain();
            msg.what = NetworkUtils.ACCESS_FAIL;
            ((TranslationActivity) mContext).mMsgHandler.sendMessage(msg);
        }
        return null;
    }

    private byte[] readInputStream(InputStream inputStream) throws IOException {
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

}
