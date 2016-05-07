package com.ssdut.roysun.personalfinancialrecommendationsystem.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.DialogUtils;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.ToastUtils;

/**
 * Created by roysun on 16/3/12.
 * 计算页面
 */
public class CalculationActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = "CalculationActivity";

    Button mBtn1, mBtn2, mBtn3, mBtn4, mBtn5, mBtn6, mBtn7, mBtn8, mBtn9, mBtn0, mBtnPoint, mBtnAdd, mBtnSubtract, mBtnMultiply, mBtnDivide, mBtnEqual, mBtnDel, mBtnClean;
    Button mBtn[] = new Button[]{mBtn0, mBtn1, mBtn2, mBtn3, mBtn4, mBtn5, mBtn6, mBtn7, mBtn8, mBtn9, mBtnPoint, mBtnDel};
    EditText mTextInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);
        initView();
    }

    @Override
    protected void initView() {
        mTextInput = (EditText) findViewById(R.id.et_text_input);
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
    protected void onResume() {
        overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
//        overridePendingTransition(0, 0);
        super.onResume();
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

    double mFirstNum = 0;
    double mSecondNum = 0;
    char mOperator = '0';

    public void calculate(Button button) {
        String _strInput = mTextInput.getText().toString().trim();
        //防止数字末尾是小数点
        if (_strInput.contains(".") && _strInput.indexOf(".") == (_strInput.length() - 1)) {
            _strInput = _strInput.substring(0, _strInput.length() - 1);  //start -> end-1
        }
        if (_strInput.endsWith("+") || _strInput.endsWith("-") || _strInput.endsWith("*") || _strInput.endsWith("/")) {
            String _strNew = _strInput.substring(0, _strInput.length() - 1);
            _strInput = _strNew;
            mTextInput.setText(_strInput);
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
            mFirstNum = Double.parseDouble(_strInput);
        }
    }

    /*
     * 获取最后一次输入符号后面的的数字
     * */
    public String getLastNum() {
        String _strInput = mTextInput.getText().toString().trim();
        //防止输入末尾出现符号
        if (_strInput.endsWith("+") || _strInput.endsWith("-") || _strInput.endsWith("*") || _strInput.endsWith("/")) {
            String _strNew = _strInput.substring(0, _strInput.length() - 1);
            _strInput = _strNew;
        }
        int _opIndex = _strInput.lastIndexOf(mOperator + "") + 1;
        String _lastNum = _strInput.substring(_opIndex);
        if (_lastNum.equals("") || _lastNum.equals(null)) {
            _lastNum = "0";
            if (mOperator == '*' || mOperator == '/') {
                _lastNum = "1";
            }
        }
        return _lastNum;
    }

    double mResult = 0;

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
                if (mSecondNum == 0 || mSecondNum == 0.0) {
                    ToastUtils.showMsg(this, "你数学是体育老师教的吧？");
                    return;
                } else {
                    mResult = mFirstNum / mSecondNum;
                }
                break;
        }
        String _resultString = String.valueOf(mResult);
        if (_resultString.contains(".")) {
            String _str[] = _resultString.split("\\.");
            if (_str[1].equals("0")) {
                //小数部分为0，即运算结果为整数
                mTextInput.setText(_str[0]);
            } else {
                //运算结果含小数
                mTextInput.setText(_resultString);
            }
        } else {
            //运算结果不含小数
            mTextInput.setText(_resultString);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: {
                DialogUtils.showExitDialog(this, ACTIVITY_CAlCULATION);
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private class NumberClickListener implements View.OnClickListener {
        private EditText et;

        public NumberClickListener(EditText et) {
            this.et = et;
        }

        @Override
        public void onClick(View v) {
            Button _button = (Button) v;
            String _num = et.getText().toString().trim();
            if (v.getId() != R.id.btn_delete && !_num.contains("+") && !_num.contains("-") && !_num.contains("*") && !_num.contains("/") && _num.length() > 12) {
                return;
            }
            if (_num.length() > 11) {
                et.setTextSize(25);
            } else {
                et.setTextSize(40);
            }
            if (v.getId() != R.id.btn_delete) {
                if (_num.equals("0") || mOperator == '=') {
                    // 第一次输入时,符号为 = 或者文本框内容为0
                    mOperator = '0';  //把符号改为 0 避免再次进入该段程序
                    if (_button.getText().equals(".")) {
                        et.setText("0.");
                    } else {
                        et.setText(_button.getText());
                    }
                } else {
                    if (_num.contains(".")) {
                        // 金额中已经包含小数点
                        if (_button.getText().equals(".") && _num.indexOf(".", _num.lastIndexOf(mOperator + "")) != -1) {
                            //已有小数点情况下依然输入小数点
                            ToastUtils.showMsg(CalculationActivity.this, "没学过数学呀？");
                            return;
                        }
                        // 小数点后超过6位时
                        if ((_num.length() - _num.indexOf(".")) <= 6 || (
                                _num.contains("+") ||
                                        _num.contains("-") ||
                                        _num.contains("*") ||
                                        _num.contains("/"))) {
                            et.append(_button.getText());
                        } else {
                            ToastUtils.showMsg(CalculationActivity.this, "这么多小数，想整死我啊？");
                        }
                    } else {
                        et.append(_button.getText());
                    }
                }
            } else {
                // 如果是删除键
                if (!_num.equals("0")) {
                    if (_num.length() > 1) {
                        String str = _num.substring(0, _num.length() - 1);
                        et.setText(str);
                    } else {
                        et.setText("0");
                    }
                }
            }
        }
    }
}
