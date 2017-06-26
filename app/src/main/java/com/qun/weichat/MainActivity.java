package com.qun.weichat;

import android.graphics.Color;
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

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.qun.weichat.factory.FragmentFactory;
import com.qun.weichat.view.activity.AddFriendActivity;
import com.qun.weichat.view.activity.BaseActivity;
import com.qun.weichat.view.fragment.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.PageBottomTabLayout;
import me.majiajie.pagerbottomtabstrip.item.NormalItemView;
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
    PageBottomTabLayout mPageBottomTabLayout;
    private NavigationController mNavigationController;
    private static final String[] TITLES = {"消息", "联系人", "动态"};
    private NormalItemView mConversationTabItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
        initData();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EMMessage emMessage) {
        int unreadMessageCount = EMClient.getInstance().chatManager().getUnreadMessageCount();
        mConversationTabItem.setMessageNumber(unreadMessageCount);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initData() {
        int unreadMessageCount = EMClient.getInstance().chatManager().getUnreadMessageCount();
        mConversationTabItem.setMessageNumber(unreadMessageCount);
    }

    private void initView() {
        mToolBar.setTitle("");
        setSupportActionBar(mToolBar);
        initTab();
        mTvTitle.setText(TITLES[0]);
        initFragment();
    }

    private void initTab() {
//        mNavigationController = mPageBottomTabLayout.material()
//                .addItem(R.mipmap.conversation_selected_2, TITLES[0])
//                .addItem(R.mipmap.contact_selected_2, TITLES[1])
//                .addItem(R.mipmap.plugin_selected_2, TITLES[2])
//                .build();

        mConversationTabItem = new NormalItemView(this);
        mConversationTabItem.setTextDefaultColor(Color.parseColor("#9c9c9c"));
        mConversationTabItem.setTextCheckedColor(getResources().getColor(R.color.colorPrimary));
        mConversationTabItem.initialize(R.mipmap.conversation_selected_2_black, R.mipmap.conversation_selected_2, TITLES[0]);
        NormalItemView contactTabItem = new NormalItemView(this);
        contactTabItem.setTextDefaultColor(Color.parseColor("#9c9c9c"));
        contactTabItem.setTextCheckedColor(getResources().getColor(R.color.colorPrimary));
        contactTabItem.initialize(R.mipmap.contact_selected_2_black, R.mipmap.contact_selected_2, TITLES[1]);
        NormalItemView pluginTabItem = new NormalItemView(this);
        pluginTabItem.setTextDefaultColor(Color.parseColor("#9c9c9c"));
        pluginTabItem.setTextCheckedColor(getResources().getColor(R.color.colorPrimary));
        pluginTabItem.initialize(R.mipmap.plugin_selected_2_black, R.mipmap.plugin_selected_2, TITLES[2]);

        mNavigationController = mPageBottomTabLayout.custom()
                .addItem(mConversationTabItem)
                .addItem(contactTabItem)
                .addItem(pluginTabItem)
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
