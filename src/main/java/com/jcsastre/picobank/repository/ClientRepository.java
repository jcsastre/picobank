package com.jcsastre.picobank.repository;

import com.jcsastre.picobank.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {

    public Optional<Client> findByEmail(String email);
}
