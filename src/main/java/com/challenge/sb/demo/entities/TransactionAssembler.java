package com.challenge.sb.demo.entities;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class TransactionAssembler implements RepresentationModelAssembler<Transaction, EntityModel<Transaction>> {

    @Override
    public EntityModel<Transaction> toModel(Transaction transaction){
        return EntityModel.of(transaction);
    }
}
