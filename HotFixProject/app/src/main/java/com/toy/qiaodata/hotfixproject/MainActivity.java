package com.toy.qiaodata.hotfixproject;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.tencent.bugly.beta.Beta;

/**
 * Created by YANG on 2018/5/9.
 */

public class MainActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = "ABC";
//                String tag = null;
                tag.toLowerCase();
                Toast.makeText(getApplicationContext(), "BUG已经修复", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btn_check_upgrade).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Beta.checkUpgrade();
            }
        });
    }
}
