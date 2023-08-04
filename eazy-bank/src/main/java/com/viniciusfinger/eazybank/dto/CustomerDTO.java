package com.viniciusfinger.eazybank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.viniciusfinger.eazybank.model.Authority;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class CustomerDTO {

    private Long id;
    private String name;
    private String email;
    private String mobileNumber;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String pwd;

    private String role;

    private Set<Authority> authorities;

    private Date createDt;

}
