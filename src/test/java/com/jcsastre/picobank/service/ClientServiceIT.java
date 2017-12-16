package com.jcsastre.picobank.service;

import com.jcsastre.picobank.entity.Client;
import com.jcsastre.picobank.entity.Operation;
import com.jcsastre.picobank.exception.ClientWithThatEmailAlreadyExistsException;
import com.jcsastre.picobank.repository.ClientRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ClientServiceIT {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TestEntityManager entityManager;

    private ClientServiceImpl clientService;

    private static final String UUID_VALID_AS_STRING = "eda22360-0772-49c3-aa60-835220760aa9";
    private static final String UUID_INVALID_AS_STRING = "eda22360835220760aa9";
    private static final String EMAIL_WELLFORMED = "email@email.com";
    private static final String EMAIL_MALFORMED = "email";
    private static final String PASSWORD = "password";

    // Scenarios
    //
    // 01. createClient
    // 01.01. Should throw IllegalArgumentException when email is null
    // 01.02. Should throw IllegalArgumentException when email is not valid
    // 01.03. Should throw IllegalArgumentException when password is null
    // 01.04. Should throw ClientWithThatEmailAlreadyExistsException when a User with provided Email already exists
    // 01.05. Happy Path

    @Before
    public void setUp() {

        // Given
        MockitoAnnotations.initMocks(this);
        clientService = new ClientServiceImpl(clientRepository);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_01_01() throws Exception {

        // When
        final Client client = clientService.createClient(null, PASSWORD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_01_02() throws Exception {

        // When
        final Client client = clientService.createClient(EMAIL_MALFORMED, PASSWORD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_01_03() throws Exception {

        // When
        final Client client = clientService.createClient(EMAIL_WELLFORMED, null);
    }

    @Test(expected = ClientWithThatEmailAlreadyExistsException.class)
    public void test_01_04() throws Exception {

        // Given
        Client clientPersisted = persistClientWithOneOperation();

        // When
        clientService.createClient(clientPersisted.getEmail(), PASSWORD);
    }

    @Test
    public void test_01_05() throws Exception {

        // When
        final Client clientPersisted = clientService.createClient(EMAIL_WELLFORMED, PASSWORD);

        // Then
        assertThat(clientPersisted.getId(), instanceOf(UUID.class));
        assertThat(clientPersisted.getEmail(), equalTo(EMAIL_WELLFORMED));
        assertThat(clientPersisted.getPasswordHash(), not(equalTo(PASSWORD)));
        assertThat(clientPersisted.getBalanceInCents(), equalTo(0));
        assertThat(clientPersisted.getOperations(), hasSize(0));
    }
    private Client persistClientWithOneOperation() {
        Client client = new Client();
        client.setEmail(EMAIL_WELLFORMED);
        client.setPasswordHash(PASSWORD);
        client.setBalanceInCents(10);
        final Operation operation = new Operation();
        operation.setType(Operation.Type.DEPOSIT);
        operation.setAmountInCents(10);
        client.addOperation(operation);
        clientRepository.saveAndFlush(client);
        return client;
    }
}
