package com.ssdut.roysun.personalfinancialrecommendationsystem.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.ssdut.roysun.personalfinancialrecommendationsystem.service.SDrw;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.ToastUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by roysun on 16/3/13.
 * 含图片选择（实时拍摄或相册中选）的Activity的基类，定义公共方法、常量...
 */

public class PicBaseActivity extends BaseActivity {

    public static final String TAG = "PicBaseActivity";

    public static final int PHOTO_FROM_CAMERA = 1010;  // 拍摄照片
    public static final int PHOTO_FROM_DATA = 1020;  // 从SD卡（图库）中得到照片
    public File PHOTO_DIR;  // 拍摄照片存储的文件夹路径
    public File captureFile;  // 拍摄的照片文件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_base_pic);
    }

    public void choosePic(final Context context) {
        final Context dialogContext = new ContextThemeWrapper(context, android.R.style.Theme_Light);
        if (context instanceof JournalAddActivity) {
            PHOTO_DIR = new File(SDrw.SDPATH + "journal/imgcache/");
        } else if (context instanceof MemoAddActivity) {
            PHOTO_DIR = new File(SDrw.SDPATH + "memo/imgcache/");
        } else if (context instanceof RegisterActivity) {
            PHOTO_DIR = new File(SDrw.SDPATH + "user/imgcache/");
        }
        if (!PHOTO_DIR.exists()) {
            PHOTO_DIR.mkdirs();
        }
        //生成当前目录图片不可见的标志文件
        File _noMediaFile = new File(PHOTO_DIR, ".nomedia");
        if (!_noMediaFile.exists()) {
            try {
                _noMediaFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String[] choices;
        choices = new String[2];
        choices[0] = "                     相机拍摄"; // 拍照
        choices[1] = "                     本地相册"; // 从相册中选择
        final ListAdapter adapter = new ArrayAdapter<String>(dialogContext, android.R.layout.simple_list_item_1, choices);
        final AlertDialog.Builder builder = new AlertDialog.Builder(dialogContext);
        builder.setTitle("                    添加图片");
        builder.setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                switch (which) {
                    case 0:
                        //拍摄实时照片
                        String _status = Environment.getExternalStorageState();
                        if (_status.equals(Environment.MEDIA_MOUNTED)) {
                            // 判断是否有SD卡
                            Intent _intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            captureFile = new File(PHOTO_DIR, getFileName("jpg"));
                            try {
                                captureFile.createNewFile();
                                _intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(captureFile));// 将拍摄的照片信息存到captureFile中
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            startActivityForResult(_intent, PHOTO_FROM_CAMERA);  //用户点击了从照相机获取,requestCode = PHOTO_FROM_CAMERA
                        } else {
                            ToastUtils.showMsg(context, "没有SD卡");
                        }
                        break;
                    case 1:
                        //从相册中去获取
                        Intent _intent = new Intent();
                    /* 开启Pictures画面Type设定为image */
                        _intent.setType("image/*");
                    /* 使用Intent.ACTION_GET_CONTENT这个Action */
                        _intent.setAction(Intent.ACTION_GET_CONTENT);
                    /* 取得相片后返回本画面 */
                        startActivityForResult(_intent, PHOTO_FROM_DATA);
                        break;
                }
            }
        });
        builder.create().show();
    }
}
