package com.qun.weichat.presenter;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.qun.weichat.adapter.CallBack;
import com.qun.weichat.view.fragment.PluginView;

/**
 * Created by Qun on 2017/5/22.
 */

public class PluginPresenterImpl implements PluginPresenter {

    private PluginView mPluginView;

    public PluginPresenterImpl(PluginView pluginView) {
        this.mPluginView = pluginView;
    }

    @Override
    public void logout() {
        EMClient.getInstance().logout(true, new CallBack() {
            @Override
            public void onMainSuccess() {
                mPluginView.onLogout (true, "success");
            }

            @Override
            public void onMainError(int code, String msg) {
                mPluginView.onLogout(false, "success");
            }

            @Override
            public void onMainProgress(int code, String msg) {

            }
        });
    }
}
