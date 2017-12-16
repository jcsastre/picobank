package com.jcsastre.picobank.controller;

import com.jcsastre.picobank.dto.RequestPostClientDto;
import com.jcsastre.picobank.entity.Client;
import com.jcsastre.picobank.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/clients")
@Slf4j
public class ClientController {

    private ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping(headers="Accept=application/json")
    public ResponseEntity<Void> createOneClient(
        @Valid @RequestBody RequestPostClientDto dto
    ) {

        final Client client = clientService.createClient(dto.getEmail(), dto.getPassword());

        URI location =
            ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(client.getId())
                .toUri();

        return ResponseEntity
            .created(location)
            .build();
    }

    @ExceptionHandler({IllegalArgumentException.class, NullPointerException.class})
    void handleBadRequests(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }
}