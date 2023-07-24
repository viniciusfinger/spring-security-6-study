package com.viniciusfinger.eazybank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

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

//    @Bean
//    public InMemoryUserDetailsManager userDetailsManger(){
//        UserDetails userAdmin = User.withDefaultPasswordEncoder()
//                .username("admin")
//                .password("admin")
//                .authorities("admin")
//                .build();
//
//        UserDetails userFinger = User.withDefaultPasswordEncoder()
//                .username("finger")
//                .password("123456")
//                .authorities("read")
//                .build();
//
//        return new InMemoryUserDetailsManager(userAdmin, userFinger);
//    }

    @Bean
    public UserDetailsService userDetailsManager(DataSource dataSource){
        return new JdbcUserDetailsManager(dataSource);
    }

}
