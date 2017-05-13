package com.qun.weichat.adapter;

import com.hyphenate.EMCallBack;
import com.qun.weichat.utils.ThreadUtils;

/**
 * Created by Qun on 2017/5/13.
 */

public abstract class CallBack implements EMCallBack {

    public abstract void onMainSuccess();

    public abstract void onMainError(int code, String msg);

    public abstract void onMainProgress(int code, String msg);

    @Override
    public void onSuccess() {
        ThreadUtils.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                onMainSuccess();
            }
        });
    }

    @Override
    public void onError(final int code, final String msg) {
        ThreadUtils.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                onMainError(code, msg);
            }
        });
    }

    @Override
    public void onProgress(final int code, final String msg) {
        ThreadUtils.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                onMainProgress(code, msg);
            }
        });
    }
}
