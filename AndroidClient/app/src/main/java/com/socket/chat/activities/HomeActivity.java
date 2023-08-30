package com.socket.chat.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.socket.chat.R;
import com.socket.chat.adapters.HomeAdapter;
import com.socket.chat.conf.Const;
import com.socket.chat.models.UserResponseDto;
import com.socket.chat.util.DialogManager;
import com.socket.chat.util.NetworkClient;
import com.socket.chat.util.PreferenceManager;
import com.socket.chat.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private HomeAdapter homeAdapter;
    private List<UserResponseDto> userResponseDtoList;
    private RecyclerView contactList;
    private Context mContext;
    private NavigationView navigationView;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mContext = this;
        userResponseDtoList = new ArrayList<>();

        var toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Socket chat");
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        var toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        homeAdapter = new HomeAdapter(mContext, userResponseDtoList) {
            @Override
            public void onClickUser(Long id, String userName, String avatar) {
                var intent = new Intent(mContext, UserChatActivity.class);
                intent.putExtra("CHAT_USER_ID", id);
                intent.putExtra("CHAT_USER_NAME", userName);
                // Need to send in parcelable
                Utils.currentChatUserAvatar = avatar;
                startActivity(intent);
            }
        };

        contactList = findViewById(R.id.contactList);
        contactList.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        contactList.addItemDecoration(new
                DividerItemDecoration(mContext,LinearLayoutManager.VERTICAL));
        contactList.setLayoutManager(mLayoutManager);
        contactList.setAdapter(homeAdapter);


        var queue = Volley.newRequestQueue(mContext);
        var networkClient = new NetworkClient(queue);
        var apiUrl = Const.rest_address+"/api/v1/user?page=1&size=1000";
        networkClient.makeGetRequest(apiUrl,
                response -> {
                    var typeToken = new TypeToken<List<UserResponseDto>>() {};
                    userResponseDtoList.addAll(new Gson().fromJson(response, typeToken.getType()));
                    homeAdapter.notifyDataSetChanged();
                },
                error -> {
                    Utils.hideProgressBar();
                    DialogManager.showGeneralMessage(mContext, "Somethings went wrong", "Can't parse user list");
                });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        var id = item.getItemId();

        if (id == R.id.home) {
        }
        if (id == R.id.profile) {
            var intent = new Intent(mContext, ProfileActivity.class);
            intent.putExtra("CHAT_USER_ID", id);
            startActivity(intent);
        }
        if (id == R.id.settings) {
            Toast.makeText(mContext, "Not yet Implemented", Toast.LENGTH_LONG).show();
        }
        if (id == R.id.logout) {
            logout();
        }
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView.getMenu().getItem(0).setChecked(true);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logout() {
        var queue = Volley.newRequestQueue(mContext);
        var networkClient = new NetworkClient(queue);
        var apiUrl = Const.rest_address+"/api/v1/user/logout";
        networkClient.makeGetRequest(apiUrl,
                response -> {
                    PreferenceManager.setToken(response);
                    var intent = new Intent(mContext, LoginActivity.class);
                    Utils.hideProgressBar();
                    startActivity(intent);
                    finish();
                },
                error -> {
                    Utils.hideProgressBar();
                    DialogManager.showGeneralMessage(mContext, "Somethings went wrong", "Can't parse user list");
                });
    }
}
