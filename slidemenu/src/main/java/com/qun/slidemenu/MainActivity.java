package com.qun.slidemenu;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

        mLvMenu.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Cheeses.sCheeseStrings) {

            //通过复写getView方法，更改字体颜色
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (view instanceof TextView) {
                    TextView textView = (TextView) view;
                    textView.setTextColor(Color.WHITE);
                }
                return view;
            }
        });
    }
}
