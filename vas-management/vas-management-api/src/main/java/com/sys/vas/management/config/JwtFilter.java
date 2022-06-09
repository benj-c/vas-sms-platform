package com.sys.vas.management.config;

import com.sys.vas.management.dto.JwtDataDto;
import com.sys.vas.management.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (Arrays.stream(SecurityConfig.PERMITTED_URIS).anyMatch(e -> e.equals(request.getServletPath()))) {
            log.info("Permitted URI, skipping jwt filter");
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("Auth header value: {}", authHeader);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.info("invalid auth header value");
            filterChain.doFilter(request, response);
            return;
        }

        //Bearer <tkn>
        String token = authHeader.split(" ")[1];
        if (token == null) {
            log.info("token not found");
            filterChain.doFilter(request, response);
            return;
        }

        //token
        JwtDataDto jwtData = null;
        try {
            jwtData = jwtUtil.validate(token);
        } catch (InvalidJwtException e) {
            log.error("InvalidJwt", e);
            filterChain.doFilter(request, response);
            return;
        } catch (MalformedClaimException e) {
            log.error("MalformedClaim", e);
            filterChain.doFilter(request, response);
            return;
        }

        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority(jwtData.getRole()));

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                jwtData.getUserName(),
                null,
                authorityList
        );
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
