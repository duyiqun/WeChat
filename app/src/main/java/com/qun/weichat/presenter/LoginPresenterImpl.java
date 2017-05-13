package com.qun.weichat.presenter;

import com.hyphenate.chat.EMClient;
import com.qun.weichat.adapter.CallBack;
import com.qun.weichat.view.activity.LoginView;

/**
 * Created by Qun on 2017/5/13.
 */

public class LoginPresenterImpl implements LoginPresenter {

    private LoginView mLoginView;

    public LoginPresenterImpl(LoginView loginView) {
        this.mLoginView = loginView;
    }

    @Override
    public void login(final String username, final String pwd) {
        EMClient.getInstance().login(username, pwd, new CallBack() {
            @Override
            public void onMainSuccess() {
                mLoginView.onLogin(true, "success", username, pwd);
            }

            @Override
            public void onMainError(int code, String msg) {

            }

            @Override
            public void onMainProgress(int code, String msg) {
                mLoginView.onLogin(true, msg, username, pwd);
            }
        });
    }
}
