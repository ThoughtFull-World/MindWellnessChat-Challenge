package com.socket.chat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

   @Bean
   public PasswordEncoder passwordEncoder() {
      // For demonstration purposes only. Use a proper PasswordEncoder in production.
      return new BCryptPasswordEncoder();
   }

   @Bean
   public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
      http
            .authorizeRequests(authorizeRequests ->
                  authorizeRequests
                        .antMatchers("/**").permitAll() // Allow all access
//                        .antMatchers("/public/**").permitAll() // Example: public resources
//                        .anyRequest().authenticated()
            )
            .csrf().disable() // Disable CSRF for simplicity
            .headers().frameOptions().disable(); // Disable frame options for H2 console (if used)

      return http.build();
   }
}

