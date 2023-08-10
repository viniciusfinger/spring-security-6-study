package com.viniciusfinger.eazybank.filter;

import jakarta.servlet.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

@Slf4j
public class AuthoritiesLoggingFilter implements Filter {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null){
            log.info("User {} is successfully authenticated and has the following authorities: {}",
                    authentication.getName(),
                    authentication.getAuthorities().toString()
            );
        }

        chain.doFilter(request, response);
    }
}
