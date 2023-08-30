package com.socket.chat.util;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class NetworkClient {

    private RequestQueue requestQueue;

    public NetworkClient(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    public void makeJsonPostRequest(String url, final String requestBody,
                                    Response.Listener<String> successListener,
                                    Response.ErrorListener errorListener) {

        var stringRequest = new StringRequest(Request.Method.POST, url,
                successListener, errorListener) {
            @Override
            public byte[] getBody() {
                return requestBody.getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json"; // Specify the content type as JSON
            }
        };
        requestQueue.add(stringRequest);
    }

    public void makeGetRequest(String url,
                               Response.Listener<String> successListener,
                               Response.ErrorListener errorListener) {

        var stringRequest = new StringRequest(Request.Method.GET, url,
                successListener, errorListener) {
            @Override
            public String getBodyContentType() {
                return "application/json"; // Specify the content type as JSON
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + PreferenceManager.getToken()); // Add Bearer token to headers
                return headers;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void makePutRequest(String url,
                               Response.Listener<String> successListener,
                               Response.ErrorListener errorListener) {

        var stringRequest = new StringRequest(Request.Method.PUT, url,
                successListener, errorListener) {


            @Override
            public String getBodyContentType() {
                return "application/json"; // Specify the content type as JSON
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + PreferenceManager.getToken()); // Add Bearer token to headers
                return headers;
            }
        };
        requestQueue.add(stringRequest);
    }
}
