package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

// ShareReceiverActivity.java
public class ShareReceiverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String action = intent.getAction();
        if (Intent.ACTION_SEND.equals(action)) {
            // 检查intent类型是否符合你的需求，比如文本、图片或链接等
            if (intent.getType().equals("text/plain")) {
                String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
                // 处理接收到的文本内容
                handleSharedContent(sharedText);
            }// else if (/* 其他类型检查 */) {
                // 处理相应类型的分享内容
            //}
        }
        finish();
    }

    private void handleSharedContent(String content) {
        // 在这里实现处理接收到的内容逻辑
        Log.d("ShareReceiverActivity", "Received shared content: " + content);
        // 可以将内容展示给用户，或者进行进一步的操作（如保存到数据库、上传网络等）
    }
}
