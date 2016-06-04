package com.ssdut.roysun.personalfinancialrecommendationsystem.utils;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by roysun on 16/3/12.
 * SD卡文件操作
 */
public class SDrw {

    public static final String SDPATH = getSDPath() + "/ssdut/personalfinancialsystemwithrecommendation/";

    private String name;
    private boolean isAppend;
    private File file;
    private String dir;//除去sd卡路径的文件路径

    public SDrw(String name, boolean isAppend, String dir) {
        this.name = name;
        this.isAppend = isAppend;
        this.dir = dir;
        createDir();
    }

    /*
     * 获取sd卡路径
     */
    public static String getSDPath() {
        File sdDir;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取根目录
            return sdDir.toString();
        } else {
            return "";
        }
    }

    public boolean isInclude() {
        boolean flag = false;
        File file = new File(SDPATH + dir, name);
        if (file.exists() && file.length() > 1) {
            flag = true;
        } else {
            flag = false;
        }
        return flag;
    }

    public void createDir() {
        file = new File(SDPATH + dir, name);
        File dirPath = new File(SDPATH + dir);
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (!isAppend) {
                file.delete();
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void outWrite(String str) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, isAppend));
            bw.write(str);
            bw.newLine();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String readIn() {
        String str = "";
        String str1 = "";
        try {
            if (file.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(file));
                while ((str = br.readLine()) != null) {
                    str1 = str.trim() + "\r\n" + str1.trim();
                }
                br.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str1;
    }
}
