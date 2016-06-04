package com.ssdut.roysun.personalfinancialrecommendationsystem.utils;

import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemClock;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by roysun on 16/3/12.
 * 设备信息
 */
public class DeviceInfoUtils {
    public static final int MIN = 0;
    public static final int MAX = 1;
    public static final int CUR = 2;
    private static String[] commandMax = {"/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"};
    private static String[] commandMin = {"/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq"};
    private static String[] commandCur = {"/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq"};

    public static String getMaxCpu(int i) {
        String res = "";
        String command[];
        if (i == MAX) {
            command = commandMax;
        } else if (i == MIN) {
            command = commandMin;
        } else if (i == CUR) {
            command = commandCur;
        } else {
            return "";
        }
        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            Process process = pb.start();
            InputStream is = process.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte buffer[] = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            byte data[] = baos.toByteArray();
            res = new String(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static String changeCpuHZ(String str) {
        try {
            if (str.length() > 7) {
//            str = str.substring(0,str.length()-4);
                str = str.substring(0, str.length() - 6) + "00";
                double d = Double.parseDouble(str) / 1000;
                str = d + "GHZ";
            } else {
//            str = str.substring(0,str.length()-4)+"HZ";
                str = str.substring(0, str.length() - 6) + "00HZ";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    //获取CPU名字
    public static String getCpuName() {
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split(":\\s+", 2);
            return array[1];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取内存大小
    public static String[] getTotalMemory() {
        String str1 = "/proc/meminfo";
        String str2 = "";
        String memTotal = "";
        String memSheng1 = "";
        String memSheng2 = "";
        int i = 0;
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            while ((str2 = localBufferedReader.readLine()) != null) {
                i++;
                //输出   MemTotal:488604 kb  用str2.substring(0,str2.length()-6);取出488mb
                System.out.println("str2" + str2);
                if (i == 1) {
                    memTotal = str2.split(":")[1].substring(0, str2.split(":")[1].length() - 6).trim();
                } else if (i == 2) {
                    memSheng1 = str2.split(":")[1].substring(0, str2.split(":")[1].length() - 6).trim();
                    System.out.println("memSheng1" + memSheng1);
                } else if (i == 4) {
                    memSheng2 = str2.split(":")[1].substring(0, str2.split(":")[1].length() - 6).trim();
                    System.out.println("memSheng2" + memSheng2);
                    int memSheng = Integer.parseInt(memSheng1) + Integer.parseInt(memSheng2);

                    String[] ramInfoString = new String[2];
                    ramInfoString[0] = memTotal;
                    ramInfoString[1] = String.valueOf(memSheng);

                    return ramInfoString;
                }
            }
        } catch (IOException e) {
        }
        return null;
    }

    //Rom大小
    public static String[] getRomMemroy() {
        long[] romInfoLong = new long[2];
        //Total rom memory
        romInfoLong[0] = getTotalInternalMemorySize();
        //Available rom memory
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        romInfoLong[1] = blockSize * availableBlocks;
        double dTotal = (double) romInfoLong[0] / (double) (1024 * 1024);
        String resTotal = String.valueOf(dTotal).substring(0, String.valueOf(dTotal).indexOf(".") - 1);
        double dAvail = (double) romInfoLong[1] / (double) (1024 * 1024);
        String resAvail = String.valueOf(dAvail).substring(0, String.valueOf(dAvail).indexOf(".") - 1);
        String[] romInfoString = new String[2];
        dTotal = Double.parseDouble(resTotal) / 100;
        dAvail = Double.parseDouble(resAvail) / 100;
        romInfoString[0] = dTotal + "";
        romInfoString[1] = dAvail + "";
        return romInfoString;
    }

    public static long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        long total = totalBlocks * blockSize;
        return total;
    }

    //SD大小
    public static String[] getSDCardMemory() {
        long[] sdCardInfo = new long[2];
        String[] sdInfoString = new String[2];
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(sdcardDir.getPath());
            long bSize = sf.getBlockSize();
            long bCount = sf.getBlockCount();
            long availBlocks = sf.getAvailableBlocks();
            sdCardInfo[0] = bSize * bCount;//总大小
            sdCardInfo[1] = bSize * availBlocks;//可用大小

            double dTotal = (double) sdCardInfo[0] / (double) (1024 * 1024);
            String resTotal = String.valueOf(dTotal).substring(0, String.valueOf(dTotal).indexOf(".") - 1);
            double dAvail = (double) sdCardInfo[1] / (double) (1024 * 1024);
            String resAvail = String.valueOf(dAvail).substring(0, String.valueOf(dAvail).indexOf(".") - 1);
            dTotal = Double.parseDouble(resTotal) / 100;
            dAvail = Double.parseDouble(resAvail) / 100;
            sdInfoString[0] = dTotal + "";
            sdInfoString[1] = dAvail + "";
        }
        return sdInfoString;
    }

    //系统的版本信息
    public static String[] getVersion() {
        String[] version = {"null", "null", "null", "null"};
        String str1 = "/proc/version";
        String str2;
        String[] arrayOfString;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            version[0] = arrayOfString[2];//KernelVersion
            localBufferedReader.close();
        } catch (IOException e) {
        }
        version[1] = Build.VERSION.RELEASE;//firmware version  android版本
        version[2] = Build.MODEL;//model
        version[3] = Build.DISPLAY;//system version
        return version;
    }

    //获取开机时间，读取系统时钟
    public static String getTimes() {
        long ut = SystemClock.elapsedRealtime() / 1000;
        if (ut == 0) {
            ut = 1;
        }
        int m = (int) ((ut / 60) % 60);
        int h = (int) ((ut / 3600));
        return h + "小时" + m + "分";
    }

}
