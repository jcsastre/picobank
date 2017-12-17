package com.jcsastre.picobank.service;

import com.jcsastre.picobank.entity.Client;
import com.jcsastre.picobank.entity.Operation;
import com.jcsastre.picobank.exception.ClientNotFoundException;
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
    private TestEntityManager testEntityManager;

    private ClientServiceImpl clientService;

    private static final String EMAIL_WELLFORMED = "email@email.com";
    private static final String EMAIL_MALFORMED = "email";
    private static final String PASSWORD = "password";

    // TODO: check TestEntityManager

    // Scenarios
    //
    // 01. createClient
    //
    // 01.01. Should throw IllegalArgumentException when email is null
    // 01.02. Should throw IllegalArgumentException when email is not valid
    // 01.03. Should throw IllegalArgumentException when password is null
    // 01.04. Should throw ClientWithThatEmailAlreadyExistsException when a User with provided Email already exists
    // 01.05. Happy Path
    //
    // 02. getClient
    //
    // 02.01. Should throw IllegalArgumentException when clienId is null
    // 02.02. Should throw IllegalArgumentException when clienId is not valid
    // 02.03. Should throw ClientNotFoundException when there is doesn't exist a Client with userId
    // 02.04. Happy Path

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
        Client clientSaved = saveClientWithOneOperation();

        // When
        clientService.createClient(clientSaved.getEmail(), PASSWORD);
    }

    @Test
    public void test_01_05() throws Exception {

        // When
        final Client clientCreated = clientService.createClient(EMAIL_WELLFORMED, PASSWORD);
        final Client clientFound = testEntityManager.find(Client.class, clientCreated.getId());

        // Then
        assertThat(clientCreated.getId(), instanceOf(UUID.class));
        assertThat(clientFound.getEmail(), equalTo(EMAIL_WELLFORMED));
        assertThat(clientFound.getPasswordHash(), not(equalTo(PASSWORD)));
        assertThat(clientFound.getBalanceInCents(), equalTo(0));
        assertThat(clientFound.getOperations(), hasSize(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_02_01() throws Exception {

        // When
        clientService.getClient(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_02_02() throws Exception {

        // When
        clientService.getClient("non_valid_client_id");
    }

    @Test(expected = ClientNotFoundException.class)
    public void test_02_03() throws Exception {

        // Given
        saveClientWithOneOperation();

        // When
        clientService.getClient(UUID.randomUUID().toString());
    }

    @Test
    public void test_02_04() throws Exception {

        // Given
        Client clientSaved = saveClientWithOneOperation();

        // When
        final Client gotClient = clientService.getClient(clientSaved.getId().toString());

        // Then
        assertThat(gotClient, equalTo(clientSaved));
    }

    private Client saveClientWithOneOperation() {

        Client client = new Client();
        client.setEmail(EMAIL_WELLFORMED);
        client.setPasswordHash(PASSWORD);
        client.setBalanceInCents(10);
        final Operation operation = new Operation();
        operation.setType(Operation.Type.DEPOSIT);
        operation.setAmountInCents(10);
        client.addOperation(operation);

        return
            testEntityManager.persistFlushFind(client);
    }
}
