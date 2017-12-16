package com.jcsastre.picobank.service;

import com.jcsastre.picobank.entity.Client;
import com.jcsastre.picobank.exception.ClientNotFoundException;
import com.jcsastre.picobank.exception.ClientWithThatEmailAlreadyExistsException;
import com.jcsastre.picobank.repository.ClientRepository;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;
import java.util.UUID;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    public ClientServiceImpl(
        ClientRepository clientRepository
    ) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Client createClient(
        String email,
        String password
    ) {

        Assert.notNull(email, "email is null");
        if (!EmailValidator.getInstance().isValid(email)) throw new IllegalArgumentException("email is not valid");
        Assert.notNull(password, "password is null");

        String hashedPassword = password + "blablabla"; //TODO: hash pasword

        final Optional<Client> clientOptional = clientRepository.findByEmail(email);

        if (clientOptional.isPresent()) {
            throw new ClientWithThatEmailAlreadyExistsException();
        }

        final Client client = new Client();
        client.setEmail(email);
        client.setPasswordHash(hashedPassword);
        client.setBalanceInCents(0);

        clientRepository.saveAndFlush(client);

        return client;
    }

    @Override
    public Client getClient(
        String clientId
    ) {

        Assert.notNull(clientId, "clientId is null");

        final Client client = clientRepository.findOne(UUID.fromString(clientId));
        if (client == null)
            throw new ClientNotFoundException();

        return client;
    }
}
