package com.ssdut.roysun.personalfinancialrecommendationsystem.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by roysun on 16/4/26.
 * sharedPreference工具类
 */
public class SharedPreferenceUtils {
    // Avoid magic numbers.
    public static final int MAX_SIZE = 5;

    public SharedPreferenceUtils() {
        super();
    }

    public static void storeList(Context context, String fileName, String key, List list) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        editor = settings.edit();
        Gson gson = new Gson();
        String strJson = gson.toJson(list);
        editor.putString(key, strJson);
        editor.apply();
    }

    public static ArrayList<String> loadList(Context context, String fileName, String key) {
        SharedPreferences settings;
        List<String> itemList;
        settings = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        if (settings.contains(key)) {
            String strJson = settings.getString(key, null);
            Gson gson = new Gson();
            String[] Items = gson.fromJson(strJson, String[].class);
            itemList = Arrays.asList(Items);
            itemList = new ArrayList<>(itemList);
        } else {
            return null;
        }
        return (ArrayList<String>) itemList;
    }

    public static void addToList(Context context, String fileName, String key, String value) {
        List<String> list = loadList(context, fileName, key);
        if (list == null)
            list = new ArrayList<>();

        if (list.contains(value)) {
            list.remove(value);
        }

        // 不应该删除吧，应该删除第一个加入list的item
        if (list.size() > MAX_SIZE) {
            list.remove(0);
        }

        list.add(value);
        storeList(context, fileName, key, list);
    }

    public static void deleteList(Context context, String fileName) {
        SharedPreferences myPrefs = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.clear();
        editor.apply();
    }
}
