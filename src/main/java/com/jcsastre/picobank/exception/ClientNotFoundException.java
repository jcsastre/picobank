package com.jcsastre.picobank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Client doesn't exists")
public class ClientNotFoundException extends RuntimeException {
}
