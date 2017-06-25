package com.qun.weichat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.qun.weichat.factory.FragmentFactory;
import com.qun.weichat.view.activity.AddFriendActivity;
import com.qun.weichat.view.activity.BaseActivity;
import com.qun.weichat.view.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.PageBottomTabLayout;
import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectedListener;


public class MainActivity extends BaseActivity implements OnTabItemSelectedListener {

    private static final String TAG = "MainActivity";
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @BindView(R.id.fl_content)
    FrameLayout mFlContent;
    @BindView(R.id.tab)
    PageBottomTabLayout mTab;
    private NavigationController mNavigationController;
    private static final String[] TITLES = {"消息", "联系人", "动态"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        mToolBar.setTitle("");
        setSupportActionBar(mToolBar);
        initTab();
        mTvTitle.setText(TITLES[0]);
        initFragment();
    }

    private void initTab() {
        mNavigationController = mTab.material()
                .addItem(R.mipmap.conversation_selected_2, TITLES[0])
                .addItem(R.mipmap.contact_selected_2, TITLES[1])
                .addItem(R.mipmap.plugin_selected_2, TITLES[2])
                .build();
        mNavigationController.addTabItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        if (menu instanceof MenuBuilder) {
            MenuBuilder menuBuilder = (MenuBuilder) menu;
            menuBuilder.setOptionalIconsVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                startActivity(AddFriendActivity.class, false);
                break;
            case R.id.share:

                break;
            case R.id.about:

                break;
            default:
                break;
        }
        return true;
    }

    private void initFragment() {
        /**
         * 为了避免Fragment重影问题
         * 如果当前Activity中已经有Fragment了，就把老的Fragment清除掉
         */
        for (int i = 0; i < TITLES.length; i++) {
            Fragment fragmentByTag = getSupportFragmentManager().findFragmentByTag(i + "");
            if (fragmentByTag != null) {
                Log.d(TAG, "initFragments: 发现有老的Fragment" + fragmentByTag);
                getSupportFragmentManager().beginTransaction().remove(fragmentByTag).commit();
            }
        }

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_content, FragmentFactory.getFragment(0), "0")
                .commit();
    }

    @Override
    public void onSelected(int index, int old) {
        mTvTitle.setText(TITLES[index]);

        /**
         * 切换Fragment
         * 1. 将oldFragment给隐藏
         * 2. 判断新Fragment是否被添加了，如果没有被添加则添加
         */
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        BaseFragment fragment = FragmentFactory.getFragment(index);
        if (!fragment.isAdded()) {
            transaction.add(R.id.fl_content, fragment, index + "");
        }
        transaction.hide(FragmentFactory.getFragment(old));
        transaction.show(fragment);
        transaction.commit();
    }

    @Override
    public void onRepeat(int index) {

    }
}
