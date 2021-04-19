package com.challenge.sb.demo;

import com.challenge.sb.demo.entities.Account;
import com.challenge.sb.demo.entities.AccountModelAssembler;
import com.challenge.sb.demo.entities.AccountNotFoundException;
import com.challenge.sb.demo.entities.Transaction;
import com.challenge.sb.demo.repositories.AccountRepository;
import com.challenge.sb.demo.repositories.TransactionRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class AccountController {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    private final AccountModelAssembler accountModelAssembler;

    private Account makeTransaction(Long accountId, BigDecimal amount, Transaction.Type type){
        return accountRepository.findById(accountId)
                .map(account -> {
                    account.setBalance(account.getBalance().add(amount));
                    transactionRepository.save(new Transaction(amount, type, account));
                    return accountRepository.save(account);
                }).orElseThrow(() -> new AccountNotFoundException(accountId));
    }

    // Dependency Injection
    AccountController(AccountRepository accountRepository,
                      TransactionRepository transactionRepository,
                      AccountModelAssembler accountModelAssembler){
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.accountModelAssembler = accountModelAssembler;
    }

    @PostMapping("/accounts")
    ResponseEntity<?> newAccount(@RequestBody Account account){
        Account newAccount = new Account(account.getName(), account.getDescription());
        EntityModel<Account> entityModel = accountModelAssembler.toModel(accountRepository.save(newAccount));
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/accounts/{id}")
    ResponseEntity<?> updateAccount(@PathVariable Long id, @RequestBody Account newAccount){
        Account updatedAccount = accountRepository.findById(id)
                .map(account -> {
                    account.setName(newAccount.getName());
                    account.setDescription(newAccount.getDescription());
                    account.setStatus(newAccount.getStatus());
                    return accountRepository.save(account);
                }).orElseThrow(() -> new AccountNotFoundException(id));

        EntityModel<Account> entityModel = accountModelAssembler.toModel(updatedAccount);
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/accounts")
    public CollectionModel<EntityModel<Account>> listAccounts(){
        List<EntityModel<Account>> accounts = accountRepository.findAll().stream()
                .map(accountModelAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(accounts, linkTo(methodOn(AccountController.class).listAccounts()).withSelfRel());
    }

    @GetMapping("/accounts/{id}")
    public EntityModel<Account> findAccount(@PathVariable Long id){
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));

        return accountModelAssembler.toModel(account);
    }

    @DeleteMapping("/accounts/{id")
    ResponseEntity<?> deleteAccount(@PathVariable Long id){
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));

        if (account.getStatus() == Account.Status.ACTIVE) {
            account.setStatus(Account.Status.CANCELED);
            return ResponseEntity.ok(accountModelAssembler.toModel(accountRepository.save(account)));
        }

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                        .withTitle("Method not allowed")
                        .withDetail("Can't cancel account already canceled"));
    }

    @GetMapping("/accounts/{id}/balance")
    BigDecimal checkBalance(@PathVariable Long id){
        return accountRepository.checkBalance(id);
    }

    @GetMapping("/accounts/{id}/transactions")
    List<Transaction> financialStatement(@PathVariable Long id){
        return transactionRepository.findByAccountId(id);
    }

    @PostMapping("/accounts/{id}/deposit")
    void makeDeposit(@RequestBody Deposit deposit, @PathVariable Long id){
        this.makeTransaction(id, deposit.getAmount(), Transaction.Type.DEPOSIT);
    }

    @PostMapping("/accounts/{id}/payment")
    void makePayment(@RequestBody Payment payment, @PathVariable Long id){
        this.makeTransaction(id, payment.getAmount().negate(), Transaction.Type.PAYMENT);
    }

    @PostMapping("/accounts/transfer")
    void makeTransfer(@RequestBody Transfer transfer){
        this.makeTransaction(transfer.getAccountOrigin(), transfer.getAmount().negate(), Transaction.Type.TRANSFER);
        this.makeTransaction(transfer.getAccountDestination(), transfer.getAmount(), Transaction.Type.TRANSFER);
    }
}
