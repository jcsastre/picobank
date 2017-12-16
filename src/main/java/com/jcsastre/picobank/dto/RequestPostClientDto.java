package com.jcsastre.picobank.dto;

import lombok.Value;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Value
public class RequestPostClientDto {

    @NotEmpty(message = "email can't be empty")
    @Email(message = "email has to be a well-formed email address")
    private String email;

    @NotEmpty(message = "password can't be empty")
    @NotNull
    private String password;
}
