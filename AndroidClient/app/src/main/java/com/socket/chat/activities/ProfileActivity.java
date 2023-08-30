package com.socket.chat.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.socket.chat.R;
import com.socket.chat.conf.Const;
import com.socket.chat.models.UserResponseDto;
import com.socket.chat.util.DialogManager;
import com.socket.chat.util.NetworkClient;
import com.socket.chat.util.PreferenceManager;
import com.socket.chat.util.Utils;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProfileActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private TextView userName;
    private Context mContext;
    private ImageView profilePicture;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mContext = this;
        var newPic = findViewById(R.id.newPic);
        profilePicture = findViewById(R.id.profilePicture);
        userName = findViewById(R.id.userName);
        var backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(view -> {
            finish();
        });
        newPic.setOnClickListener(view -> {
            // Launch the image picker
            var intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });
        getUser();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            var selectedImageUri = data.getData();
            try {
                var inputStream = getContentResolver().openInputStream(selectedImageUri);
                var imageData = Utils.getBytes(inputStream);
                profilePicture.setImageBitmap(BitmapFactory.decodeByteArray(imageData, 0, imageData.length));
                uploadImage(imageData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(byte[] imageData) {
        var url = Const.rest_address + "/api/v1/user/picture"; // Replace with the actual upload URL
        var authToken = PreferenceManager.getToken(); // Replace with the actual auth token

        var client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", "image.jpg", RequestBody.create(MediaType.parse("image/jpeg"), imageData))
                .build();

        var request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + authToken)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                // Handle error
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    var responseBody = response.body().string();
                    var uploadSuccess = Boolean.parseBoolean(responseBody);
                    // Handle success or failure
                } else {
                    // Handle non-success response
                }
            }
        });
    }

    public void getUser() {
        var queue = Volley.newRequestQueue(mContext);
        var networkClient = new NetworkClient(queue);
        var apiUrl = Const.rest_address + "/api/v1/selfUser";
        networkClient.makeGetRequest(apiUrl,
                response -> {
                    var userResponseDto = new Gson().fromJson(response, UserResponseDto.class);
                    userName.setText(userResponseDto.getName());
                    if(StringUtils.length(userResponseDto.getAvatar()) > 0) {
                        profilePicture.setImageBitmap(Utils.base64ToBitmap(userResponseDto.getAvatar()));
                    }
                },
                error -> {
                    Utils.hideProgressBar();
                    DialogManager.showGeneralMessage(mContext, "Somethings went wrong", "Can't parse user list");
                });
    }
}
