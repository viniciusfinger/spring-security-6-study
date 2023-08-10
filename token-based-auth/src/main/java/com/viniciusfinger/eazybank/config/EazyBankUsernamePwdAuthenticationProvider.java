package com.viniciusfinger.eazybank.config;

import com.viniciusfinger.eazybank.model.Authority;
import com.viniciusfinger.eazybank.model.Customer;
import com.viniciusfinger.eazybank.repository.CustomerRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class EazyBankUsernamePwdAuthenticationProvider implements AuthenticationProvider {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public EazyBankUsernamePwdAuthenticationProvider(
            CustomerRepository customerRepository,
            PasswordEncoder passwordEncoder
    ){
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName(); //username, email, ...
        String password = authentication.getCredentials().toString(); //credentials = password

        List<Customer> customerList = customerRepository.findByEmail(email);

        if (customerList.isEmpty()){
            throw new BadCredentialsException("No user with this e-mail.");
        }

        Customer customer = customerList.get(0);

        if(passwordEncoder.matches(password, customer.getPwd())){
            Set<GrantedAuthority> grantedAuthorities = this.getGrantedAuthorities(customer.getAuthorities());
            return new UsernamePasswordAuthenticationToken(email, password, grantedAuthorities);
        } else {
            throw new BadCredentialsException("Invalid password.");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private Set<GrantedAuthority> getGrantedAuthorities(Set<Authority> authorities){
        return authorities
                .stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                .collect(Collectors.toSet());
    }
}
