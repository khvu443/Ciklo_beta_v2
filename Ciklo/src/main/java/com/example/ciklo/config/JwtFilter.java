package com.example.ciklo.config;

import com.example.ciklo.service.admin.AdminDetailServiceImpl;
import com.example.ciklo.service.customer.CustomerDetailServiceImpl;
import com.example.ciklo.service.driver.DriverDetailServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService service;
    private final CustomerDetailServiceImpl cusDetailService;
    private final AdminDetailServiceImpl adminDetailService;
    private final DriverDetailServiceImpl driverDetailService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        String authHeader = null;
        String reAuthHeader = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("access")) {
                    authHeader = cookie.getValue();
                    log.info("Get token access from cookie {}", authHeader);
                }
            }
        }

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refresh")) {
                    reAuthHeader = cookie.getValue();
                    log.info("Get token refresh access from cookie {}", reAuthHeader);
                }
            }
        }

        final String jwt;
        final String userEmail;
        if (authHeader == null && reAuthHeader == null) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
//            jwt = authHeader.substring(7);
            if (authHeader == null) {
                jwt = reAuthHeader;
            } else {
                jwt = authHeader;
            }
            userEmail = service.extractUsername(jwt); // extract the userEmail from jwt token;
            log.info("Extract JWT to get email : {}", userEmail);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // Check if account is driver, customer, or admin
                UserDetails user = null;
                if (this.cusDetailService.loadUserByUsername(userEmail) != null) {
                    log.info("Filter customer");
                    user = this.cusDetailService.loadUserByUsername(userEmail);
                } else if (this.adminDetailService.loadUserByUsername(userEmail) != null) {
                    log.info("Filter admin");
                    user = this.adminDetailService.loadUserByUsername(userEmail);
                } else if (this.driverDetailService.loadUserByUsername(userEmail) != null) {
                    log.info("Filter driver");
                    user = this.driverDetailService.loadUserByUsername(userEmail);
                }

                log.info("Filter pass with email: {} and role(s): {} ", user.getUsername(), user.getAuthorities());

                if (service.isTokenValid(jwt, user)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            user.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.info("set context holder - " + SecurityContextHolder.getContext().getAuthentication().getName());
                    filterChain.doFilter(request, response);
                }
            }

        } catch (Exception ex) {
            log.error("Something wrong is happen {}", ex.getMessage());
            response.setHeader("ERROR", ex.getMessage());
            response.setStatus(FORBIDDEN.value());
            Map<String, String> error = new HashMap<>();
            error.put("ERROR_MSG", ex.getMessage());
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), error);
        }

    }
}
