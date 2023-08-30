package com.socket.chat.config;

import com.socket.chat.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtTokenUtil jwtTokenUtil;
    /**
     * Register STOMP's endpoint
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        stompEndpointRegistry.addEndpoint("/im").addInterceptors(new HandshakeInterceptor(jwtTokenUtil)).withSockJS();
    }

    /**
     * Configure message broker
     * <p>
     * Configure the use of message brokers
     *
     * @param registry message broker registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Configure global message broker, client will subscribe these message brokers to receive messages
        // Unified configuration of the message agent, the message agent is the subscription point, and the client receives the message by subscribing to the message agent point
        registry.enableSimpleBroker("/b", "/g", "/user");

        // configure point to point message's prefix
        // Configure the prefix for point-to-point messages
        registry.setUserDestinationPrefix("/user");
    }
}
