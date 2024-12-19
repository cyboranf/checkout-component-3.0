package com.component.checkout.infrastructure.security;

import com.component.checkout.service.TimeProvider;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private SecretKey jwtSecret;

    private final TimeProvider timeProvider;
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public JwtTokenProvider(TimeProvider timeProvider, @Lazy UserDetailsServiceImpl userDetailsService) {
        this.timeProvider = timeProvider;
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    private void init() {
        jwtSecret = generateSecretKey(secret);
    }

    public String generateToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        Date now = timeProvider.now();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .claim("roles", extractRoles(userPrincipal))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(jwtSecret, SignatureAlgorithm.HS512)
                .compact();
    }

    public String resolveToken(HttpServletRequest request) {
        String token = extractTokenFromHeader(request);
        return token != null ? token : extractTokenFromCookies(request);
    }

    public String getUsername(String token) {
        return parseToken(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseToken(token);
        Collection<? extends GrantedAuthority> authorities = extractAuthorities(claims);
        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

    private SecretKey generateSecretKey(String secret) {
        byte[] decodedKey = Base64.getDecoder().decode(secret.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA512");
    }

    private Set<String> extractRoles(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        return (bearerToken != null && bearerToken.startsWith("Bearer "))
                ? bearerToken.substring(7)
                : null;
    }

    private String extractTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Collection<? extends GrantedAuthority> extractAuthorities(Claims claims) {
        List<String> roles = (List<String>) claims.get("roles");
        return roles.stream()
                .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role) // Ensure "ROLE_" prefix
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}