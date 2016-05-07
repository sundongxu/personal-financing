package com.ssdut.roysun.personalfinancialrecommendationsystem.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity{

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("用户登录");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEmailView.setTextColor(R.color.black);
    }

}

