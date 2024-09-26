package com.carpooling.ums.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.carpooling.ums.services.MyUserDetailsService;
import com.carpooling.ums.utils.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
@Order(1)
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        String path = request.getServletPath();
        logger.info("doFilterInternal: path: {}", path);

        try {
            // Skip filter for login and signup paths
            if ("/login".equals(path) || "/signup".equals(path) || "/verify/user".equals(path)) {
                logger.info("doFilterInternal: path is /login or /signup, skipping filter");
                filterChain.doFilter(request, response);
                return;
            }

            String username = null;
            String jwt = null;

            // Check if Authorization header is present and starts with "Bearer "
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7);  // Extract token after "Bearer "
                username = jwtUtil.extractUsername(jwt);
                logger.info("doFilterInternal: Extracted username: {}", username);
            } else {
                logger.warn("doFilterInternal: Missing or malformed Authorization header");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Authorization header is missing or not formatted correctly.");
                return;
            }

            // Validate the token and set authentication in the context if valid
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                logger.info("doFilterInternal: User details loaded: {}", userDetails);

                if (jwtUtil.validateToken(jwt, username)) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    logger.info("doFilterInternal: Authentication set in SecurityContext");
                } else {
                    logger.warn("doFilterInternal: JWT token validation failed");
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT Token");
                    return;
                }
            }

            // If no JWT or invalid username, set 401 Unauthorized
        } catch (ExpiredJwtException e) {
            logger.error("doFilterInternal: JWT token has expired", e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT token has expired");

        } catch (SignatureException e) {
            logger.error("doFilterInternal: JWT token signature invalid", e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token signature");

        } catch (MalformedJwtException e) {
            logger.error("doFilterInternal: Malformed JWT token", e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Malformed JWT token");

        } catch (IllegalArgumentException e) {
            logger.error("doFilterInternal: Illegal argument while processing JWT token", e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT token");

        } catch (Exception e) {
            logger.error("doFilterInternal: Unexpected error occurred", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred. Please contact support if the issue persists.");
            return;
        }

        filterChain.doFilter(request, response);
    }
}