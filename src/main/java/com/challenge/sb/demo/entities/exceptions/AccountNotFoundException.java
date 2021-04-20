package com.challenge.sb.demo.entities.exceptions;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(Long id) {
        super("Could not find account " + id);
    }
}
