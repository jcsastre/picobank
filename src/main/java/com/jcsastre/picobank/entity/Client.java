package com.jcsastre.picobank.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class Client {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable=false, nullable=false)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    private String passwordHash;

    private Integer balanceInCents;

    @ElementCollection
    @OrderColumn
    private List<Operation> operations = new ArrayList<>();

    public void addOperation(Operation operation) {
        this.operations.add(operation);
    }
}
