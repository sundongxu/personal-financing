package com.ssdut.roysun.personalfinancialrecommendationsystem.adapter.info;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.UserInfoActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.User;

/**
 * Created by roysun on 16/5/13.
 * 个人信息List适配器
 * 存在一个bug：点击密保答案后再点击问题的EditText会crash
 */
public class UserInfoListAdapter extends InfoListBaseAdapter {

    public static final String TAG = "UserInfoListAdapter";

    private boolean mIsConfirmPasswordHidden = true;
    private boolean mIsConfirmQuestionHidden = true;
    private boolean mIsConfirmRechargeHidden = true;

    private InputMethodManager mInputMethodManager;

    private String mPasswordUpdate;
    private String mSecurityQuestionUpdate;
    private String mSecurityAnswerUpdate;
    private String mBalanceAdd;

    public UserInfoListAdapter(Context context) {
        mContext = context;
        mInputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        initInfoList();
    }

    @Override
    public void initInfoList() {
        if (mInfoIconList != null) {
            mInfoIconList.clear();
            mInfoIconList.add(R.drawable.icon_username_hint);
            mInfoIconList.add(R.drawable.icon_password_hint);
//            mInfoIconList.add(R.drawable.icon_password_hint_input);
            mInfoIconList.add(R.drawable.icon_balance_hint);
            mInfoIconList.add(R.drawable.icon_security_question_hint);
//            mInfoIconList.add(R.drawable.icon_security_question_hint_input);
//            mInfoIconList.add(R.drawable.icon_security_answer_hint_input);
            mInfoIconList.add(R.drawable.icon_create_time_hint);
            mInfoIconList.add(R.drawable.icon_update_time_hint);
            mInfoIconList.add(R.drawable.icon_special_account_hint);
        }

        if (mInfoTitleList != null) {
            mInfoTitleList.clear();
            mInfoTitleList.add("用户名");
            mInfoTitleList.add("密码");  // 可修改项 修改按钮 -> 确认按钮 toast提示
//            mInfoTitleList.add("重置密码");  // 开始不可见
            mInfoTitleList.add("账户余额");
//            mInfoTitleList.add("账户充值");
            mInfoTitleList.add("密保问题");  // 可修改项 - 密保问题、答案一起修改 修改按钮 -> 确认按钮
//            mInfoTitleList.add("重置问题");  // 开始不可见
//            mInfoTitleList.add("重置答案");  // 开始不可见
            mInfoTitleList.add("注册时间");
            mInfoTitleList.add("更新时间");
            mInfoTitleList.add("管理员");
        }

        if (mInfoValueList != null) {
            mInfoValueList.clear();
            if (mContext instanceof UserInfoActivity) {
                User _curUser = ((UserInfoActivity) mContext).getUserManager().getCurUser();
                mInfoValueList.add(_curUser.getName());
                String asterisk = "";
                for (int i = 0; i < _curUser.getPassword().length(); i++) {
                    asterisk = asterisk + "*";
                }
                mInfoValueList.add(asterisk);
                mInfoValueList.add(String.valueOf(_curUser.getBalance()));
                mInfoValueList.add(_curUser.getQuestion());
                mInfoValueList.add(_curUser.getCreateTime());
                mInfoValueList.add(_curUser.getUpdateTime());
                mInfoValueList.add(_curUser.isSpecial() == 1 ? "是" : "否");
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mIsConfirmPasswordHidden && mIsConfirmQuestionHidden && mIsConfirmRechargeHidden) {
            // 正常情况（1），7个title
            switch (position) {
                case 0:
                    mItemType = INFO_USER_NAME;
                    break;
                case 1:
                    mItemType = INFO_USER_PASSWORD;
                    break;
                case 2:
                    mItemType = INFO_USER_BALANCE;
                    break;
                case 3:
                    mItemType = INFO_USER_SECURITY_QUESTION;
                    break;
                case 4:
                    mItemType = INFO_USER_CREATE_TIME;
                    break;
                case 5:
                    mItemType = INFO_USER_UPDATE_TIME;
                    break;
                case 6:
                    mItemType = INFO_USER_IS_SPECIAL;
                    break;
            }
        } else if (mIsConfirmQuestionHidden && mIsConfirmRechargeHidden) {
            // 情况（2），在position == 2增加 重置密码 item
            switch (position) {
                case 0:
                    mItemType = INFO_USER_NAME;
                    break;
                case 1:
                    mItemType = INFO_USER_PASSWORD;
                    break;
                case 2:
                    mItemType = INFO_USER_PASSWORD_RESET;
                    break;
                case 3:
                    mItemType = INFO_USER_BALANCE;
                    break;
                case 4:
                    mItemType = INFO_USER_SECURITY_QUESTION;
                    break;
                case 5:
                    mItemType = INFO_USER_CREATE_TIME;
                    break;
                case 6:
                    mItemType = INFO_USER_UPDATE_TIME;
                    break;
                case 7:
                    mItemType = INFO_USER_IS_SPECIAL;
                    break;
            }
        } else if (mIsConfirmPasswordHidden && mIsConfirmRechargeHidden) {
            // 情况（3），在position == 4、5处分别增加重置密保问题、重置密保答案 两个item
            switch (position) {
                case 0:
                    mItemType = INFO_USER_NAME;
                    break;
                case 1:
                    mItemType = INFO_USER_PASSWORD;
                    break;
                case 2:
                    mItemType = INFO_USER_BALANCE;
                    break;
                case 3:
                    mItemType = INFO_USER_SECURITY_QUESTION;
                    break;
                case 4:
                    mItemType = INFO_USER_SECURITY_QUESTION_RESET;
                    break;
                case 5:
                    mItemType = INFO_USER_SECURITY_ANSWER_RESET;
                    break;
                case 6:
                    mItemType = INFO_USER_CREATE_TIME;
                    break;
                case 7:
                    mItemType = INFO_USER_UPDATE_TIME;
                    break;
                case 8:
                    mItemType = INFO_USER_IS_SPECIAL;
                    break;
            }
        } else {
            // 情况（4），在position == 3处增加充值item
            switch (position) {
                case 0:
                    mItemType = INFO_USER_NAME;
                    break;
                case 1:
                    mItemType = INFO_USER_PASSWORD;
                    break;
                case 2:
                    mItemType = INFO_USER_BALANCE;
                    break;
                case 3:
                    mItemType = INFO_USER_BALANCE_RECHARGE;
                    break;
                case 4:
                    mItemType = INFO_USER_SECURITY_QUESTION;
                    break;
                case 5:
                    mItemType = INFO_USER_CREATE_TIME;
                    break;
                case 6:
                    mItemType = INFO_USER_UPDATE_TIME;
                    break;
                case 7:
                    mItemType = INFO_USER_IS_SPECIAL;
                    break;
            }
        }
        return mItemType;
    }

    @Override
    public int getItemCount() {
//        if (mIsConfirmPasswordHidden && mIsConfirmQuestionHidden) {
//            return super.getItemCount();
//        } else if (mIsConfirmQuestionHidden) {
//            return super.getItemCount() + 1;
//        } else {
//            return super.getItemCount() + 2;
//        }
        if (mIsConfirmPasswordHidden && mIsConfirmQuestionHidden && mIsConfirmRechargeHidden) {
            return super.getItemCount();
        } else if (mIsConfirmPasswordHidden && mIsConfirmRechargeHidden) {
            return super.getItemCount() + 2;
        } else {
            // mIsConfirmPasswordHidden && mIsConfirmQuestionHidden
            // mIsConfirmQuestionHidden && mIsConfirmRechargeHidden
            return super.getItemCount() + 1;
        }
    }

    @Override
    public InfoItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }

//    @Override
//    public void onBindViewHolder(final InfoItemViewHolder holder, final int position) {
//        final int itemType = getItemViewType(position);
//        if (itemType == INFO_USER_PASSWORD || itemType == INFO_USER_SECURITY_QUESTION ) {
//            holder.mItemArea.setBackgroundResource(R.drawable.item_click_ripple);
//            holder.mItemArea.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // do nothing just for ripple
//                }
//            });
//            holder.mItemIcon.setImageResource(mInfoIconList.get(position));
//            holder.mItemTitleText.setText(mInfoTitleList.get(position));
//            holder.mItemValueText.setText(mInfoValueList.get(position));
//            holder.mBtnItemUpdate.setVisibility(View.VISIBLE);
//            holder.mBtnItemUpdate.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);  // 点击确认按钮后关闭软键盘
//                    if (itemType == INFO_USER_PASSWORD && mIsConfirmPasswordHidden && mIsConfirmQuestionHidden) {
//                        mIsConfirmPasswordHidden = false;
//                        notifyItemInserted(2);
////                        holder.mItemIcon.setImageResource(R.drawable.icon_password_hint_input);
////                        holder.mItemTitleText.setText("重置密码");
//                        holder.mBtnItemUpdate.setText("确  认");
//
//                    } else if (itemType == INFO_USER_PASSWORD && !mIsConfirmPasswordHidden) {
//                        ToastUtils.showMsg(mContext, "你点击了确认按钮！");
//                        mIsConfirmPasswordHidden = true;
//                        notifyItemRemoved(2);
//                        holder.mBtnItemUpdate.setText("修  改");
//                        if (mContext instanceof UserInfoActivity) {
//                            if (mPasswordUpdate != null && !mPasswordUpdate.equals(""))
//                                ((UserInfoActivity) mContext).resetPassword(mPasswordUpdate);
//                        }
//                        mPasswordUpdate = "";
//
//                    } else if (itemType == INFO_USER_SECURITY_QUESTION && mIsConfirmQuestionHidden && mIsConfirmPasswordHidden) {
//                        mIsConfirmQuestionHidden = false;
//                        notifyItemInserted(4);
//                        notifyItemInserted(5);
////                        holder.mItemIcon.setImageResource(R.drawable.icon_security_question_hint_input);
////                        holder.mItemTitleText.setText("重置问题");
//                        holder.mBtnItemUpdate.setText("确  认");
//
//                    } else if (itemType == INFO_USER_SECURITY_QUESTION && !mIsConfirmQuestionHidden) {
//                        // itemType == INFO_USER_SECURITY_QUESTION && !mIsBtnConfirmHidden
//                        // 不能直接else，两个按钮一起按下会crash
//                        ToastUtils.showMsg(mContext, "你又点击了确认按钮！");
//                        mIsConfirmQuestionHidden = true;
//                        notifyItemRemoved(4);
//                        notifyItemRemoved(4);
////                        resetCardList();
//                        holder.mBtnItemUpdate.setText("修  改");
//                        if (mContext instanceof UserInfoActivity) {
//                            if (mSecurityQuestionUpdate != null && !mSecurityQuestionUpdate.equals("")
//                                    && mSecurityAnswerUpdate != null && !mSecurityAnswerUpdate.equals(""))
//                                ((UserInfoActivity) mContext).resetSecurityInfo(mSecurityQuestionUpdate, mSecurityAnswerUpdate);
//                        }
//                        mSecurityQuestionUpdate = "";
//                        mSecurityAnswerUpdate = "";
//                    }
//                }
//            });
//        } else if (itemType == INFO_USER_PASSWORD_RESET) {
//            holder.mItemArea.setBackgroundResource(R.drawable.item_click_ripple);
//            holder.mItemArea.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
//            holder.mItemIcon.setImageResource(R.drawable.icon_password_hint_input);
////            holder.mItemTitleText.setText("重置密码");
//            holder.mItemTitleText.setVisibility(View.GONE);
//            holder.mItemValueText.setVisibility(View.GONE);
//            holder.mItemUpdateView.setVisibility(View.VISIBLE);
//            holder.mItemUpdateView.setLabel("重置密码");
//            holder.mItemUpdateView.setText("");
//            holder.mItemUpdateView.getEditText().setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
//            holder.mItemUpdateView.getEditText().setTextColor(R.color.black);
//            holder.mItemUpdateView.getEditText().addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    mPasswordUpdate = s.toString();
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//
//                }
//            });
//        } else if (itemType == INFO_USER_SECURITY_QUESTION_RESET) {
//            holder.mItemArea.setBackgroundResource(R.drawable.item_click_ripple);
//            holder.mItemArea.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
//            holder.mItemIcon.setImageResource(R.drawable.icon_security_question_hint_input);
//            holder.mItemTitleText.setText("重置密保问题");
//            holder.mItemTitleText.setVisibility(View.GONE);
//            holder.mItemValueText.setVisibility(View.GONE);
//            holder.mItemUpdateView.setVisibility(View.VISIBLE);
//            holder.mItemUpdateView.setLabel("重置密保问题");
//            holder.mItemUpdateView.setText("");
//            holder.mItemUpdateView.getEditText().setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
//            holder.mItemUpdateView.getEditText().setTextColor(R.color.black);
//            holder.mItemUpdateView.getEditText().addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    mSecurityQuestionUpdate = s.toString();
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//                }
//            });
//        } else if (itemType == INFO_USER_SECURITY_ANSWER_RESET) {
//            holder.mItemArea.setBackgroundResource(R.drawable.item_click_ripple);
//            holder.mItemArea.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
//            holder.mItemIcon.setImageResource(R.drawable.icon_security_answer_hint_input);
//            holder.mItemTitleText.setText("重置密保答案");
//            holder.mItemTitleText.setVisibility(View.GONE);
//            holder.mItemValueText.setVisibility(View.GONE);
//            holder.mItemUpdateView.setVisibility(View.VISIBLE);
//            holder.mItemUpdateView.setLabel("重置密保答案");
//            holder.mItemUpdateView.setText("");
//            holder.mItemUpdateView.getEditText().setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
//            holder.mItemUpdateView.getEditText().setTextColor(R.color.black);
//            holder.mItemUpdateView.getEditText().addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    mSecurityAnswerUpdate = s.toString();
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//                }
//            });
//        } else {
//            holder.mItemArea.setBackgroundResource(R.drawable.item_click_ripple);
//            holder.mItemArea.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // do nothing just for ripple
//                }
//            });
//            int realPos = position;
//            if (realPos == 0 || realPos == 1) {
//                holder.mItemIcon.setImageResource(mInfoIconList.get(realPos));
//                holder.mItemTitleText.setText(mInfoTitleList.get(realPos));
//                holder.mItemValueText.setText(mInfoValueList.get(realPos));
//            } else {
//                if (!mIsConfirmQuestionHidden && mIsConfirmPasswordHidden) {
//                    realPos = realPos - 2;
//                } else if (mIsConfirmQuestionHidden && !mIsConfirmPasswordHidden) {
//                    realPos = realPos - 1;
//                } else {
//                    // to do nothing
//                }
//                holder.mItemIcon.setImageResource(mInfoIconList.get(realPos));
//                holder.mItemTitleText.setText(mInfoTitleList.get(realPos));
//                holder.mItemValueText.setText(mInfoValueList.get(realPos));
//            }
//
//        }
//    }

