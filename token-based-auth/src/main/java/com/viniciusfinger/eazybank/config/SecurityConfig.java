package com.viniciusfinger.eazybank.config;

import com.viniciusfinger.eazybank.filter.AuthoritiesLoggingFilter;
import com.viniciusfinger.eazybank.filter.CsrfCookieFilter;
import com.viniciusfinger.eazybank.filter.RequestValidationBeforeFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        CsrfTokenRequestAttributeHandler csrfRequestHandler = new CsrfTokenRequestAttributeHandler();
        csrfRequestHandler.setCsrfRequestAttributeName("_csrf");

        http
                .sessionManagement(sessionManagementConfig -> {
                    sessionManagementConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS); //Informando ao spring security que eu mesmo vou me encarregar de cuidar do gerenciamento de sessão, não usar jssessionid
                })
                .cors(corsConfig -> corsConfig.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
                    config.setAllowedMethods(Arrays.asList("*"));
                    config.setAllowedHeaders(Arrays.asList("*"));
                    config.setExposedHeaders(Arrays.asList("Authorization")); //Expondo o token JWT para o client
                    config.setAllowCredentials(true);
                    config.setMaxAge(3600L); //24h
                    return config;
                }))
                .csrf(csrfConfig -> csrfConfig
                        .csrfTokenRequestHandler(csrfRequestHandler)
                        .ignoringRequestMatchers("/contacts/**", "/auth/**") //ignore csrf protection for this public paths that contains PUT/POST methods
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class) //adicionando o filtro que adiciona o cabeçalho do csrf token
                .addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new AuthoritiesLoggingFilter(), BasicAuthenticationFilter.class)
                .authorizeHttpRequests(requests ->
                requests
                        .requestMatchers("/accounts/**").hasAuthority("VIEWACCOUNT") //hasAutority precisa de autenticação + aquela authority
                        .requestMatchers("/balances/**").hasAnyAuthority("VIEWACCOUNT", "VIEWBALANCE")
                        .requestMatchers("/loans/**").hasAuthority("VIEWLOANS")
                        .requestMatchers("/cards/**").hasAuthority("VIEWCARDS")
                        .requestMatchers("/notices/**", "/contacts/**", "/auth").permitAll()
                        .requestMatchers("/users/**").authenticated() //não precisa ter uma authority especifica, apenas estar autenticado.
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
