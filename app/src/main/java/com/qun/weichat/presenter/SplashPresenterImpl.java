package com.qun.weichat.presenter;

import com.hyphenate.chat.EMClient;
import com.qun.weichat.view.activity.SplashView;

/**
 * Created by Qun on 2017/5/11.
 */

public class SplashPresenterImpl implements SplashPresenter {

    private SplashView mSplashView;

    public SplashPresenterImpl(SplashView splashView) {
        this.mSplashView = splashView;
    }

    @Override
    public void isLogin() {
        boolean isLogin = EMClient.getInstance().isLoggedInBefore() && EMClient.getInstance().isConnected();
        //如何将结果返回给View
        mSplashView.onCheckLogin(isLogin);
    }
}
