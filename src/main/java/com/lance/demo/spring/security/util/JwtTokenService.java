package com.lance.demo.spring.security.util;

import com.lance.demo.spring.security.auth.JwtUserDetails;
import com.lance.demo.spring.security.config.JwtTokenProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClock;
import io.jsonwebtoken.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;


@Component
public class JwtTokenService {
    private final Clock clock = DefaultClock.INSTANCE;
    private final String JWT_ROLES_KEY = "role";
    private static final String TOKEN_PREFIX = "Bearer ";
    @Autowired
    private JwtTokenProperties jwtTokenProperties;

    public boolean isExpired(String token) {
        Date expire = getClaims(token).getExpiration();
        return expire.after(clock.now());
    }

    public Date getIssueDate(String token) {
        return getClaims(token).getIssuedAt();
    }

    public String parseToken(String tokenWithBearer) {
        if (tokenWithBearer !=null && tokenWithBearer.startsWith(TOKEN_PREFIX)) {
            return tokenWithBearer.substring(7);
        }
        return null;
    }

    public String generateToke(UserDetails userDetails) {
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        List<String> roles = new ArrayList<>();
        authorities.forEach(authority->roles.add(authority.getAuthority()));
        Date expire = new Date(System.currentTimeMillis()+jwtTokenProperties.getExpire()*1000);

        return TOKEN_PREFIX + Jwts.builder()
                .setAudience(jwtTokenProperties.getAudience())
                .setIssuedAt(clock.now())
                .setExpiration(expire)
                .setSubject(userDetails.getUsername())
                .setIssuer(jwtTokenProperties.getIssuer())
                .claim(JWT_ROLES_KEY, Strings.arrayToDelimitedString(roles.toArray(),","))
                .signWith(SignatureAlgorithm.HS256, jwtTokenProperties.getKey())
                .compact();
    }

    public UserDetails parseUserDetail(String token) {
        if (token == null) {
            return null;
        }
        String username = getClaims(token).getSubject();
        String roles = getClaims(token).get(JWT_ROLES_KEY, String.class);
        String[] roleList = null;
        if (!StringUtils.isEmpty(roles)) {
            roleList = roles.split(",");
        }
        return new JwtUserDetails(username,roleList);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtTokenProperties.getKey())
                .parseClaimsJws(token)
                .getBody();
    }

    private Header getHeader(String token) {
        return Jwts.parser()
                .setSigningKey(jwtTokenProperties.getKey())
                .parseClaimsJws(token)
                .getHeader();
    }
}
