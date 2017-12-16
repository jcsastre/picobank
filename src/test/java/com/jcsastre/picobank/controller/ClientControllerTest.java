package com.jcsastre.picobank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcsastre.picobank.dto.RequestPostClientDto;
import com.jcsastre.picobank.entity.Client;
import com.jcsastre.picobank.exception.ClientWithThatEmailAlreadyExistsException;
import com.jcsastre.picobank.service.ClientService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static io.github.benas.randombeans.api.EnhancedRandom.random;
import static org.hamcrest.core.IsInstanceOf.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ClientController.class)
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    // Scenarios
    //
    // 01. POST Client
    //
    // 01.01. Should return HttpStatus BAD_REQUEST (400) when email not provided
    // 01.02. Should return HttpStatus BAD_REQUEST (400) when email is not valid
    // 01.03. Should return HttpStatus BAD_REQUEST (400) when password not provided
    // 01.04. Should return HttpStatus CONFLICT (409) when a user with provided email already exists
    // 01.05. Should return HttpStatus CREATED (201) and location header when Happy Path

    private static final String EMAIL_WELLFORMED = "email@dot.com";
    private static final String EMAIL_MALFORMED = "email-dot.com";
    private static final String PASSWORD = "password123";

    private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    public void test_01_01() throws Exception {

        // Given
        final String requestBody =
            "{" +
                "\"password\": \"" + PASSWORD + "\"" +
            "}";

        // When
        mockMvc.perform(post("/clients").contentType(MediaType.APPLICATION_JSON).content(requestBody))

        // Then
        .andExpect(status().isBadRequest());
    }

    @Test
    public void test_01_02() throws Exception {

        // Given
        final RequestPostClientDto dto = new RequestPostClientDto(EMAIL_MALFORMED, PASSWORD);
        final String dtoAsJson = OBJECT_MAPPER.writeValueAsString(dto);
        when(clientService.createClient(dto.getEmail(), dto.getPassword())).thenThrow(new IllegalArgumentException());

        // When
        mockMvc.perform(post("/clients").contentType(MediaType.APPLICATION_JSON).content(dtoAsJson))

        // Then
        .andExpect(status().isBadRequest());
    }

    @Test
    public void test_01_03() throws Exception {

        // Given
        final String requestBody =
            "{" +
                "\"email\": \"jcsastre@yahoo.es\"" +
            "}";

        // When
        mockMvc.perform(post("/clients").contentType(MediaType.APPLICATION_JSON).content(requestBody))

        // Then
        .andExpect(status().isBadRequest());
    }

    @Test
    public void test_01_04() throws Exception {

        // Given
        final RequestPostClientDto dto = new RequestPostClientDto(EMAIL_WELLFORMED, PASSWORD);
        final String dtoAsJson = OBJECT_MAPPER.writeValueAsString(dto);
        when(clientService.createClient(dto.getEmail(), dto.getPassword())).thenThrow(new ClientWithThatEmailAlreadyExistsException());

        // When
        mockMvc.perform(post("/clients").contentType(MediaType.APPLICATION_JSON).content(dtoAsJson))

        // Then
        .andExpect(status().isConflict());
    }

    @Test
    public void test_01_05() throws Exception {

        // Given
        final RequestPostClientDto dto = new RequestPostClientDto(EMAIL_WELLFORMED, PASSWORD);
        final String dtoAsJson = OBJECT_MAPPER.writeValueAsString(dto);
        when(clientService.createClient(dto.getEmail(), dto.getPassword())).thenReturn(random(Client.class));

        // When
        mockMvc.perform(post("/clients").contentType(MediaType.APPLICATION_JSON).content(dtoAsJson))

        // Then
        .andExpect(status().isCreated())
        .andExpect(header().string("location", any(String.class)));
    }
}
