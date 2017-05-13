package com.qun.weichat.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Qun on 2017/5/11.
 */

public class BaseActivity extends AppCompatActivity {

    private SharedPreferences mSharedPreferences;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(true);
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
}
