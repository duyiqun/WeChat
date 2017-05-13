package com.qun.weichat;

import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.qun.weichat.view.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.PageBottomTabLayout;
import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectedListener;


public class MainActivity extends BaseActivity implements OnTabItemSelectedListener {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @BindView(R.id.fl_content)
    FrameLayout mFlContent;
    @BindView(R.id.tab)
    PageBottomTabLayout mTab;
    private NavigationController mNavigationController;
    private static final String[] TITLES = {"消息","联系人","动态"};

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
    }

    private void initTab() {
        mNavigationController = mTab.material()
                .addItem(R.mipmap.conversation_selected_2, TITLES[0])
                .addItem(R.mipmap.contact_selected_2, TITLES[1])
                .addItem(R.mipmap.plugin_selected_2, TITLES[2]).build();

        mNavigationController.addTabItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        if (menu instanceof MenuBuilder){
            MenuBuilder menuBuilder = (MenuBuilder) menu;
            menuBuilder.setOptionalIconsVisible(true);
        }
        return true;
    }

    @Override
    public void onSelected(int index, int old) {
        mTvTitle.setText(TITLES[index]);
    }

    @Override
    public void onRepeat(int index) {

    }
}
