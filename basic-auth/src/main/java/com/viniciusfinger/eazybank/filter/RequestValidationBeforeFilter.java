package com.viniciusfinger.eazybank.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;


public class RequestValidationBeforeFilter implements Filter {

    public static final String AUTHENTICATION_SCHEME_BASIC = "Basic";
    private static final Charset CREDENTIALS_CHARSET = StandardCharsets.UTF_8;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String authHeader = req.getHeader(AUTHORIZATION);

        if (authHeader != null) {
            authHeader = authHeader.trim();

            if (isBasicAuth(authHeader)) {
                byte[] base64Token = authHeader.substring(6).getBytes(CREDENTIALS_CHARSET);
                byte[] decoded;

                try {

                    decoded = Base64.getDecoder().decode(base64Token);
                    String token = new String(decoded, CREDENTIALS_CHARSET);
                    int delimiter = token.indexOf(":");

                    if (delimiter == -1) {
                        throw new BadCredentialsException("Invalid basic authentication token");
                    }

                    String email = token.substring(0, delimiter);

                    if (email.toLowerCase().contains("test")) {
                        res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        return;
                    }

                } catch (IllegalArgumentException e) {
                    throw new BadCredentialsException("Failed to decode basic authentication token");
                }
            }
        }

        chain.doFilter(request, response);
    }

    private boolean isBasicAuth(String authHeader){
        return StringUtils.startsWithIgnoreCase(authHeader, AUTHENTICATION_SCHEME_BASIC);
    }
}