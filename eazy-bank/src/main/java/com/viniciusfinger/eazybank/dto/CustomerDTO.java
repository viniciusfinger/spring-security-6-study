package com.viniciusfinger.eazybank.dto;

import lombok.Data;

@Data
public class CustomerDTO {

    private Long id;
    private String email;
    private String pwd;
    private String role;

}
