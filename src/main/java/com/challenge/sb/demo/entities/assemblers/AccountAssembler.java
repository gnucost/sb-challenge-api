package com.challenge.sb.demo.entities.assemblers;

import com.challenge.sb.demo.AccountController;
import com.challenge.sb.demo.entities.Account;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AccountAssembler implements RepresentationModelAssembler<Account, EntityModel<Account>> {

    @Override
    public EntityModel<Account> toModel(Account account){
        return EntityModel.of(account,
                linkTo(methodOn(AccountController.class).findAccount(account.getId())).withSelfRel(),
                linkTo(methodOn(AccountController.class).listAccounts()).withRel("Accounts"));
    }
}