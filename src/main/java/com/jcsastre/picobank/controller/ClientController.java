package com.jcsastre.picobank.controller;

import com.jcsastre.picobank.dto.RequestPostClientDto;
import com.jcsastre.picobank.dto.ResponseGetClientDto;
import com.jcsastre.picobank.entity.Client;
import com.jcsastre.picobank.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/clients")
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

    @GetMapping(value="/{clientId}", headers="Accept=application/json")
    public ResponseEntity<ResponseGetClientDto> getOneClient(
        @PathVariable UUID clientId
    ) {

        final Client client = clientService.getClient(clientId.toString());

        final ResponseGetClientDto dto = ResponseGetClientDto.asGetClientDto.apply(client);

        return
            ResponseEntity.ok(dto);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    void handleBadRequests(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }
}
