package com.tangzy.demo;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final String[] sPermissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void toPDF(View view) {
        if (!EasyPermissions.hasPermissions(this, sPermissions)) {
            EasyPermissions.requestPermissions(this, "请允许存储权限，以便使用功能", 1, sPermissions);
        } else {
            Intent intent = new Intent(this, RecycleViewActivity.class);
            String path = Environment.getExternalStorageDirectory().toString();
            path = path + "/Allinmd/download/项目管理知识体系指南 中文 第六版.pdf";
            File file = new File(path);
            if (!file.exists()) {
                Log.e("", "文件不存在");
            }
            intent.putExtra("url", path);
            startActivity(intent);
        }
    }
}
