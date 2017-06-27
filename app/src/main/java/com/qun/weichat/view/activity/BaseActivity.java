package com.qun.weichat.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.qun.weichat.WeiChatApplication;
import com.qun.weichat.utils.StringUtils;

/**
 * Created by Qun on 2017/5/11.
 */

public class BaseActivity extends AppCompatActivity {

    private SharedPreferences mSharedPreferences;
    private ProgressDialog mProgressDialog;
//    private WeiChatApplication mWeiChatApplication;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(true);

//        mWeiChatApplication = (WeiChatApplication) getApplication();
//        mWeiChatApplication.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mWeiChatApplication.removeActivity(this);
    }

    public void saveUser(String username, String pwd) {
        mSharedPreferences.edit().putString("username", username).putString("pwd", pwd).commit();
    }

    public String getUsername() {
        return mSharedPreferences.getString("username", "");
    }

    public String getPwd() {
        return mSharedPreferences.getString("pwd", "");
    }

    public void startActivity(Class<? extends BaseActivity> clazz, boolean isFinish) {
        startActivity(new Intent(this, clazz));
        if (isFinish) {
            finish();
        }
    }

    public void showProgress(String msg) {
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }

    public void hideProgress() {
        mProgressDialog.dismiss();
    }

    public boolean checkUsernameAndPwd(EditText mEtUsername, EditText mEtPwd, TextInputLayout mTilUsername, TextInputLayout mTilPwd) {
        //字母开头，长度为[3,16]
        String username = mEtUsername.getText().toString().trim();
        //纯数字密码，长度[3,16]
        String pwd = mEtPwd.getText().toString().trim();

        if (!StringUtils.checkUsername(username)) {//用户名不合法
            //显示错误信息
            mTilUsername.setErrorEnabled(true);
            mTilUsername.setError("用户名不合法");
            //重新定位焦点
            mEtUsername.requestFocus(View.FOCUS_RIGHT);
            return false;
        } else {
            //校验合格，隐藏错误信息
            mTilUsername.setErrorEnabled(false);
        }

        if (!StringUtils.checkPwd(pwd)) {//密码不合法
            //显示错误信息
            mTilPwd.setErrorEnabled(true);
            mTilPwd.setError("密码不合法");
            //重新定位焦点
            mEtPwd.requestFocus(View.FOCUS_RIGHT);
            return false;
        } else {
            //校验合格，隐藏错误信息
            mTilPwd.setErrorEnabled(false);
        }
        return true;
    }
}
