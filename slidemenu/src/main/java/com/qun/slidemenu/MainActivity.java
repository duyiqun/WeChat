package com.qun.slidemenu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView mLvMain;
    private ListView mLvMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLvMain = (ListView) findViewById(R.id.lv_main);
        mLvMenu = (ListView) findViewById(R.id.lv_menu);

        List<String> names = Arrays.asList(Cheeses.NAMES);
        MainAdapter mainAdapter = new MainAdapter(names);
        mLvMain.setAdapter(mainAdapter);
    }
}
