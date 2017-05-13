package com.qun.weichat.view.activity;

import android.Manifest;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.qun.weichat.MainActivity;
import com.qun.weichat.R;
import com.qun.weichat.presenter.LoginPresenter;
import com.qun.weichat.presenter.LoginPresenterImpl;
import com.qun.weichat.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity implements TextView.OnEditorActionListener, View.OnClickListener, LoginView {

    private static final int REQUEST_PERMISSION = 1;
    @BindView(R.id.et_username)
    EditText mEtUsername;
    @BindView(R.id.et_pwd)
    EditText mEtPwd;
    @BindView(R.id.til_username)
    TextInputLayout mTilUsername;
    @BindView(R.id.til_pwd)
    TextInputLayout mTilPwd;
    @BindView(R.id.btn_login)
    Button mBtnLogin;
    @BindView(R.id.tv_newuser)
    TextView mTvNewuser;

    LoginPresenter mLoginPresenter;

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

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mEtPwd.setOnEditorActionListener(this);
        mBtnLogin.setOnClickListener(this);
        mTvNewuser.setOnClickListener(this);

        mLoginPresenter = new LoginPresenterImpl(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String username = getUsername();
        String pwd = getPwd();
        mEtUsername.setText(username);
        mEtPwd.setText(pwd);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (v.getId() == R.id.et_pwd) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                login();
                return true;
            }
        }
        return false;
    }

    private void login() {
        /**
         * 1. 获取用户名和密码
         * 2. 校验用户名和密码，如果哪个不正确就把焦点移动到哪里
         * 3. 登录（调用P层）
         */
        if (!checkUsernameAndPwd(mEtUsername, mEtPwd, mTilUsername, mTilPwd)) {
            return;
        }

        //申请SDCard权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PermissionChecker.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
            return;
        }

        //显示进度条对话框
        showProgress("正在登录");
        String username = mEtUsername.getText().toString().trim();
        String pwd = mEtPwd.getText().toString().trim();
        mLoginPresenter.login(username, pwd);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            login();
            if (grantResults[0] != PermissionChecker.PERMISSION_GRANTED) {
                ToastUtil.showMsg(this, "您拒绝了访问外部存储权限，可能导致部分功能不可用。");
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.tv_newuser:
                startActivity(RegistActivity.class, false);
                break;
            default:
                break;
        }
    }

    @Override
    public void onLogin(boolean isSuccess, String msg, String username, String pwd) {
        /**
         * 1. 隐藏进度对话框
         * 2. 保存用户到sp
         * 3. 如果成功则跳转到主界面
         * 4. 失败弹Toast
         */
        hideProgress();
        saveUser(username, pwd);
        if (isSuccess) {
            startActivity(MainActivity.class, true);
        } else {
            ToastUtil.showMsg(this, msg);
        }
    }
}
