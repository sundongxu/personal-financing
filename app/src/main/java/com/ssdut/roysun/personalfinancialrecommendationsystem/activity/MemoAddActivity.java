package com.ssdut.roysun.personalfinancialrecommendationsystem.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.adapter.MemoListAdapter;
import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.MemoContent;
import com.ssdut.roysun.personalfinancialrecommendationsystem.db.manager.MemoManager;
import com.ssdut.roysun.personalfinancialrecommendationsystem.service.DongHuaYanChi;
import com.ssdut.roysun.personalfinancialrecommendationsystem.service.SDrw;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.DialogUtils;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.PicUtils;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.TimeUtils;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.ToastUtils;

import java.io.File;

/**
 * Created by roysun on 16/3/12.
 * 实际是两个页面(靠isUpdate标志位区分或者intent有无id)，因通用性复用，但界面显示控件有所差异
 * （1）新建一个全新备忘录item，Activity显示新建item页面
 * （2）点击主界面备忘录list跳转至某一个item，显示选中item的备忘录内容
 */
public class MemoAddActivity extends PicBaseActivity implements View.OnClickListener {

    public static final String TAG = "MemoAddActivity";

    public EditText mMemoContent;  //备忘录输入框
    private LinearLayout mBgColorSelectArea, mTextSizeSelectArea;  //更换颜色和字体LinerLayout
    public TextView mTime;  //顶部日期
    private ImageButton mIBPicChoose, mIBColorChoose;
    public MemoManager mMemoDataHelper;
    public Boolean isUpdate;  //新建false，修改true
    public MemoContent mMemoItem;  //当前选中的备忘录条目
    private String mUpdateString;

    //字体大小
    private float mTextSize = 24;
    private float mUpdateTextSize = 0;
    private float mSizeFloats[] = new float[]{20, 24, 29, 34};

    //背景颜色
    private int mBgColorId = R.drawable.bw_new_et_bg_1;
    private int mUpdateBgColorId = R.drawable.bw_new_et_bg_1;
    private int mIds[] = new int[]{R.drawable.bw_new_et_bg_1, R.drawable.bw_new_et_bg_2, R.drawable.bw_new_et_bg_3, R.drawable.bw_new_et_bg_4, R.drawable.bw_new_et_bg_5};

    private Handler mHandler;

