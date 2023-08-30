package com.socket.chat.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.socket.chat.R;
import com.socket.chat.util.PreferenceManager;
import com.socket.chat.util.Utils;

import org.apache.commons.lang3.StringUtils;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Utils.preferenceManager = new PreferenceManager(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                var i = new Intent(SplashActivity.this, LoginActivity.class);
//                if(StringUtils.length(PreferenceManager.getToken()) > 0) {
//                    i = new Intent(SplashActivity.this, HomeActivity.class);
//                }
                startActivity(i);
                finish();
            }
        }, 2*1000);
    }
}
