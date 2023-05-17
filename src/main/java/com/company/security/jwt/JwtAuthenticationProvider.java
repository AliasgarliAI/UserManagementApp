package com.company.security.jwt;

import com.company.entity.UserDetailsImpl;
import com.company.mapper.UserMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import static com.company.constant.SecurityConstants.*;

@Component
public class JwtAuthenticationProvider {
    @Value("${jwt.secret-key}")
    private String key;

    public String generateToken(Map<String, Claims> extraClaims, UserDetailsImpl userPrincipal) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setClaims(Map.of(ROLES,userPrincipal.getRoles()))
                .setAudience(TOKEN_SOURCE)
                .setIssuer(TOKEN_ISSUER)
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
                .signWith(getSignInKey(), SignatureAlgorithm.HS512)
                .compact();
    }
    public Authentication getAuthentication(String userName, List<? extends GrantedAuthority> authorities, HttpServletRequest request){
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userName,null,authorities);
        authentication.setDetails( new WebAuthenticationDetailsSource().buildDetails(request));
        return authentication;
    }

    public String generateToken(UserDetailsImpl userPrincipal) {
        return generateToken(new HashMap<>(), userPrincipal);
    }
    @SuppressWarnings(value = "unchecked")
    public List<GrantedAuthority> extractAuthorities(String token) {
        List<String> authorities = (List<String>) extractAllClaims(token).get(AUTHORITIES);
        return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
    public boolean isTokenValid(String token, UserDetailsImpl userDetailsImpl){
        String userName = extractUserName(token);
        return userDetailsImpl.getUsername().equals(userName) && !isTokenExpired(token);
    }
    public Date extractExpirationDate(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    private boolean isTokenExpired(String token){
        return extractExpirationDate(token).before(new Date());
    }
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        return Keys.hmacShaKeyFor(key.getBytes());
    }
}
