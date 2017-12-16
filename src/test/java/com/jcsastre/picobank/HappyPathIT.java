package com.jcsastre.picobank;

import com.jcsastre.picobank.dto.RequestPostClientDto;
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
    // 1. Post client
    // 2. Add Operation
    // 3. Get client info

    @Test
    public void happyPath() {

        final ResponseEntity<Void> responseEntity =
            this.testRestTemplate.postForEntity(
                "/clients",
                new RequestPostClientDto("email@email.com", "plain_password"),
                Void.class
            );

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.CREATED));
        assertThat(responseEntity.getHeaders().getLocation(), is(any(URI.class)));
    }
}
