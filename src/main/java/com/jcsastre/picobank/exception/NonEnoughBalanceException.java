package com.jcsastre.picobank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.CONFLICT, reason="Non enough balance exception")
public class NonEnoughBalanceException extends RuntimeException {
}
