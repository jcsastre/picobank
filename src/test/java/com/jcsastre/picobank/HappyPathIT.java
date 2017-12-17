package com.jcsastre.picobank;

import com.jcsastre.picobank.dto.RequestPostClientDto;
import com.jcsastre.picobank.dto.RequestPostClientOperationDto;
import com.jcsastre.picobank.dto.ResponseGetClientDto;
import com.jcsastre.picobank.entity.Operation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HappyPathIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    // Happy Path
    //
    // 1. I POST Client ...
    // 2. ... then I GET Client and got the correct information.
    // 3. ... then I POST Operation and got the correct information

    @Test
    public void happyPath() {

        final String email = "email@email.com";
        final String password = "password";

        // 1. I POST Client ...

        final ResponseEntity<Void> postClientResponseEntity =
            this.testRestTemplate.postForEntity(
                "/clients",
                new RequestPostClientDto(email, password),
                Void.class
            );

        assertThat(postClientResponseEntity.getStatusCode(), equalTo(HttpStatus.CREATED));
        assertThat(postClientResponseEntity.getHeaders().getLocation(), is(any(URI.class)));

        // 2. ... then I GET Client and got the correct information.

        final URI uriClient = postClientResponseEntity.getHeaders().getLocation();

        final ResponseEntity<ResponseGetClientDto> getClientResponseEntity =
            this.testRestTemplate.getForEntity(uriClient, ResponseGetClientDto.class);

        assertThat(getClientResponseEntity.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(getClientResponseEntity.getBody().getData().getClient().getEmail(), equalTo(email));
        assertThat(getClientResponseEntity.getBody().getData().getClient().getBalanceInCents(), equalTo(0));

        // 3. ... then I POST Operation and got the correct information

        final Integer amountInCents = 100;

        final ResponseEntity<ResponseGetClientDto> postOperationResponseEntity =
            this.testRestTemplate.postForEntity(
                uriClient + "/operations",
                new RequestPostClientOperationDto(Operation.Type.DEPOSIT.toString(), amountInCents),
                ResponseGetClientDto.class
            );
        assertThat(postOperationResponseEntity.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(postOperationResponseEntity.getBody().getData().getClient().getEmail(), equalTo(email));
        assertThat(postOperationResponseEntity.getBody().getData().getClient().getBalanceInCents(), equalTo(amountInCents));
        assertThat(postOperationResponseEntity.getBody().getData().getClient().getOperations().get(0).getType(), equalTo(Operation.Type.DEPOSIT));
        assertThat(postOperationResponseEntity.getBody().getData().getClient().getOperations().get(0).getAmountInCents(), equalTo(amountInCents));
    }
}
