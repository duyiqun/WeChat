package com.qun.weichat.view.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.qun.weichat.R;
import com.qun.weichat.presenter.RegistPresenter;
import com.qun.weichat.presenter.RegistPresenterImpl;
import com.qun.weichat.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegistActivity extends BaseActivity implements TextView.OnEditorActionListener, View.OnClickListener, RegistView {

    @BindView(R.id.et_username)
    EditText mEtUsername;
    @BindView(R.id.til_username)
    TextInputLayout mTilUsername;
    @BindView(R.id.et_pwd)
    EditText mEtPwd;
    @BindView(R.id.til_pwd)
    TextInputLayout mTilPwd;
    @BindView(R.id.btn_regist)
    Button mBtnRegist;

    RegistPresenter mRegistPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_regist);
        ButterKnife.bind(this);

        mEtPwd.setOnEditorActionListener(this);
        mBtnRegist.setOnClickListener(this);

        //创建P层对象
        mRegistPresenter = new RegistPresenterImpl(this);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (v.getId()) {
            case R.id.et_pwd:
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    regist();
                    return true;
                }
                break;
            default:
                break;
        }
        return false;
    }

    private void regist() {
        /**
         * 1. 获取用户名和密码
         * 2. 校验用户名和密码，如果哪个不正确就把焦点移动到哪里
         * 3. 注册（调用P层）
         */
        if (!checkUsernameAndPwd(mEtUsername, mEtPwd, mTilUsername, mTilPwd)) {
            return;
        }

        //显示进度条对话框
        showProgress("正在注册");
        String username = mEtUsername.getText().toString().trim();
        String pwd = mEtPwd.getText().toString().trim();
        //调用P层注册
        mRegistPresenter.regist(username, pwd);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_regist:
                regist();
                break;
        }
    }

    @Override
    public void onRegist(boolean isSuccess, String message, String username, String pwd) {
        hideProgress();
        if (isSuccess) {
            //1).保存username和pwd到sp
            //2).跳转都LoginActivity
            saveUser(username, pwd);
            startActivity(LoginActivity.class, true);
        } else {//失败
            ToastUtil.showMsg(this, message);
        }
    }
}
