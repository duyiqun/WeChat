package com.qun.weichat.presenter;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.qun.weichat.utils.ThreadUtils;
import com.qun.weichat.view.activity.RegisterView;

/**
 * Created by Qun on 2017/5/13.
 */

public class RegisterPresenterImpl implements RegisterPresenter {

    private RegisterView mRegisterView;

    public RegisterPresenterImpl(RegisterView registerView) {
        mRegisterView = registerView;
    }

    /**
     * 1. 调用AVOSCloud的SDK，注册云数据库
     * 2. 调用环信SDK，注册环信平台
     * 3. 让结果返回给View
     */
    @Override
    public void register(final String username, final String pwd) {
        final AVUser user = new AVUser();// 新建 AVUser 对象实例
        user.setUsername(username);// 设置用户名
        user.setPassword(pwd);// 设置密码
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    // 注册成功，再注册环信
                    ThreadUtils.runOnSubThread(new Runnable() {
                        @Override
                        public void run() {
                            //注册失败会抛出HyphenateException
                            try {
                                EMClient.getInstance().createAccount(username, pwd);//同步方法
                                //成功,在主线程中回调View
                                ThreadUtils.runOnMainThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mRegisterView.onRegister(true, null, username, pwd);
                                    }
                                });
                            } catch (final HyphenateException e1) {
                                e1.printStackTrace();
                                //环信失败
                                //1).将云数据库的数据删除
                                try {
                                    user.delete();
                                } catch (AVException e2) {
                                    e2.printStackTrace();
                                }
                                //2).告诉View注册删除
                                ThreadUtils.runOnMainThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mRegisterView.onRegister(false, e1.getMessage(), username, pwd);
                                    }
                                });
                            }
                        }
                    });
                } else {
                    // 失败的原因可能有多种，常见的是用户名已经存在。
                    // 将失败的原因告诉View
                    mRegisterView.onRegister(false, e.getMessage(), username, pwd);
                }
            }
        });
    }
}
