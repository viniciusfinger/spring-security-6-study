package com.viniciusfinger.eazybank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        //http.authorizeHttpRequests((requests) -> requests.anyRequest().denyAll()); Negar todas requisições
        //http.authorizeHttpRequests((requests) -> requests.anyRequest().permitAll()); Permitir todas requisições


        return http.authorizeHttpRequests((requests) ->
                requests
                        .requestMatchers("/accounts/**", "balances/**", "/loans/**", "/cards/**").authenticated()
                        .requestMatchers("/notices/**", "/contacts/**").permitAll()
                )
                .formLogin(withDefaults())
                .httpBasic(withDefaults())
                .build();
    }
}
