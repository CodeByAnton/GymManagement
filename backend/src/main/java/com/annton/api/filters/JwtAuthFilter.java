package com.annton.api.filters;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.annton.api.configuration.security.SecurityConfig;
import com.annton.api.services.JwtService;
import com.annton.api.data.enums.TokenType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private static final Logger logger = LogManager.getLogger(JwtAuthFilter.class);
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    private void writeUnauthorizedResponse(@NonNull HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(message);
        response.getWriter().flush();
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request
            , @NonNull HttpServletResponse response
            , @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        logger.info("--- START REQUEST PROCESSING EXECUTION ---");
        String requestUri = request.getRequestURI();

        String[] staticResources = {".html", ".css", ".jpg", ".jpeg", ".js", ".png"};

        for(var staticResource : staticResources) {
            if(requestUri.endsWith(staticResource)) {
                logger.info("Request to static resource {} {}. Skipping filter...",
                        request.getMethod(), requestUri);
                filterChain.doFilter(request, response);
                return;
            }
        }

        //if list of open uris includes requested uri -> skip filter
        for (String pattern : SecurityConfig.openUris) {
            String regexPattern = pattern.replace("**", ".*");
            if (Pattern.compile(regexPattern).matcher(requestUri).matches()) {
                logger.info("Request to open endpoint {} {}. Skipping filter...",
                        request.getMethod(), requestUri);
                filterChain.doFilter(request, response);
                return;
            }
        }


        for(Map.Entry<String, HttpMethod> entry : SecurityConfig.urlMethodOpen.entrySet()) {
            if(entry.getValue().toString().equals(request.getMethod()) &&
                entry.getKey().equals(request.getRequestURI())) {
                logger.info("Request to open endpoint {} {}. Skipping filter...", request.getMethod(), requestUri);
                filterChain.doFilter(request, response);
                return;
            }
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Access token is absent or not in the correct format
            // go user+password authentication
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.split(" ")[1];
        //if token is expired or forged send Error
        DecodedJWT decodedJWT;
        try{
            decodedJWT = jwtService.decodeAndValidateToken(jwt);
        }catch (JWTVerificationException e){
            logger.error("Attempt to use forged ot invalid access token");
            writeUnauthorizedResponse(response, "invalid token");
            return;
        }

        String username = jwtService.getEmailFromToken(decodedJWT);

        TokenType type = jwtService.getTypeFromToken(decodedJWT);

        if (type.equals(TokenType.ACCESS)) {
            if (username != null) {
                UserDetails user = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }else{
            logger.error("Attempt to use refresh token instead of access token");
            writeUnauthorizedResponse(response, "refresh token cant be used as an access token");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
