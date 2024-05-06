package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.ui.login.LoginActivity;
import com.example.myapplication.ui.main.SectionsPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    public static String userName = "";
    public static String password = "";
    boolean isZddl = false;

    ViewPager viewPager;
    TabLayout tabs;
    ProgressBar progressBar;
    Map<String, String> cookies;
    SectionsPagerAdapter sectionsPagerAdapter;
    SharedPreferences sharedPreferences;
    String host;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_overflow, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id == R.id.menu_item_refresh) {
            Toast.makeText(this, "刷新", Toast.LENGTH_SHORT).show();
            userName = sharedPreferences.getString("userName", "");
            password = sharedPreferences.getString("password", "");
            progressBar.setVisibility(View.VISIBLE);
            getRefreshData();
            return true;
        }
        if (id == R.id.menu_item_logout) {
            Toast.makeText(this, "注销", Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.putBoolean("isJzmm", false);
            editor.putBoolean("isZddl", false);
            editor.apply();
            SharedPreferencesHelper.deleteAll(this);
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("IdInfo",Context.MODE_PRIVATE);
        Intent intent = getIntent();
        if (intent != null) {
            userName = intent.getStringExtra("userName");
            isZddl = intent.getBooleanExtra("isZddl", false);
            host = intent.getStringExtra("host");
            try {
                cookies = (Map<String, String>) intent.getSerializableExtra("cookies");
            } catch (ClassCastException e) {
                cookies = null;
            }
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        viewPager = binding.viewPager;
        tabs = binding.tabs;
        progressBar = binding.loadingMain;
        setSupportActionBar(binding.toolbar);
        setContentView(binding.getRoot());
        initViewPager(isZddl);
    }
    private void initViewPager(boolean isZddl) {
        sectionsPagerAdapter=
                new SectionsPagerAdapter(this, getSupportFragmentManager(), userName, password, cookies, isZddl,host);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs.setupWithViewPager(viewPager);
        sectionsPagerAdapter.notifyDataSetChanged();
    }
    public void getRefreshData() {
        final HandlerThread handlerThread = new HandlerThread("StudentDataFetcher");
        handlerThread.start();
        Handler backgroundHandler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 4) {
                    if (msg.arg1 == 1)
                        cookies = (Map<String, String>) msg.obj;
                    // 更新UI
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        intent.putExtra("userName", userName);
                        intent.putExtra("cookies", (Serializable) cookies);
                        intent.putExtra("isZddl", false);
                        startActivity(intent);
                        finish();
                    });
                }
            }
        };

        backgroundHandler.post(new Runnable() {
            @Override
            public void run() {
                // 在这里执行你的后台任务
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message;

                        if (cookies == null){
                            try {
                                //获取登录信息
                                Connection.Response response = Jsoup.connect("https://wvpn.ahu.edu.cn/https/77726476706e69737468656265737421fff944d226387d1e7b0c9ce29b5b/tp_up/view?m=up#act=portal/viewhome")
                                        .method(Connection.Method.GET)
                                        .execute();

                                Document doc = Jsoup.parse(response.body());
                                String lt = Objects.requireNonNull(doc.getElementById("lt")).attr("value");
                                String execution = Objects.requireNonNull(
                                        doc.getElementsByAttributeValue("name", "execution").first()).attr("value");
                                String _eventId = Objects.requireNonNull(
                                        doc.getElementsByAttributeValue("name", "_eventId").first()).attr("value");
                                String loginURL = "https://wvpn.ahu.edu.cn" + Objects.requireNonNull(
                                        doc.getElementById("loginForm")).attr("action");
                                Map<String, String> data = new Encrypt().encrypt(userName, password, lt);
                                data.put("lt", lt);
                                data.put("execution", execution);
                                data.put("_eventId", _eventId);
                                cookies = response.cookies();
                                response = Jsoup.connect(loginURL)
                                        .data(data)
                                        .method(Connection.Method.POST)
                                        .cookies(cookies)
                                        .execute();
                                doc = Jsoup.parse(response.body());

                                //登录
                                response = Jsoup.connect("https://wvpn.ahu.edu.cn/https/77726476706e69737468656265737421fff944d226387d1e7b0c9ce29b5b/idc/view?m=idc")
                                        .method(Connection.Method.GET)
                                        .cookies(cookies)
                                        .execute();
                                doc = Jsoup.parse(response.body());

                                message = Message.obtain(backgroundHandler, 4,1,0, cookies); // 用Message携带数据
                                message.sendToTarget();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }else {
                            message = Message.obtain(backgroundHandler, 4,0,0, cookies); // 用Message携带数据
                            message.sendToTarget();
                        }
                    }
                });
                thread.start();
            }
        });
    }
}