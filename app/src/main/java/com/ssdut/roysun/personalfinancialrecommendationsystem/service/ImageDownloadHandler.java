package com.ssdut.roysun.personalfinancialrecommendationsystem.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by roysun on 16/3/12.
 * 从网络上下载图片等耗时操作
 * （1）完成输入流到Bitmap的转换：HttpEntity->InputStream->ByteArrayOutputStream->Bitmap
 * 或（2）更简单的转换：HttpEntity->InputStream->Bitmap
 */
public class ImageDownloadHandler {

    /**
     * 对外暴露的下载图片接口
     * @param url 图片url
     * @return Bitmap 分时线图
     * @throws Exception
     */
    public Bitmap loadImageFromUrl(String url) throws Exception {
        HttpEntity httpEntity = getHttpEntity(url);
//        ByteArrayOutputStream imageBytesStream = getImageBytesStream(httpEntity);
//        return convertBytesToBitmap(imageBytesStream);
        return httpEntityToBitmap(httpEntity);
    }

    private Bitmap httpEntityToBitmap(HttpEntity httpEntity) throws IOException {
        InputStream is = httpEntity.getContent();
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        return bitmap;
    }

    private Bitmap convertBytesToBitmap(ByteArrayOutputStream baos) {
        byte[] imageArray = baos.toByteArray();
        return BitmapFactory.decodeByteArray(
                imageArray, 0, imageArray.length);
    }

    private ByteArrayOutputStream getImageBytesStream(HttpEntity entity)
            throws Exception {
        InputStream is = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            is = entity.getContent();
            byte[] buf = new byte[1024];
            int readBytes = -1;
            while ((readBytes = is.read(buf)) != -1) {  //从is中读至buf，未到is最后之前都是返回的读入的字节数，到最后时返回-1
                baos.write(buf, 0, readBytes);  //从buf写入baos，共计readBytes - 0个字节
            }
        } finally {
            if (baos != null) {
                baos.close();
            }
            if (is != null) {
                is.close();
            }
        }

        return baos;
    }

    private HttpEntity getHttpEntity(String url) throws Exception {
        final DefaultHttpClient client = new DefaultHttpClient();
        final HttpGet getRequest = new HttpGet(url);

        HttpResponse response = client.execute(getRequest);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_OK) { //状态码200即成功
            Log.e("PicShow", "Request URL failed, error code =" + statusCode);  //错误码
        }

        HttpEntity entity = response.getEntity();
        if (entity == null) {
            Log.e("PicShow", "HttpEntity is null");
        }

        return entity;
    }
}
