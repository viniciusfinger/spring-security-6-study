package com.viniciusfinger.eazybank.controller;

import com.viniciusfinger.eazybank.dto.CustomerDTO;
import com.viniciusfinger.eazybank.model.Customer;
import com.viniciusfinger.eazybank.repository.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    public AuthController(
            CustomerRepository customerRepository,
            ModelMapper modelMapper,
            PasswordEncoder passwordEncoder
    ){
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> create(@RequestBody CustomerDTO customerDTO){
        Customer customer = modelMapper.map(customerDTO, Customer.class);
        ResponseEntity<String> response = null;

        try {
            String hashPassword = passwordEncoder.encode(customer.getPwd());
            customer.setPwd(hashPassword);
            customer.setCreateDt(new Date());
            Customer persistedCustomer = customerRepository.save(customer);


            if (persistedCustomer.getId() > 0){
                response = ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body("Customer successfully created");
            }

        } catch (Exception e){
            response = ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An exception occurred due to " + e.getMessage());
        }

        return response;
    }

    @GetMapping
    public CustomerDTO getUserDetailsAfterLogin(Authentication authentication){
        List<Customer> customerList = customerRepository.findByEmail(authentication.getName());

        if (!customerList.isEmpty()){
            return modelMapper.map(customerList.get(0), CustomerDTO.class);
        }

        return null;

    }
}
