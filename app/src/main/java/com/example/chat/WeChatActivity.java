package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.chat.adapter.WeChatAdapter;

import io.socket.client.Socket;

public class WeChatActivity extends AppCompatActivity {
    private ViewPager vp_content; // 声明一个翻页视图对象
    private RadioGroup rg_tabbar; // 声明一个单选组对象
    private Socket mSocket; // 声明一个套接字对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_we_chat);
        initView(); // 初始化视图
        mSocket = MainApplication.getInstance().getSocket();
        mSocket.connect(); // 建立Socket连接
    }

    // 初始化视图
    private void initView() {
        vp_content = findViewById(R.id.vp_content);
        rg_tabbar = findViewById(R.id.rg_tabbar);
        WeChatAdapter adapter = new WeChatAdapter(getSupportFragmentManager());
        vp_content.setAdapter(adapter);
        vp_content.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                rg_tabbar.check(rg_tabbar.getChildAt(position).getId());
            }
        });
        rg_tabbar.setOnCheckedChangeListener((group, checkedId) -> {
            for (int pos = 0; pos < rg_tabbar.getChildCount(); pos++) {
                RadioButton tab = (RadioButton) rg_tabbar.getChildAt(pos);
                if (tab.getId() == checkedId) {
                    vp_content.setCurrentItem(pos);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSocket.connected()) { // 已经连上Socket服务器
            mSocket.disconnect(); // 断开Socket连接
        }
    }

}