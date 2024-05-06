package com.example.myapplication.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Course;
import com.example.myapplication.Encrypt;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.Rank;
import com.example.myapplication.databinding.ActivityLoginBinding;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private ProgressBar loadingProgressBar;
    String userName;
    String password;
    TextView processTextView;
    EditText usernameEditText;
    EditText passwordEditText;
    Button loginButton;
    CheckBox checkBoxJzmm;
    CheckBox checkBoxZddl;
    List<Rank> zyRanks = new ArrayList<>();
    List<Rank> bjRanks = new ArrayList<>();
    List<List<Course>> xsCourses = new ArrayList<>();
    Map<String, String> cookies = new HashMap<>();

    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("IdInfo", Context.MODE_PRIVATE);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        usernameEditText = binding.username;
        passwordEditText = binding.password;
        loginButton = binding.login;
        loadingProgressBar = binding.loading;
        processTextView = binding.textViewHint;
        checkBoxJzmm = binding.checkBoxJzmm;
        checkBoxZddl = binding.checkBoxZddl;
        loginButton.setEnabled(true);
        if (sharedPreferences.getBoolean("isJzmm", false)) {
            checkBoxJzmm.setChecked(true);
            checkBoxZddl.setEnabled(checkBoxJzmm.isChecked());
            usernameEditText.setText(sharedPreferences.getString("userName", ""));
            passwordEditText.setText(sharedPreferences.getString("password", ""));
        }
        if (sharedPreferences.getBoolean("isZddl", false)) {
            checkBoxZddl.setChecked(true);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("userName", usernameEditText.getText().toString());
            intent.putExtra("isZddl", true);
            startActivity(intent);
            finish();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                //if (imm != null) {
                //    imm.hideSoftInputFromWindow(loginButton.getWindowToken(), 0);
                //}
                userName = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();
                if (!userName.equals("") && !password.equals("")) {
                    loadingProgressBar.setVisibility(View.VISIBLE);
                    processTextView.setVisibility(View.VISIBLE);
                    passwordEditText.setVisibility(View.INVISIBLE);
                    usernameEditText.setVisibility(View.INVISIBLE);
                    loginButton.setVisibility(View.INVISIBLE);
                    checkBoxJzmm.setVisibility(View.INVISIBLE);
                    checkBoxZddl.setVisibility(View.INVISIBLE);
                    processTextView.setText("正在登录");
                    //////////////////
                    String welcome = getString(R.string.welcome) + userName;
                    final HandlerThread handlerThread = new HandlerThread("StudentDataFetcher");
                    handlerThread.start();
                    Handler backgroundHandler = new Handler(handlerThread.getLooper()) {
                        @Override
                        public void handleMessage(@NonNull Message msg) {
                            if (msg.what == 4) {
                                String host = (String) msg.obj;
                                // 更新UI
                                runOnUiThread(() -> {
                                    loadingProgressBar.setVisibility(View.GONE);
                                    processTextView.setVisibility(View.GONE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    if (checkBoxJzmm.isChecked()) {
                                        editor.putBoolean("isJzmm", true);
                                        editor.putString("userName", userName);
                                        editor.putString("password", password);
                                        editor.apply();
                                    }
                                    if(checkBoxZddl.isChecked()){
                                        editor.putBoolean("isZddl", true);
                                        editor.apply();
                                    }
                                    editor.putString("host", host);
                                    editor.apply();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("userName", userName);
                                    intent.putExtra("cookies", (Serializable) cookies);
                                    intent.putExtra("host", host);
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

                                    try {
                                        //获取登录信息
                                        Connection.Response response = Jsoup.connect("https://wvpn.ahu.edu.cn/https/77726476706e69737468656265737421fff944d226387d1e7b0c9ce29b5b/tp_up/view?m=up#act=portal/viewhome")
                                                .method(Connection.Method.GET)
                                                .execute();

                                        Document doc = Jsoup.parse(response.body());
                                        String host = "https://" + response.url().getHost();
                                        String lt = Objects.requireNonNull(doc.getElementById("lt")).attr("value");
                                        String execution = Objects.requireNonNull(
                                                doc.getElementsByAttributeValue("name", "execution").first()).attr("value");
                                        String _eventId = Objects.requireNonNull(
                                                doc.getElementsByAttributeValue("name", "_eventId").first()).attr("value");
                                        String loginURL = host + Objects.requireNonNull(
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
                                        cookies.putAll(response.cookies());
                                        String dataViewURL;
                                        if (host.contains("one.ahu"))
                                            dataViewURL = "https://one.ahu.edu.cn/idc/view?m=idc#act=idc/idcHome/studentHome";
                                        else dataViewURL = "https://wvpn.ahu.edu.cn/https/77726476706e69737468656265737421fff944d226387d1e7b0c9ce29b5b/idc/view?m=idc#act=idc/idcHome/studentHome";
                                        //登录
                                        response = Jsoup.connect(dataViewURL)
                                                .method(Connection.Method.GET)
                                                .cookies(cookies)
                                                .execute();

                                        doc = Jsoup.parse(response.body());

                                        message = Message.obtain(backgroundHandler, 4,host); // 用Message携带数据
                                        message.sendToTarget();
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }

                                }
                            });
                            thread.start();
                        }
                    });
                    //////////////////////////////////////////////////////////////
                    Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
                }
            }
        });
        checkBoxJzmm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkBoxZddl.setEnabled(isChecked);
            }
    });

    }
}