package com.viniciusfinger.eazybank.config;

import com.viniciusfinger.eazybank.filter.CsrfCookieFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import java.util.Arrays;
import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        CsrfTokenRequestAttributeHandler csrfRequestHandler = new CsrfTokenRequestAttributeHandler();
        csrfRequestHandler.setCsrfRequestAttributeName("_csrf");

        http
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
                    config.setAllowedMethods(Arrays.asList("*"));
                    config.setAllowCredentials(true);
                    config.setAllowedHeaders(Arrays.asList("*"));
                    config.setMaxAge(3600L); //24h
                    return config;
                }))
                .csrf(csrf -> {
                    csrf
                            .csrfTokenRequestHandler(csrfRequestHandler)
                            .ignoringRequestMatchers("/contacts/**", "/auth/**") //ignore csrf protection for this public paths that contains PUT/POST methods
                            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
                })
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class) //adicionando o filtro que adiciona o cabeçalho do csrf token
                .authorizeHttpRequests(requests ->
                requests
                        .requestMatchers("/notices/**", "/contacts/**", "/auth/**").permitAll()
                        .requestMatchers("/accounts/**", "/balances/**", "/loans/**", "/cards/**", "/users/**").authenticated()
                )
                .formLogin(withDefaults())
                .httpBasic(withDefaults());

        return http.build();
    }

    //Spring standard JDBC user details
//    @Bean
//    public UserDetailsService userDetailsManager(DataSource dataSource){
//        return new JdbcUserDetailsManager(dataSource);
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
