package com.socket.chat.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.socket.chat.util.NetworkClient;

import com.socket.chat.R;
import com.socket.chat.conf.Const;
import com.socket.chat.models.UserRequestDto;
import com.socket.chat.util.DialogManager;
import com.socket.chat.util.PreferenceManager;
import com.socket.chat.util.Utils;

import org.apache.commons.lang3.StringUtils;

public class LoginActivity extends AppCompatActivity {

    private TextView name;
    private TextView password;
    private Button login;
    private Context mContext;
    private CheckBox rememberMe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        name = findViewById(R.id.name);
        password = findViewById(R.id.password);
        var register = findViewById(R.id.register);
        login = findViewById(R.id.login);
        rememberMe = findViewById(R.id.rememberMe);
        if (StringUtils.length(PreferenceManager.getUser()) > 0
                && StringUtils.length(PreferenceManager.getPassword()) > 0) {
            rememberMe.setChecked(true);
            name.setText(PreferenceManager.getUser());
            password.setText(PreferenceManager.getPassword());
        }
        login.setOnClickListener(view -> {
            if (login.getText().toString().length() > 0) {
                if (password.getText().toString().length() > 0) {
                    getLogin();
                } else {
                    password.setError("Please fill");
                }
            } else {
                login.setError("Please fill");
            }
        });
        register.setOnClickListener(view -> {
            var intent = new Intent(mContext, RegisterActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void getLogin() {
        var queue = Volley.newRequestQueue(mContext);
        var networkClient = new NetworkClient(queue);
        var apiUrl = Const.rest_address + "/api/v1/user/login";
        var userRequestDto = new UserRequestDto();
        userRequestDto.setName(name.getText().toString());
        userRequestDto.setPassword(password.getText().toString());
        var jsonString = new Gson().toJson(userRequestDto);
        Utils.showProgressBar(mContext, (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content));
        networkClient.makeJsonPostRequest(apiUrl, jsonString,
                response -> {
                    if (rememberMe.isChecked()) {
                        PreferenceManager.setUser(name.getText().toString());
                        PreferenceManager.setPassword(password.getText().toString());
                    } else {
                        PreferenceManager.setUser(null);
                        PreferenceManager.setPassword(null);
                    }
                    PreferenceManager.setToken(response);
                    var intent = new Intent(mContext, HomeActivity.class);
                    Utils.hideProgressBar();
                    startActivity(intent);
                    finish();
                },
                error -> {
                    Utils.hideProgressBar();
                    DialogManager.showGeneralMessage(mContext, "Somethings went wrong", "Problem with login");
                });
    }
}
