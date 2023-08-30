package com.socket.chat.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.socket.chat.R;
import com.socket.chat.conf.Const;
import com.socket.chat.models.UserRequestDto;
import com.socket.chat.util.DialogManager;
import com.socket.chat.util.NetworkClient;
import com.socket.chat.util.Utils;

public class RegisterActivity extends AppCompatActivity {

    private TextView name;
    private TextView password;
    private Context mContext;
    private Button login;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mContext = this;
        name = findViewById(R.id.name);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        login.setOnClickListener(view->{
            var intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
            finish();
        });
        var registration = findViewById(R.id.registration);
        registration.setOnClickListener(view -> {
            if(login.getText().toString().length() > 0 ) {
                if(password.getText().toString().length() > 0 ) {
                    doRegistration();
                } else {
                    password.setError("Please fill");
                }
            } else {
                login.setError("Please fill");
            }
        });
    }

    private void doRegistration() {
        var queue = Volley.newRequestQueue(mContext);
        var networkClient = new NetworkClient(queue);
        var apiUrl = Const.rest_address + "/api/v1/user/register";
        var userRequestDto = new UserRequestDto();
        userRequestDto.setName(name.getText().toString());
        userRequestDto.setPassword(password.getText().toString());
        var jsonString = new Gson().toJson(userRequestDto);
        Utils.showProgressBar(mContext, (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content));
        networkClient.makeJsonPostRequest(apiUrl, jsonString,
                response -> {
                    Utils.hideProgressBar();
                    showGeneralMessage("Registration Completed", "You have successfully registered");
                },
                error -> {
                    Utils.hideProgressBar();
                    DialogManager.showGeneralMessage(mContext, "Somethings went wrong", "Problem with Registration");
                });
    }

    public void showGeneralMessage(String tittle, String message){
        new AlertDialog.Builder(mContext)
                .setTitle(tittle)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("ok", (dialog, which) -> {
                    dialog.dismiss();
                    var intent = new Intent(mContext, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }).show();
    }
}
