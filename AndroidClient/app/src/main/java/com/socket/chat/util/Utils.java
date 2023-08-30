package com.socket.chat.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Utils {
    public static SimpleDateFormat recentChatTimeFormat = new SimpleDateFormat("h:mm a", Locale.US);

    public static PreferenceManager preferenceManager;
    private static ProgressBar progressBar;
    public static String currentChatUserAvatar;

    public static void showProgressBar(Context context, ViewGroup parentView) {
        if (progressBar == null) {
            progressBar = new ProgressBar(context, null,
                    android.R.style.Widget_ProgressBar_Horizontal);
            progressBar.setVisibility(View.VISIBLE);
            parentView.addView(progressBar);
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public static void hideProgressBar() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }
    public static Bitmap base64ToBitmap(String base64Image) {
        var imageBytes = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }
    public static Bitmap drawableToBitmap (Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        var width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        var height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        var bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        var canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
    public static byte[] getBytes(InputStream inputStream) throws IOException {
        var byteBuffer = new ByteArrayOutputStream();
        var bufferSize = 1024;
        var buffer = new byte[bufferSize];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}
