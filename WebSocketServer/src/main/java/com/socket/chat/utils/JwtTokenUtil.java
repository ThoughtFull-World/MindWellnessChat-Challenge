package com.socket.chat.utils;

import com.socket.chat.model.entity.Users;
import com.socket.chat.repository.BlackListedTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Component
@RequiredArgsConstructor
public class JwtTokenUtil implements Serializable {
    private final BlackListedTokenRepository blackListedTokenRepository;
    private static final long serialVersionUID = -2550185165626007488L;

    private static final long JWT_TOKEN_VALIDITY = 30 * 24 * 60 * 60;
    public static final String JWT_DATA_USER_NAME = "user_name";
    public static final String JWT_DATA_ID = "ID";
    @Value("${jwt.secret}")
    private String secret;

    public String getUserIdFromToken(String token) {
        if(token.startsWith("Bearer "))
            token = token.substring(7);
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        if(token.startsWith("Bearer "))
            token = token.substring(7);
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        if(token.startsWith("Bearer "))
            token = token.substring(7);
        final var claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    //for retrieving any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        if(token.startsWith("Bearer "))
            token = token.substring(7);
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public Map<String, Object> getDataFromToken(String token) {
        if(token.startsWith("Bearer "))
            token = token.substring(7);
        var claims = getAllClaimsFromToken(token);
        return (Map<String, Object>) claims.get("data");
    }

    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        if(token.startsWith("Bearer "))
            token = token.substring(7);
        final var expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //generate token for user
    public String generateToken(Users user) {
        var claims = new HashMap<String, Object>();
        var userData =  new HashMap<String, Object>();
        userData.put(JWT_DATA_ID, user.getId());
        userData.put(JWT_DATA_USER_NAME, user.getName());
        claims.put("data", userData);
        return doGenerateToken(claims, user.getId());
    }

    private String doGenerateToken(Map<String, Object> claims, Long subject) {
        return Jwts.builder().setClaims(claims).setSubject(String.valueOf(subject)).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    //validate token
    public Boolean validateToken(String token) {
        if(token.startsWith("Bearer "))
            token = token.substring(7);
        if(blackListedTokenRepository.findByToken(token).isPresent()) {
            return false;
        }
        return (!isTokenExpired(token));
    }
}