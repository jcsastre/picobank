package com.jcsastre.picobank.service;

import com.jcsastre.picobank.entity.Client;

public interface ClientService {

    Client createClient(
        String email,
        String password
    );

    Client getClient(
        String clientId
    );

    Client addOperation(
        String clientId,
        String operationType,
        Integer amountInCents
    );
}
