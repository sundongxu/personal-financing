package com.ssdut.roysun.personalfinancialrecommendationsystem.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by roysun on 16/3/12.
 * 图片下载
 */
public class ImageLoader {
    public Drawable loadDrawable(final String imageUrl, final ImageView imageView, final ImageCallback imageCallback) {
        // 根据传入的参数不同而执行不同的操作
        return isHasImg(imageUrl, imageView, imageCallback);
    }

    private Drawable isHasImg(final String imageUrl, final ImageView imageView, final ImageCallback imageCallback) {
        // 以‘/’来拆分传入的链接地址，因为该链接格式是固定的
        String imageName = imageUrl.lastIndexOf("/") != 0 ? imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.length()) : "";
        File file = new File(SDrw.SDPATH + "tianqi/imgcache", imageName);
        Drawable d = null;
        FileInputStream fis = null;
        if (file.exists()) {// 判断请求的图像是否在本地存在
            try {
                fis = new FileInputStream(file);
                d = Drawable.createFromStream(fis, "src");
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return d;
        } else {// 如果本地不存在则开始下载
            final Handler handler = new Handler() {
                public void handleMessage(Message message) {
                    imageCallback.imageLoaded((Drawable) message.obj, imageView, imageUrl);
                }
            };
            // 建立新一个新的线程下载图片
            new Thread() {
                @Override
                public void run() {
                    Drawable drawable = loadImageFromUrl(imageUrl);
                    Message message = handler.obtainMessage(0, drawable);
                    handler.sendMessage(message);
                }
            }.start();
            return null;
        }
    }

    private Drawable loadImageFromUrl(final String url) {
        Drawable d = null;
        try {
            d = creatDrawable(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }

    /*
     * 通过提供的连接下载该图片，并将其存储在本地
     */
    private Drawable creatDrawable(final String url) throws Exception {
        URL m = new URL(url);
        InputStream i = (InputStream) m.getContent();
        Bitmap b = BitmapFactory.decodeStream(i);
        BitmapDrawable bd = new BitmapDrawable(b);
        String imageName = url.lastIndexOf("/") != 0 ? url.substring(url.lastIndexOf("/") + 1, url.length()) : "";
        File file = null;
        if (!SDrw.getSDPath().equals("") && !SDrw.getSDPath().equals(null)) {
            File dir = new File(SDrw.SDPATH + "tianqi/imgcache");
            file = new File(dir, imageName);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            } else {
                file.delete();
            }
            // 把该图片写入到sd卡
            FileOutputStream fos = new FileOutputStream(file, true);
            b.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            i.close();
        }
        return bd;
    }

    // 回调接口 用来加载图片
    public interface ImageCallback {
        public void imageLoaded(Drawable imageDrawable, ImageView imageView, String imageUrl);
    }
}