    private String mPicPath = "";// 文件路径

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_add);
        initView();
        initData();
        initUpdate();
    }

    @Override
    protected void initView() {
        super.initView();
        mTime = (TextView) this.findViewById(R.id.tv_topbar_time);
        mMemoContent = (EditText) this.findViewById(R.id.et_main_content);
        mMemoContent.setOnClickListener(this);
        mMemoContent.setTextSize(mSizeFloats[1]);
        mIBPicChoose = (ImageButton) this.findViewById(R.id.ib_pic_choose);
        mIBPicChoose.setOnClickListener(this);
        mIBColorChoose = (ImageButton) this.findViewById(R.id.ib_color_bg_choose);
        mIBColorChoose.setOnClickListener(this);
        initBGColor();
        initTextSize();
    }

    @Override
    protected void initData() {
        super.initData();
        isUpdate = false;
        mMemoItem = new MemoContent();
        mHandler = new Handler();
        mMemoDataHelper = new MemoManager(this);
    }

    /**
     * 初始化背景颜色
     */
    private void initBGColor() {
        mBgColorSelectArea = (LinearLayout) this.findViewById(R.id.ll_bg_color_select);
        mBgColorSelectArea.setVisibility(View.GONE);
        RelativeLayout _color_1 = (RelativeLayout) this.findViewById(R.id.rl_bg_color_1);
        RelativeLayout _color_2 = (RelativeLayout) this.findViewById(R.id.rl_bg_color_2);
        RelativeLayout _color_3 = (RelativeLayout) this.findViewById(R.id.rl_bg_color_3);
        RelativeLayout _color_4 = (RelativeLayout) this.findViewById(R.id.rl_bg_color_4);
        RelativeLayout _color_5 = (RelativeLayout) this.findViewById(R.id.rl_bg_color_5);
        RelativeLayout _colors[] = new RelativeLayout[]{_color_1, _color_2, _color_3, _color_4, _color_5};
        for (RelativeLayout color : _colors) {
            color.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.rl_bg_color_1:
                            mBgColorId = mIds[0];
                            break;
                        case R.id.rl_bg_color_2:
                            mBgColorId = mIds[1];
                            break;
                        case R.id.rl_bg_color_3:
                            mBgColorId = mIds[2];
                            break;
                        case R.id.rl_bg_color_4:
                            mBgColorId = mIds[3];
                            break;
                        case R.id.rl_bg_color_5:
                            mBgColorId = mIds[4];
                            break;
                    }
                    mMemoContent.setBackgroundResource(mBgColorId);
                    DongHuaYanChi.dongHuaEnd(mBgColorSelectArea, MemoAddActivity.this, mHandler, R.anim.picpush_right_out, 300);
                }
            });
        }
    }

    /**
     * 初始化文本大小
     */
    private void initTextSize() {
        mTextSizeSelectArea = (LinearLayout) this.findViewById(R.id.ll_textsize_select);
        mTextSizeSelectArea.setVisibility(View.GONE);
        RelativeLayout _size_1 = (RelativeLayout) this.findViewById(R.id.rl_textsize_1);
        RelativeLayout _size_2 = (RelativeLayout) this.findViewById(R.id.rl_textsize_2);
        RelativeLayout _size_3 = (RelativeLayout) this.findViewById(R.id.rl_textsize_3);
        RelativeLayout _size_4 = (RelativeLayout) this.findViewById(R.id.rl_textsize_4);
        RelativeLayout _sizes[] = new RelativeLayout[]{_size_1, _size_2, _size_3, _size_4};
        for (RelativeLayout size : _sizes) {
            size.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.rl_textsize_1:
                            mTextSize = mSizeFloats[0];
                            break;
                        case R.id.rl_textsize_2:
                            mTextSize = mSizeFloats[1];
                            break;
                        case R.id.rl_textsize_3:
                            mTextSize = mSizeFloats[2];
                            break;
                        case R.id.rl_textsize_4:
                            mTextSize = mSizeFloats[3];
                            break;
                    }
                    mMemoContent.setTextSize(mTextSize);
                    DongHuaYanChi.dongHuaEnd(mTextSizeSelectArea, MemoAddActivity.this, mHandler, R.anim.jz_menu_down, 300);
                }
            });
        }
    }

    /**
     * 初始化更新信息
     */
    private void initUpdate() {
        Intent intent = this.getIntent();
        if (intent.hasExtra("id")) {
            int id = intent.getIntExtra("id", 0);
            for (MemoContent _memoContent : MemoListAdapter.sMemoList) {
                if (_memoContent.getId() == id) {
                    mTime.setText(_memoContent.getYear() + "年" + _memoContent.getMonth() + "月" + _memoContent.getDay() + "日" + " " + _memoContent.getTime());
                    isUpdate = true;
                    mUpdateString = _memoContent.getContent();
                    mMemoContent.setText(mUpdateString);
                    Editable ea = mMemoContent.getText();
                    Selection.setSelection((Spannable) ea, ea.length());  //设置光标在文字末尾
                    //初始化文字的大小
                    mTextSize = _memoContent.getSize();
                    mUpdateTextSize = mTextSize;
                    if (mTextSize == 0) {
                        mTextSize = 24;
                        mUpdateTextSize = 24;
                    }
                    mMemoContent.setTextSize(mTextSize);
                    //初始化图片 存在图片就设置图片
                    if (_memoContent.getPic() != null && !_memoContent.getPic().equals("") && !SDrw.getSDPath().equals("")) {
                        mPicPath = _memoContent.getPic();
                        File picFile = new File(mPicPath);
                        mIBPicChoose.setImageBitmap(PicUtils.decodeFileAndCompress(picFile));
                    } else {
                        _memoContent.setPic("");//防止修改时空指针异常
                    }

                    //初始化背景颜色
                    mBgColorId = _memoContent.getColor();
                    mUpdateBgColorId = mBgColorId;
                    mMemoContent.setBackgroundResource(mUpdateBgColorId);
                    mMemoItem = _memoContent;
                    return;
                }
            }
        } else {
            mTime.setText(TimeUtils.getYear() + "年" + TimeUtils.getMonth() + "月" + TimeUtils.getDay() + "日" + " " + TimeUtils.getTime());
        }
    }

    @Override
    protected void onResume() {
        overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_pic_choose:
                if (isUpdate) {
                    if (mMemoItem.getPic() != null && new File(mMemoItem.getPic()).exists()) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        Uri uri = Uri.parse("file://" + mMemoItem.getPic());
                        intent.setDataAndType(uri, "image/*");
                        startActivity(intent);
                    } else {
                        choosePic(this);
                    }
                } else {
                    choosePic(this);
                }
                break;
            case R.id.ib_color_bg_choose:
                if (mBgColorSelectArea.isShown()) {
                    DongHuaYanChi.dongHuaEnd(mBgColorSelectArea, MemoAddActivity.this, mHandler, R.anim.picpush_right_out, 300);
                } else {
                    mBgColorSelectArea.setVisibility(View.VISIBLE);
                    mBgColorSelectArea.setAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
                }
                break;
            case R.id.et_main_content:
                if (mBgColorSelectArea.isShown()) {
                    DongHuaYanChi.dongHuaEnd(mBgColorSelectArea, MemoAddActivity.this, mHandler, R.anim.picpush_right_out, 300);
                }
                if (mTextSizeSelectArea.isShown()) {
                    DongHuaYanChi.dongHuaEnd(mTextSizeSelectArea, MemoAddActivity.this, mHandler, R.anim.jz_menu_down, 300);
                }
                break;
        }
    }

    /*
     * 存储备忘录item到数据库
	 * */
    private void saveToDB() {
        MemoContent _memoContent = new MemoContent();
        int _year = TimeUtils.getYear();
        int _month = TimeUtils.getMonth();
        int _week = TimeUtils.getWeek();
        int _day = TimeUtils.getDay();
        String _time = TimeUtils.getTime();
        String _content = mMemoContent.getText().toString().trim();
        int _color = mBgColorId;
        if (_content.equals(null) || _content.equals("")) {
            ToastUtils.showMsg(this, "输入不能为空");
            return;
        }
        //一个备忘录item共有10个属性需要保存
        _memoContent.setYear(_year);
        _memoContent.setMonth(_month);
        _memoContent.setWeek(_week);
        _memoContent.setDay(_day);
        _memoContent.setTime(_time);
        _memoContent.setContent(_content);
        _memoContent.setColor(_color);
        _memoContent.setSize(mTextSize);
        if (!isUpdate) {
            //新建，不需要保存id，因为是直接添加到数据库表的末尾
            if (!"".equals(mPicPath) && !mPicPath.equals(null)) {
                _memoContent.setPic(mPicPath);
            } else {
                _memoContent.setPic("");
            }
            mMemoDataHelper.addMemoInfo(_memoContent);  //不需要id
            ToastUtils.showMsg(this, "存储成功");
        } else {
            //修改，_content实际已是修改后的内容
            if (!mUpdateString.equals(_content) ||
                    mUpdateBgColorId != mBgColorId ||
                    mUpdateTextSize != mTextSize ||
                    !mPicPath.equals(mMemoItem.getPic())) {
                if (!mPicPath.equals(mMemoItem.getPic())) {
                    _memoContent.setPic(mPicPath);
                }
                mMemoDataHelper.updateMemoInfo(_memoContent, mMemoItem.getId());  //需要id，根据id在数据库表中查询
                ToastUtils.showMsg(this, "修改成功");
            }
        }
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 100, 0, "字体大小");
        menu.add(0, 200, 0, "删除该条");
        menu.add(0, 300, 0, "导出文本");
        menu.add(0, 400, 0, "返回列表");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 100:
                if (mTextSizeSelectArea.isShown()) {
                    DongHuaYanChi.dongHuaEnd(mTextSizeSelectArea, MemoAddActivity.this, mHandler, R.anim.jz_menu_down, 300);
                } else {
                    mTextSizeSelectArea.setVisibility(View.VISIBLE);
                    mTextSizeSelectArea.setAnimation(AnimationUtils.loadAnimation(this, R.anim.jz_menu_up));
                }
                break;
            case 200:
                DialogUtils.showDeleteDialog(this);
                break;
            case 300:
                if (mMemoContent.getText().toString().trim().length() < 1) {
                    ToastUtils.showMsg(this, "导出内容不能为空");
                    return false;
                }
                DialogUtils.showExportDialog(this);
                break;
            case 400:
                finish();  //back to MemoMainActivity
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //定义了返回键按下保存备忘录
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: {
                if (mBgColorSelectArea.isShown() || mTextSizeSelectArea.isShown()) {
                    //当选择颜色和选择字体大小界面显示时按下返回键先关闭显示的界面
                    if (mBgColorSelectArea.isShown()) {
                        DongHuaYanChi.dongHuaEnd(mBgColorSelectArea, MemoAddActivity.this, mHandler, R.anim.picpush_right_out, 300);
                    }
                    if (mTextSizeSelectArea.isShown()) {
                        DongHuaYanChi.dongHuaEnd(mTextSizeSelectArea, MemoAddActivity.this, mHandler, R.anim.jz_menu_down, 300);
                    }
                    return false;
                }
                //如果内容有字符或有图片就存储到数据库中
                if (!"".equals(mMemoContent.getText().toString().trim()) || !"".equals(mPicPath)) {
                    saveToDB();
                    finish();
                    return false;
                } else {
                    if (isUpdate) {
                        //如果当前状态为更新且内容为空则删除该条
                        mMemoDataHelper.deleteMemoInfo(mMemoItem.getId());
                    }
                    finish();
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /*
     * 选择图片的回传处理
     * 这里有bug，保存以后再进入该备忘录条目就无法重新选择相片了，imageView来展示是不是更好？
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        File _file;
        Bitmap _bmp;
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PHOTO_FROM_CAMERA:
                    //获取拍摄的文件
                    mPicPath = captureFile.getAbsolutePath();
                    _file = new File(mPicPath);
                    _bmp = PicUtils.decodeFileAndCompress(_file);
                    mIBPicChoose.setImageBitmap(_bmp);  //选择图片按钮显示该图
                    break;
                case PHOTO_FROM_DATA:
                    //获取从图库选择的文件
                    Uri uri = data.getData();
                    String scheme = uri.getScheme();
                    if (scheme.equalsIgnoreCase("file")) {
                        mPicPath = uri.getPath();  //从uri中获得路径
                        _file = new File(mPicPath);
                        _bmp = PicUtils.decodeFileAndCompress(_file);  //转换图片大小
                        mIBPicChoose.setImageBitmap(_bmp);
                    } else if (scheme.equalsIgnoreCase("content")) {
                        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                        if (cursor != null) {
                            cursor.moveToFirst();
                            mPicPath = cursor.getString(1);
                            _file = new File(mPicPath);
                            _bmp = PicUtils.decodeFileAndCompress(_file);
                            mIBPicChoose.setImageBitmap(_bmp);
                        }
                    }
                    break;
            }
            //存放照片的路径
            String savePath = SDrw.SDPATH + "memo/imgcache/";
            mPicPath = PicUtils.compressPic(mPicPath, savePath);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
