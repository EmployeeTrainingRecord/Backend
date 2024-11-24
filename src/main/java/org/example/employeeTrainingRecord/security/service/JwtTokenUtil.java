package org.example.employeeTrainingRecord.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.example.employeeTrainingRecord.database2.entities.Users;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil implements Serializable {

    @Value("${jwt.secret}")
    private String SECRET_KEY;
    @Value("#{${jwt.max-token-interval-min}*60*1000}")
    private long JWT_TOKEN_VALIDITY;
    @Value("#{${jwt.refresh-token-interval-min} *60*60*1000}")
    private long JWT_TOKEN_REFRESH_VALIDITY;

    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public Claims getAllClaimsFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        return claims;
    }


    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(Users userDetails, String tokenType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("name", userDetails.getName());
        claims.put("oid", userDetails.getOid());
        claims.put("role", userDetails.getRole());
        if(tokenType.equalsIgnoreCase("refresh_token")) {
            return doGenerateRefreshToken(claims, userDetails.getUsername());
        }else {
            return doGenerateToken(claims, userDetails.getUsername());
        }
    }

    private String doGenerateRefreshToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setHeaderParam("typ", "JWT").
                setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_REFRESH_VALIDITY))
                .signWith(signatureAlgorithm, SECRET_KEY).compact();
    }


    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setHeaderParam("typ", "JWT").
                setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(signatureAlgorithm, SECRET_KEY).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    public boolean isValidToken(Authentication authentication) {
        if (authentication == null || !(authentication instanceof UsernamePasswordAuthenticationToken)) {
            return false;
        }
        String token = (String) authentication.getCredentials();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return validateToken(token, userDetails);
    }
}
