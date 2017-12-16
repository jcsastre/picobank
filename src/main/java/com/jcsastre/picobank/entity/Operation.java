package com.jcsastre.picobank.entity;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class Operation {

    private Type type;

    private Integer amountInCents;

    public enum Type {
        DEPOSIT, WITHDRAWAL
    }
}
