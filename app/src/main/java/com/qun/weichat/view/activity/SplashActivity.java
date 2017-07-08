package com.qun.weichat.view.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.widget.ImageView;

import com.qun.weichat.MainActivity;
import com.qun.weichat.R;
import com.qun.weichat.adapter.AnimatorAdapter;
import com.qun.weichat.presenter.SplashPresenter;
import com.qun.weichat.presenter.SplashPresenterImpl;

public class SplashActivity extends BaseActivity implements SplashView {

    public static final int DURATION = 2000;
    private ImageView mIvSplash;
    private SplashPresenter mSplashPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mIvSplash = (ImageView) findViewById(R.id.iv_splash);

        //在创建P层对象的时候，将自己也传递给P层，这样P层就可以回调View层的方法了
        mSplashPresenter = new SplashPresenterImpl(this);
        mSplashPresenter.isLogin();
    }

    @Override
    public void onCheckLogin(boolean isLogin) {
        /**
         * 1. 如果没有登录，则闪屏2s后跳转到登录界面
         *
         * 2. 如果已经登录了，则直接进入主界面
         */
        if (isLogin) {
//            startActivity(new Intent(this, MainActivity.class));
//            finish();
            startActivity(MainActivity.class, true);
        } else {
            //闪屏2s后跳转到登录界面
            playAnimation();
        }
    }

    private void playAnimation() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mIvSplash, "alpha", 0, 1).setDuration(DURATION);
        //设置监听器
        objectAnimator.addListener(new AnimatorAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                startActivity(LoginActivity.class, true);
            }
        });
        objectAnimator.start();
    }
}
