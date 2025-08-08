package com.example.fileintegrity.util;

import com.example.fileintegrity.model.GenericResponse;
import com.example.fileintegrity.service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService implements TokenService {

    @Value("${jwt-secret}")
    private String secret;


    public String generateToken(String username) {
        Map<String,Object> claims = new HashMap<>();
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000*60*60))
                .signWith(getSignKey())
                .compact();
    }
    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUser(String token){
        Claims claims = Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    private Date extractExpiration(String token){
        Claims claims = Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getExpiration();
    }

    public Boolean validateToken(String token, UserDetails userDetails){
        String username = extractUser(token);
        Date expiration = extractExpiration(token);
        return username.equals(userDetails.getUsername()) && expiration.after(new Date());
    }

    public ResponseCookie createJwtCookie(String value, int maxAge, String path) {
        return ResponseCookie.from("jwt", value)
                .sameSite("Lax")
                .secure(false)
                .httpOnly(false)
                .maxAge(maxAge)
                .path(path)
                .build();
    }

    public void resetJwtCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);
    }

    public void JwtCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public ResponseEntity<GenericResponse<String>> logout(HttpServletResponse response) {
        resetJwtCookie(response);

        HttpHeaders responseHeaders = new HttpHeaders();
        String domainName = "localhost";
        String setCookieValue = "jwt=;" +
                "Domain=" + domainName + "; " +
                "Path=/; " +
                "HttpOnly; " +
                "Secure; " +
                "SameSite=None; " +
                "Expires=Thu, 01 Jan 1970 00:00:00 GMT";

        responseHeaders.add(HttpHeaders.SET_COOKIE, setCookieValue);

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(new GenericResponse<>(200, "JWT successfully reset", "Logout successful"));
    }


    public String extractClaim(String token, String claimKey) {
        Claims claims = Jwts.parser()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get(claimKey, String.class);
    }
}
