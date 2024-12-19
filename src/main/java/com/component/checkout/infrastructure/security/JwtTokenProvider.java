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

/**
 * JwtTokenProvider is responsible for generating, resolving, and validating JWT tokens for authenticated users.
 */
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

    /**
     * Initializes the JWT secret after bean creation.
     */
    @PostConstruct
    private void init() {
        jwtSecret = generateSecretKey(secret);
    }

    /**
     * Generates a JWT token for the given Authentication object.
     *
     * @param authentication The authenticated user's details.
     * @return A signed JWT token.
     */
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

    /**
     * Resolves the JWT token from the request, checking headers first and then cookies.
     *
     * @param request The HttpServletRequest to extract the token from.
     * @return The JWT token or null if not found.
     */
    public String resolveToken(HttpServletRequest request) {
        String token = extractTokenFromHeader(request);
        return token != null ? token : extractTokenFromCookies(request);
    }

    /**
     * Extracts the username from the JWT token.
     *
     * @param token The JWT token.
     * @return The username (subject) inside the token.
     */
    public String getUsername(String token) {
        return parseToken(token).getSubject();
    }

    /**
     * Validates the JWT token for correct signature and expiration.
     *
     * @param token The JWT token to validate.
     * @return true if valid, false otherwise.
     */
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    /**
     * Builds an Authentication object (UserDetails + authorities) from the token.
     *
     * @param token The JWT token.
     * @return An Authentication object representing the user.
     */
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

    @SuppressWarnings("unchecked")
    private Collection<? extends GrantedAuthority> extractAuthorities(Claims claims) {
        List<String> roles = (List<String>) claims.get("roles");
        return roles.stream()
                .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role) // Ensure "ROLE_" prefix
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}