package com.viniciusfinger.eazybank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;


import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        //http.authorizeHttpRequests((requests) -> requests.anyRequest().denyAll()); Negar todas requisições
        //http.authorizeHttpRequests((requests) -> requests.anyRequest().permitAll()); Permitir todas requisições


        return http.authorizeHttpRequests(requests ->
                requests
                        .requestMatchers("/notices/**", "/contacts/**", "/customers/**").permitAll()
                        .requestMatchers("/accounts/**", "/balances/**", "/loans/**", "/cards/**").authenticated()
                )
                .formLogin(withDefaults())
                .httpBasic(withDefaults())
                .csrf().disable()
                .build();
    }

    //Spring standard JDBC Authentication
//    @Bean
//    public UserDetailsService userDetailsManager(DataSource dataSource){
//        return new JdbcUserDetailsManager(dataSource);
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
