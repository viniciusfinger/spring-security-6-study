package com.viniciusfinger.eazybank.controller;

import com.viniciusfinger.eazybank.dto.CustomerDTO;
import com.viniciusfinger.eazybank.model.Customer;
import com.viniciusfinger.eazybank.repository.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    public CustomerController(
            CustomerRepository customerRepository,
            ModelMapper modelMapper,
            PasswordEncoder passwordEncoder
    ){
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody CustomerDTO customerDTO){
        Customer customer = modelMapper.map(customerDTO, Customer.class);
        ResponseEntity<String> response = null;

        try {

            String hashPassword = passwordEncoder.encode(customer.getPwd());
            customer.setPwd(hashPassword);

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
}
