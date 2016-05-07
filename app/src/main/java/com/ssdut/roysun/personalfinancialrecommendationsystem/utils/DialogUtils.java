package com.ssdut.roysun.personalfinancialrecommendationsystem.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.ssdut.roysun.personalfinancialrecommendationsystem.MD.activity.MainActivityMD;
import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.BaseActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.MemoAddActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.MemoMainActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.adapter.MemoListAdapter;
import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.MemoContent;
import com.ssdut.roysun.personalfinancialrecommendationsystem.service.SDrw;

/**
 * Created by roysun on 16/4/18.
 * 提供各种对话框样式
 */
public class DialogUtils {

    private static String fileName;

    public static void showExitDialog(final Context context, final int activityId) {
        if (context instanceof BaseActivity) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            String confrimStr = "";  //右按钮文案
            String cancelStr = "";  //左按钮文案
            builder.setTitle(R.string.exit_confirm);
            confrimStr = context.getString(R.string.exit_app);
            switch (activityId) {
                case BaseActivity.ACTIVITY_MAIN_MD:
                    cancelStr = context.getString(R.string.cancel);
                    break;
                case BaseActivity.ACTIVITY_JOURNAL_MAIN:
                    cancelStr = context.getString(R.string.exit_journal);
                    break;
                case BaseActivity.ACTIVITY_STOCK:
                    cancelStr = context.getString(R.string.exit_stock);
                    break;
                case BaseActivity.ACTIVITY_WEATHER:
                    cancelStr = context.getString(R.string.exit_weather);
                    break;
                case BaseActivity.ACTIVITY_TRANSLATION:
                    cancelStr = context.getString(R.string.exit_translation);
                    break;
                case BaseActivity.ACTIVITY_CAlCULATION:
                    cancelStr = context.getString(R.string.exit_calculation);
                    break;
                case BaseActivity.ACTIVITY_MEMO_MAIN:
                    cancelStr = context.getString(R.string.exit_memo);
                    break;
            }
            if (activityId == BaseActivity.ACTIVITY_MAIN_MD) {
                builder.setPositiveButton(confrimStr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((BaseActivity) context).exitApplication();
                    }
                });
                builder.setNeutralButton(cancelStr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            } else {
                builder.setPositiveButton(confrimStr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((BaseActivity) context).exitApplication();
                    }
                });

                builder.setNeutralButton(cancelStr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context, MainActivityMD.class);
                        context.startActivity(intent);
                        ((BaseActivity) context).finish();  //必须finish掉不然只是把MainActivity顶到栈顶但是CalculationActivity还在栈中，从MainActivity中返回后还是会跳到本Activity
                    }
                });
            }
            builder.create().show();
        }
    }

    public static void showExportDialog(final Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String confirmStr = context.getString(R.string.confirm);
        String cancelStr = context.getString(R.string.cancel);
        //导出备忘录条目
        fileName = (context instanceof MemoMainActivity) ? ((MemoMainActivity) context).getFileName("memoAll.txt") : ((MemoAddActivity) context).getFileName("memo.txt");
        builder.setMessage("确定后导出的文件名为" + fileName + "存放在SD/ssdut/personalfinancialsystemwithrecommendation/memo/txt/文件夹下");
        builder.setPositiveButton(confirmStr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String dir = "memo/txt/";
                String str = "";
                String line1;
                String line2;
                SDrw ic = null;
                if (context instanceof MemoMainActivity) {
                    ic = new SDrw(fileName, true, dir);
                    for (MemoContent _memoContent : MemoListAdapter.sMemoList) {
                        line1 = "---" + _memoContent.getYear() + "年" + _memoContent.getMonth() + "月" + _memoContent.getDay() + "日" + _memoContent.getTime() + "\r\n";
                        line2 = "---" + _memoContent.getContent() + "\r\n\r\n";
                        str += line1 + line2;
                    }
                } else if (context instanceof MemoAddActivity) {
                    ic = new SDrw(fileName, false, dir);
                    line1 = "---" + ((MemoAddActivity) context).mTime.getText().toString() + "\r\n";
                    line2 = "---" + ((MemoAddActivity) context).mMemoContent.getText().toString();
                    str = line1 + line2;
                } else {

                }
                ic.outWrite(str);
                ToastUtils.showMsg(context, context.getString(R.string.success_export));
            }
        });
        builder.setNeutralButton(cancelStr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder.create().cancel();
            }
        });
        builder.create().show();
    }

    public static void showDeleteDialog(final Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String confirmStr = context.getString(R.string.confirm);
        String cancelStr = context.getString(R.string.cancel);
        builder.setTitle(R.string.confirm_delete_memo_item);
        if (context instanceof MemoAddActivity) {
            builder.setPositiveButton(confirmStr, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (((MemoAddActivity) context).isUpdate) {
                        ((MemoAddActivity) context).mMemoDataHelper.deleteMemoInfo(((MemoAddActivity) context).mMemoItem.getId());
                    } else {
                        ((MemoAddActivity) context).mMemoContent.setText("");
                    }
                    ((MemoAddActivity) context).finish();
                }
            });
            builder.setNegativeButton(cancelStr, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    builder.create().cancel();
                }
            });
        }
        builder.create().show();
    }
}

