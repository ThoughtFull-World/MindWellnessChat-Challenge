package com.socket.chat.util;

import com.google.gson.Gson;

import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class JwtUtils {

    public static Map<String, Object> parseJwtPayload(String jwtToken) {
        try {
            var parts = jwtToken.split("\\.");
            var payload = parts[1]; // Get the payload part

            // Decode URL-safe base64
            var decodedBytes = Base64.decodeBase64(payload);

            var decodedPayload = new String(decodedBytes, StandardCharsets.UTF_8);
            return new Gson().fromJson(decodedPayload, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

