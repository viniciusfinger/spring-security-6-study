package com.viniciusfinger.eazybank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class CustomerDTO {

    private Long id;
    private String name;
    private String email;
    private String mobileNumber;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String pwd;

    private String role;
    private Date createDt;

}