    @Override
    public void onBindViewHolder(final InfoItemViewHolder holder, final int position) {
        final int itemType = getItemViewType(position);
        if (itemType == INFO_USER_PASSWORD || itemType == INFO_USER_SECURITY_QUESTION || itemType == INFO_USER_BALANCE) {
            holder.mItemIcon.setImageResource(mInfoIconList.get(position));
            holder.mItemTitleText.setText(mInfoTitleList.get(position));
            holder.mItemValueText.setText(mInfoValueList.get(position));
            if (itemType == INFO_USER_BALANCE) {
                holder.mBtnItemUpdate.setText("充  值");
            }
            holder.mBtnItemUpdate.setVisibility(View.VISIBLE);
            holder.mBtnItemUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);  // 点击确认按钮后关闭软键盘
                    if (itemType == INFO_USER_PASSWORD && mIsConfirmPasswordHidden && mIsConfirmQuestionHidden & mIsConfirmRechargeHidden) {
                        mIsConfirmPasswordHidden = false;
                        notifyItemInserted(2);
                        holder.mBtnItemUpdate.setText("确  认");

                    } else if (itemType == INFO_USER_PASSWORD && !mIsConfirmPasswordHidden && mIsConfirmQuestionHidden && mIsConfirmRechargeHidden) {
                        mIsConfirmPasswordHidden = true;
                        notifyItemRemoved(2);
                        holder.mBtnItemUpdate.setText("修  改");
                        if (mContext instanceof UserInfoActivity) {
                            if (mPasswordUpdate != null && !mPasswordUpdate.equals(""))
                                ((UserInfoActivity) mContext).resetPassword(mPasswordUpdate);
                        }
                        mPasswordUpdate = "";

                    } else if (itemType == INFO_USER_SECURITY_QUESTION && mIsConfirmQuestionHidden && mIsConfirmRechargeHidden && mIsConfirmPasswordHidden) {
                        mIsConfirmQuestionHidden = false;
                        notifyItemInserted(4);
                        notifyItemInserted(5);
                        holder.mBtnItemUpdate.setText("确  认");

                    } else if (itemType == INFO_USER_SECURITY_QUESTION && !mIsConfirmQuestionHidden && mIsConfirmRechargeHidden && mIsConfirmPasswordHidden) {
                        // itemType == INFO_USER_SECURITY_QUESTION && !mIsBtnConfirmHidden
                        // 不能直接else，两个按钮一起按下会crash
                        mIsConfirmQuestionHidden = true;
                        notifyItemRemoved(4);
                        notifyItemRemoved(4);
                        holder.mBtnItemUpdate.setText("修  改");
                        if (mContext instanceof UserInfoActivity) {
                            if (mSecurityQuestionUpdate != null && !mSecurityQuestionUpdate.equals("")
                                    && mSecurityAnswerUpdate != null && !mSecurityAnswerUpdate.equals(""))
                                ((UserInfoActivity) mContext).resetSecurityInfo(mSecurityQuestionUpdate, mSecurityAnswerUpdate);
                        }
                        mSecurityQuestionUpdate = "";
                        mSecurityAnswerUpdate = "";
                    } else if (itemType == INFO_USER_BALANCE && mIsConfirmRechargeHidden && mIsConfirmPasswordHidden && mIsConfirmQuestionHidden) {
                        mIsConfirmRechargeHidden = false;
                        notifyItemInserted(3);
                        holder.mBtnItemUpdate.setText("确  认");
                    } else if (itemType == INFO_USER_BALANCE && !mIsConfirmRechargeHidden && mIsConfirmPasswordHidden && mIsConfirmQuestionHidden) {
                        mIsConfirmRechargeHidden = true;
                        notifyItemRemoved(3);
                        holder.mBtnItemUpdate.setText("充  值");
                        if (mContext instanceof UserInfoActivity) {
                            // 充值函数，UserInfoActivity中定义
                            if (mBalanceAdd != null && !mBalanceAdd.equals("")) {
                                ((UserInfoActivity) mContext).recharge(mBalanceAdd);
                            }
                        }
                        mBalanceAdd = "";
                    }
                }
            });
        } else if (itemType == INFO_USER_PASSWORD_RESET) {
            holder.mItemIcon.setImageResource(R.drawable.icon_password_hint_input);
//            holder.mItemTitleText.setText("重置密码");
            holder.mItemTitleText.setVisibility(View.GONE);
            holder.mItemValueText.setVisibility(View.GONE);
            holder.mItemUpdateView.setVisibility(View.VISIBLE);
            holder.mItemUpdateView.setLabel("重置密码");
            holder.mItemUpdateView.setText("");
            holder.mItemUpdateView.getEditText().setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            holder.mItemUpdateView.getEditText().setTextColor(R.color.black);
            holder.mItemUpdateView.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mPasswordUpdate = s.toString();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        } else if (itemType == INFO_USER_SECURITY_QUESTION_RESET) {
            holder.mItemIcon.setImageResource(R.drawable.icon_security_question_hint_input);
            holder.mItemTitleText.setText("重置密保问题");
            holder.mItemTitleText.setVisibility(View.GONE);
            holder.mItemValueText.setVisibility(View.GONE);
            holder.mItemUpdateView.setVisibility(View.VISIBLE);
            holder.mItemUpdateView.setLabel("重置密保问题");
            holder.mItemUpdateView.setText("");
            holder.mItemUpdateView.getEditText().setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            holder.mItemUpdateView.getEditText().setTextColor(R.color.black);
            holder.mItemUpdateView.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mSecurityQuestionUpdate = s.toString();
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        } else if (itemType == INFO_USER_SECURITY_ANSWER_RESET) {
            holder.mItemIcon.setImageResource(R.drawable.icon_security_answer_hint_input);
            holder.mItemTitleText.setText("重置密保答案");
            holder.mItemTitleText.setVisibility(View.GONE);
            holder.mItemValueText.setVisibility(View.GONE);
            holder.mItemUpdateView.setVisibility(View.VISIBLE);
            holder.mItemUpdateView.setLabel("重置密保答案");
            holder.mItemUpdateView.setText("");
            holder.mItemUpdateView.getEditText().setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            holder.mItemUpdateView.getEditText().setTextColor(R.color.black);
            holder.mItemUpdateView.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mSecurityAnswerUpdate = s.toString();
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        } else if (itemType == INFO_USER_BALANCE_RECHARGE) {
            holder.mItemIcon.setImageResource(R.drawable.icon_recharge_hint);
            holder.mItemTitleText.setText("账户充值");
            holder.mItemTitleText.setVisibility(View.GONE);
            holder.mItemValueText.setVisibility(View.GONE);
            holder.mItemUpdateView.setVisibility(View.VISIBLE);
            holder.mItemUpdateView.setLabel("充值金额");
            holder.mItemUpdateView.setText("");
            holder.mItemUpdateView.getEditText().setTextColor(R.color.black);
            holder.mItemUpdateView.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
            holder.mItemUpdateView.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mBalanceAdd = s.toString();
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

        } else {
            int realPos = position;
            if (realPos == 0 || realPos == 1) {
                holder.mItemIcon.setImageResource(mInfoIconList.get(realPos));
                holder.mItemTitleText.setText(mInfoTitleList.get(realPos));
                holder.mItemValueText.setText(mInfoValueList.get(realPos));
            } else {
//                if (!mIsConfirmQuestionHidden && mIsConfirmPasswordHidden) {
//                    realPos = realPos - 2;
//                } else if (mIsConfirmQuestionHidden && !mIsConfirmPasswordHidden) {
//                    realPos = realPos - 1;
//                } else {
//                    // to do nothing
//                }
                if (mIsConfirmPasswordHidden && mIsConfirmQuestionHidden && mIsConfirmRechargeHidden) {
                    // do nothing about realPos
                } else if (mIsConfirmPasswordHidden && mIsConfirmQuestionHidden) {
                    realPos = realPos - 1;
                } else if (mIsConfirmPasswordHidden && mIsConfirmRechargeHidden) {
                    realPos = realPos - 2;
                } else {
                    realPos = realPos - 1;
                }
                holder.mItemIcon.setImageResource(mInfoIconList.get(realPos));
                holder.mItemTitleText.setText(mInfoTitleList.get(realPos));
                holder.mItemValueText.setText(mInfoValueList.get(realPos));
            }

        }
    }

}
