package com.challenge.sb.demo;

import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class AccountController {
    private final AccountRepository repo;

    // Dependency Injection
    AccountController(AccountRepository repo){
        this.repo = repo;
    }

    @PostMapping("/accounts")
    Account newAccount(@RequestBody Account account){
        return repo.save(account);
    }

    @PutMapping("/accounts")
    Optional<Account> updateAccount(@RequestBody Account newAccount){
        return repo.findById(newAccount.getId())
                .map(account -> {
                    account.setName(newAccount.getName());
                    account.setDescription(newAccount.getDescription());
                    return repo.save(account);
                });
    }

    @GetMapping("/accounts/{id}")
    Optional<Account> find(@PathVariable Long id){
        return repo.findById(id);
    }

    @DeleteMapping("/accounts/{id}")
    Optional<Account> deleteAccount(@PathVariable Long id){
        return repo.findById(id)
                .map(account -> {
                    account.setStatus(Account.Status.CANCELED);
                    return repo.save(account);
                });
    }
}
