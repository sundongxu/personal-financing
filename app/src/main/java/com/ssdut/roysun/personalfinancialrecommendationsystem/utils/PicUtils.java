package com.ssdut.roysun.personalfinancialrecommendationsystem.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ssdut.roysun.personalfinancialrecommendationsystem.service.SDrw;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by roysun on 16/3/12.
 * 图片工具类
 */
public class PicUtils {

    // 转换照片，变小利于上传，压缩后存储到cache文件夹中
    public static String compressPic(String pic, String path) {
        int _size = 800;
        String _newPic = "";
        Bitmap _bmp;
        try {
            Bitmap _b = BitmapFactory.decodeFile(pic);
            int _width = _b.getWidth();
            int _height = _b.getHeight();
            if (_width > _size || _height > _size) {
                // 通过长宽比来缩小图片
                double _ratio = ((double) _height / (double) _width);
                _bmp = Bitmap.createScaledBitmap(_b, _size, (int) (_ratio * _size), true);
                // 缩小后存储在sd卡上
                if (!SDrw.getSDPath().equals(null) && !SDrw.getSDPath().equals("")) {
                    File _dir = new File(path);
                    if (!_dir.exists()) {
                        _dir.mkdirs();
                    }
                    // 以当前时间命名存储图片
                    Date _date = new Date(System.currentTimeMillis());
                    SimpleDateFormat _dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                    File _newFile = new File(path + _dateFormat.format(_date) + ".jpg");
                    FileOutputStream _out = new FileOutputStream(_newFile);
                    _bmp.compress(Bitmap.CompressFormat.JPEG, 70, _out);
                    _newPic = _newFile.getAbsolutePath();
                } else {
                    _newPic = "";
                }
            } else {
                _newPic = pic;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return _newPic;
    }

    // 压缩图片，避免报错OOM
    public static Bitmap decodeFileAndCompress(File f) {
        Bitmap _bmp = null;
        try {
            Bitmap _bbmp = BitmapFactory.decodeFile(f.getAbsolutePath());
            int width = _bbmp.getWidth();
            int height = _bbmp.getHeight();
            if (width > 60 || height > 60) {
                //压缩到指定大小
                double _ratio = ((double) height / (double) width);
                _bmp = Bitmap.createScaledBitmap(_bbmp, 60, (int) (_ratio * 60), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return _bmp;
    }
}
