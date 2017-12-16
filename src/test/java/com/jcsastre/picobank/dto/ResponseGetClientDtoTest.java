package com.jcsastre.picobank.dto;

import com.jcsastre.picobank.entity.Client;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import static io.github.benas.randombeans.api.EnhancedRandom.random;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ResponseGetClientDtoTest {

    // Scenarios
    //
    // 01. asGetClientDto
    //
    // 01.01. Should map correctly

    @Test
    public void test_01_01() {

        // Given
        final Client client = random(Client.class);

        // When
        final ResponseGetClientDto dto = ResponseGetClientDto.asGetClientDto.apply(client);

        // Then
        assertThat(dto.getStatus(), equalTo(HttpStatus.OK.value()));
        assertThat(dto.getData().getClient().getEmail(), equalTo(client.getEmail()));
        assertThat(dto.getData().getClient().getBalanceInCents(), equalTo(client.getBalanceInCents()));
        assertThat(dto.getData().getClient().getOperations(), equalTo(client.getOperations()));
    }
}
