package com.qun.weichat.presenter;

import com.qun.weichat.view.activity.RegistView;

/**
 * Created by Qun on 2017/5/13.
 */

public class RegistPresenterImpl implements RegistPresenter {

    private RegistView mRegistView;

    public RegistPresenterImpl(RegistView registView) {
        mRegistView = registView;
    }

    @Override
    public void regist(String username, String pwd) {
        
    }
}
