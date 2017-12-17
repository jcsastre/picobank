package com.jcsastre.picobank.dto;

import com.jcsastre.picobank.entity.Operation;
import com.jcsastre.picobank.validation.Enum;
import lombok.Value;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Value
public class RequestPostClientOperationDto {

    @NotBlank(message = "operationTypeAsString can't be empty")
    @Enum(enumClass=Operation.Type.class, ignoreCase=true)
    private String operationTypeAsString;

    @NotNull(message = "amount can't be empty")
    private Integer amountInCents;
}
