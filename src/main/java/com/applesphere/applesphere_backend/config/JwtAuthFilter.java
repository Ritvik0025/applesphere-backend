package com.applesphere.applesphere_backend.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final ApplicationContext applicationContext;

    public JwtAuthFilter(JwtUtil jwtUtil, ApplicationContext applicationContext) {
        this.jwtUtil = jwtUtil;
        this.applicationContext = applicationContext;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // Step 1 — get Authorization header
        String authHeader = request.getHeader("Authorization");

        // Step 2 — check if header exists and starts with Bearer
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Step 3 — extract token (remove "Bearer " prefix)
        String token = authHeader.substring(7);

        // Step 4 — validate token
        if (jwtUtil.validateToken(token)) {

            // Step 5 — extract email from token
            String email = jwtUtil.extractEmail(token);

            // Step 6 — load user from database
            UserDetailsService userDetailsService =
                applicationContext.getBean(UserDetailsService.class);
            UserDetails userDetails =
                userDetailsService.loadUserByUsername(email);

            // Step 7 — set user as authenticated
            UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
                );
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        // Step 8 — continue to next filter
        filterChain.doFilter(request, response);
    }
}