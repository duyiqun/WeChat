package com.qun.weichat.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Qun on 2017/5/11.
 */

public class BaseActivity extends AppCompatActivity {

    public void startActivity(Class<? extends BaseActivity> clazz, boolean isFinish) {
        startActivity(new Intent(this, clazz));
        if (isFinish) {
            finish();
        }
    }
}
