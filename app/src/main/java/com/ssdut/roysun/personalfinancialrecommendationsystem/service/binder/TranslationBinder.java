package com.ssdut.roysun.personalfinancialrecommendationsystem.service.binder;

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
                            String _result = getInfo(content);
                            if (_result != null && !_result.equals("")) {
                                //结果非空，获取成功
                                Message _msg = Message.obtain();
                                _msg.what = NetworkUtils.ACCESS_SUCCESS;
                                _msg.obj = _result;
                                ((TranslationActivity) mContext).mMsgHandler.sendMessage(_msg);
                            } else {
                                //结果为空，获取失败（但并不是没有网络的情况）
                                Message _msg = Message.obtain();
                                _msg.what = NetworkUtils.ACCESS_FAIL;
                                ((TranslationActivity) mContext).mMsgHandler.sendMessage(_msg);
                            }
                        } catch (Exception e) {
                            Message _msg = Message.obtain();
                            _msg.what = NetworkUtils.ACCESS_FAIL;
                            ((TranslationActivity) mContext).mMsgHandler.sendMessage(_msg);
                            e.printStackTrace();
                        }
                    }
                }.start();
            } else {
                Message _msg = Message.obtain();
                _msg.what = NetworkUtils.NO_INTERNET;
                ((TranslationActivity) mContext).mMsgHandler.sendMessage(_msg);
            }
        }
    }

    private String getInfo(String content) throws Exception {
        // http://fanyi.youdao.com/openapi.do?keyfrom=personal-financing&_key=807921974&type=_data&doctype=json&version=1.1&q=要翻译的文本
        URL _url = new URL("http://fanyi.youdao.com/openapi.do?keyfrom=personal-financing"
                + "&key=807921974&type=data&doctype=json&version=1.1&q=" + URLEncoder.encode(content));
        HttpURLConnection _conn = (HttpURLConnection) _url.openConnection();
        _conn.setRequestMethod("GET");
        _conn.setConnectTimeout(15000);  // 设置连接超时为15秒
        _conn.connect();
        if (_conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream _is = _conn.getInputStream();
            byte _data[] = readInputStream(_is);
            JSONArray _jsonArray = new JSONArray("[" + new String(_data) + "]");
            String _message = null;
            for (int i = 0; i < _jsonArray.length(); i++) {
                JSONObject _jsonObject = _jsonArray.getJSONObject(i);
                if (_jsonObject != null) {
                    String _errorCode = _jsonObject.getString("errorCode");
                    if (_errorCode.equals("20")) {
                        Toast.makeText(mContext, "翻译的文本过长", Toast.LENGTH_SHORT).show();
                    } else if (_errorCode.equals("30 ")) {
                        Toast.makeText(mContext, "无法进行有效的翻译", Toast.LENGTH_SHORT).show();
                    } else if (_errorCode.equals("40")) {
                        Toast.makeText(mContext, "不支持的语言类型", Toast.LENGTH_SHORT).show();
                    } else if (_errorCode.equals("50")) {
                        Toast.makeText(mContext, "无效的key", Toast.LENGTH_SHORT).show();
                    } else {
                        // 要翻译的内容
                        String _query = _jsonObject.getString("query");
                        _message = _query;
                        // 翻译内容
                        String _translation = _jsonObject.getString("translation");
                        _message = _message + "\n" + _translation;
                        // 有道词典-基本词典
                        if (_jsonObject.has("basic")) {
                            JSONObject _basic = _jsonObject.getJSONObject("basic");
                            if (_basic.has("phonetic")) {
                                String _phonetic = _basic.getString("phonetic");
                                _message += "\n\n" + "音标：[" + _phonetic + "]";
                            }
                            if (_basic.has("explains")) {
                                String _explains = _basic.getString("explains");
                                _message += "\n\n基础释义：\n" + _explains;
                            }
                        }
                        // 有道词典-网络释义
                        if (_jsonObject.has("web")) {
                            String _web = _jsonObject.getString("web");
                            JSONArray _webString = new JSONArray("[" + _web + "]");
                            _message += "\n\n网络释义：";
                            JSONArray _webArray = _webString.getJSONArray(0);
                            int count = 0;
                            while (!_webArray.isNull(count)) {
                                if (_webArray.getJSONObject(count).has("key")) {
                                    String _key = _webArray.getJSONObject(count)
                                            .getString("key");
                                    _message += "\n\t<" + (count + 1) + ">"
                                            + _key;
                                }
                                if (_webArray.getJSONObject(count).has("value")) {
                                    String _value = _webArray
                                            .getJSONObject(count).getString("value");
                                    _message += "\n\t   " + _value;
                                }
                                count++;
                            }
                        }
                    }
                }
            }
            Log.v(TAG, "_message=" + _message);
            return _message;
        } else {
            Message _msg = Message.obtain();
            _msg.what = NetworkUtils.ACCESS_FAIL;
            ((TranslationActivity) mContext).mMsgHandler.sendMessage(_msg);
        }
        return null;
    }

    private byte[] readInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream _byteOutputStream = new ByteArrayOutputStream();
        byte[] _buffer = new byte[1024];
        int _len = -1;
        while ((_len = inputStream.read(_buffer)) != -1) {
            _byteOutputStream.write(_buffer, 0, _len);
        }
        inputStream.close();
        byte[] _data = _byteOutputStream.toByteArray();
        _byteOutputStream.close();
        return _data;
    }

}
