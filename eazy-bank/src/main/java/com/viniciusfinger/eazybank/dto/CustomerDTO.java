package com.viniciusfinger.eazybank.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CustomerDTO {

    private Long id;
    private String name;
    private String email;
    private String mobileNumber;
    private String pwd;
    private String role;
    private Date createDt;

}
