package com.ssdut.roysun.personalfinancialrecommendationsystem.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;

/**
 * Created by roysun on 16/3/12.
 * 计算页面
 */
public class CalculationActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = "CalculationActivity";
    double mFirstNum = 0;
    double mSecondNum = 0;
    char mOperator = '0';
    double mResult = 0;
    private Button mBtn1, mBtn2, mBtn3, mBtn4, mBtn5, mBtn6, mBtn7, mBtn8, mBtn9, mBtn0, mBtnPoint, mBtnAdd, mBtnSubtract, mBtnMultiply, mBtnDivide, mBtnEqual, mBtnDel, mBtnClean;
    private Button mBtn[] = new Button[]{mBtn0, mBtn1, mBtn2, mBtn3, mBtn4, mBtn5, mBtn6, mBtn7, mBtn8, mBtn9, mBtnPoint, mBtnDel};
    private EditText mTextInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);
        initData();
        initView();
    }

    @Override
    protected void initData() {
        super.initData();
        mContext = this;
    }

    @Override
    protected void initView() {
        super.initView();
        if (mToolbar != null) {
            mToolbar.setTitle(R.string.title_calculation_page);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mTextInput = (EditText) findViewById(R.id.et_num_input);
        int id[] = new int[]{R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4, R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9, R.id.btn_point, R.id.btn_delete};
        for (int i = 0; i < mBtn.length; i++) {
            mBtn[i] = (Button) findViewById(id[i]);
            mBtn[i].setOnClickListener(new NumberClickListener(mTextInput));
        }

        mBtnAdd = (Button) this.findViewById(R.id.btn_add);
        mBtnAdd.setOnClickListener(this);
        mBtnSubtract = (Button) this.findViewById(R.id.btn_subtract);
        mBtnSubtract.setOnClickListener(this);
        mBtnMultiply = (Button) this.findViewById(R.id.btn_multiply);
        mBtnMultiply.setOnClickListener(this);
        mBtnDivide = (Button) this.findViewById(R.id.btn_divide);
        mBtnDivide.setOnClickListener(this);
        mBtnEqual = (Button) this.findViewById(R.id.btn_equal);
        mBtnEqual.setOnClickListener(this);
        mBtnClean = (Button) this.findViewById(R.id.btn_clean);
        mBtnClean.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_add:
                calculate((Button) v);
                break;
            case R.id.btn_subtract:
                calculate((Button) v);
                break;
            case R.id.btn_multiply:
                calculate((Button) v);
                break;
            case R.id.btn_divide:
                calculate((Button) v);
                break;
            case R.id.btn_equal:
                equalTo();
                break;
            case R.id.btn_clean:
                initInput();
                break;
        }
    }

    public void initInput() {
        mTextInput.setText("0");
        mFirstNum = 0;
        mSecondNum = 0;
        mOperator = '0';
    }

    public void calculate(Button button) {
        String strInput = mTextInput.getText().toString().trim();
        //防止数字末尾是小数点
        if (strInput.contains(".") && strInput.indexOf(".") == (strInput.length() - 1)) {
            strInput = strInput.substring(0, strInput.length() - 1);  //start -> end-1
        }
        if (strInput.endsWith("+") || strInput.endsWith("-") || strInput.endsWith("*") || strInput.endsWith("/")) {
            strInput = strInput.substring(0, strInput.length() - 1);
            mTextInput.setText(strInput);
            mOperator = button.getText().charAt(0);
            mTextInput.append(mOperator + "");
            return;
        }
        if (mOperator != '0' && mOperator != '=') {
            equalTo();  //保证前一步运算先得出结果，在进行第二步计算
            mOperator = button.getText().charAt(0);
            mTextInput.append(mOperator + "");
        } else {
            mOperator = button.getText().charAt(0);
            mTextInput.append(mOperator + "");
            mFirstNum = Double.parseDouble(strInput);
        }
    }

    /*
     * 获取最后一次输入符号后面的的数字
     * */
    public String getLastNum() {
        String strInput = mTextInput.getText().toString().trim();
        //防止输入末尾出现符号
        if (strInput.endsWith("+") || strInput.endsWith("-") || strInput.endsWith("*") || strInput.endsWith("/")) {
            strInput = strInput.substring(0, strInput.length() - 1);
        }
        int opIndex = strInput.lastIndexOf(mOperator + "") + 1;
        String lastNum = strInput.substring(opIndex);
        if (lastNum.equals("") || lastNum.equals(null)) {
            lastNum = "0";
            if (mOperator == '*' || mOperator == '/') {
                lastNum = "1";
            }
        }
        return lastNum;
    }

    public void equalTo() {
        if (mOperator == '0') {
            return;
        }
        mSecondNum = Double.parseDouble(getLastNum());
        switch (mOperator) {
            case '+':
                mResult = mFirstNum + mSecondNum;
                break;
            case '-':
                mResult = mFirstNum - mSecondNum;
                break;
            case '*':
                mResult = mFirstNum * mSecondNum;
                break;
            case '/':
                if (mSecondNum == 0) {
                    Snackbar.make(mToolbar, R.string.calculation_who_teach_your_math, Snackbar.LENGTH_LONG).show();
                    return;
                } else {
                    mResult = mFirstNum / mSecondNum;
                }
                break;
        }
        String strResult = String.valueOf(mResult);
        if (strResult.contains(".")) {
            String str[] = strResult.split("\\.");
            if (str[1].equals("0")) {
                //小数部分为0，即运算结果为整数
                mTextInput.setText(str[0]);
            } else {
                //运算结果含小数
                mTextInput.setText(strResult);
            }
        } else {
            //运算结果不含小数
            mTextInput.setText(strResult);
        }
        mFirstNum = mResult;
        mSecondNum = 0;
        mOperator = '=';
        if (mTextInput.getText().toString().trim().length() > 11) {
            mTextInput.setTextSize(25);
        } else {
            mTextInput.setTextSize(40);
        }
    }

    private class NumberClickListener implements View.OnClickListener {

        private EditText et;

        public NumberClickListener(EditText et) {
            this.et = et;
        }

        @Override
        public void onClick(View v) {
            Button btn = (Button) v;
            String num = et.getText().toString().trim();
            if (v.getId() != R.id.btn_delete && !num.contains("+") && !num.contains("-") && !num.contains("*") && !num.contains("/") && num.length() > 12) {
                return;
            }
            if (num.length() > 11) {
                et.setTextSize(25);
            } else {
                et.setTextSize(40);
            }
            if (v.getId() != R.id.btn_delete) {
                if (num.equals("0") || mOperator == '=') {
                    // 第一次输入时,符号为 = 或者文本框内容为0
                    mOperator = '0';  //把符号改为 0 避免再次进入该段程序
                    if (btn.getText().equals(".")) {
                        et.setText("0.");
                    } else {
                        et.setText(btn.getText());
                    }
                } else {
                    if (num.contains(".")) {
                        // 金额中已经包含小数点
                        if (btn.getText().equals(".") && num.indexOf(".", num.lastIndexOf(mOperator + "")) != -1) {
                            //已有小数点情况下依然输入小数点
                            Snackbar.make(mToolbar, R.string.calculation_who_teach_your_math, Snackbar.LENGTH_LONG).show();
                            return;
                        }
                        // 小数点后超过6位时
                        if ((num.length() - num.indexOf(".")) <= 6 || (
                                num.contains("+") ||
                                        num.contains("-") ||
                                        num.contains("*") ||
                                        num.contains("/"))) {
                            et.append(btn.getText());
                        } else {
                            Snackbar.make(mToolbar, R.string.calculation_too_much_decimal, Snackbar.LENGTH_LONG).show();
                        }
                    } else {
                        et.append(btn.getText());
                    }
                }
            } else {
                // 如果是删除键
                if (!num.equals("0")) {
                    if (num.length() > 1) {
                        String str = num.substring(0, num.length() - 1);
                        et.setText(str);
                    } else {
                        et.setText("0");
                    }
                }
            }
        }
    }
}
