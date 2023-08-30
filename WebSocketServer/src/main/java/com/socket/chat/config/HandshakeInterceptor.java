package com.socket.chat.config;

import com.socket.chat.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class HandshakeInterceptor extends HttpSessionHandshakeInterceptor {

    private final JwtTokenUtil jwtTokenUtil;
    /**
     * Before websocket handshake
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        log.info("HandshakeInterceptor: beforeHandshake");
        log.info("Attributes: " + attributes.toString());
        var jwtToken = request.getURI().getQuery().substring(6);
        if (!StringUtils.hasLength(jwtToken)) {
            return false;
        }
        if(Boolean.TRUE.equals(jwtTokenUtil.validateToken(jwtToken))) {
            //TODO need to catch the ID from destination, So we can match the token
//            if(jwtTokenUtil.getUserIdFromToken(jwtToken).equalsIgnoreCase(request.getURI().getQuery().toString())) {
                return super.beforeHandshake(request, response, wsHandler, attributes);
//            } else {
//                return false;
//            }
        } else {
            return false;
        }
    }

    /**
     * After websocket handshake
     */
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        log.info("HandshakeInterceptor: afterHandshake");
        super.afterHandshake(request, response, wsHandler, ex);
    }

}
