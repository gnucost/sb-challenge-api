package com.challenge.sb.demo;

import com.challenge.sb.demo.entities.*;
import com.challenge.sb.demo.entities.helpers.Deposit;
import com.challenge.sb.demo.entities.Payment;
import com.challenge.sb.demo.entities.helpers.Transfer;
import com.challenge.sb.demo.repositories.AccountRepository;
import com.challenge.sb.demo.repositories.PaymentRepository;
import com.challenge.sb.demo.repositories.TransactionRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class AccountController {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final PaymentRepository paymentRepository;

    private final AccountAssembler accountAssembler;
    private final TransactionAssembler transactionAssembler;

    private Account makeTransaction(Long accountId, BigDecimal amount, Transaction.Type type, Long transferAccountId, Payment payment){
        return accountRepository.findById(accountId)
                .map(account -> {
                    account.setBalance(account.getBalance().add(amount));
                    transactionRepository.save(new Transaction(amount, type, account, transferAccountId, payment));
                    return accountRepository.save(account);
                }).orElseThrow(() -> new AccountNotFoundException(accountId));
    }

    // Dependency Injection
    AccountController(AccountRepository accountRepository,
                      TransactionRepository transactionRepository,
                      PaymentRepository paymentRepository,
                      AccountAssembler accountAssembler,
                      TransactionAssembler transactionAssembler){
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.paymentRepository = paymentRepository;
        this.accountAssembler = accountAssembler;
        this.transactionAssembler = transactionAssembler;
    }

    @Operation(summary = "Add new account")
    @PostMapping("/accounts")
    ResponseEntity<?> newAccount(@RequestBody Account account){
        Account newAccount = new Account(account.getName(), account.getDescription());
        EntityModel<Account> entityModel = accountAssembler.toModel(accountRepository.save(newAccount));
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @Operation(summary = "Update an existing account")
    @PutMapping("/accounts/{id}")
    ResponseEntity<?> updateAccount(@PathVariable Long id, @RequestBody Account newAccount){
        Account updatedAccount = accountRepository.findById(id)
                .map(account -> {
                    account.setName(newAccount.getName());
                    account.setDescription(newAccount.getDescription());
                    account.setStatus(newAccount.getStatus());
                    return accountRepository.save(account);
                }).orElseThrow(() -> new AccountNotFoundException(id));

        EntityModel<Account> entityModel = accountAssembler.toModel(updatedAccount);
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @Operation(summary = "List all accounts")
    @GetMapping("/accounts")
    public CollectionModel<EntityModel<Account>> listAccounts(){
        List<EntityModel<Account>> accounts = accountRepository.findAll().stream()
                .map(accountAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(accounts, linkTo(methodOn(AccountController.class).listAccounts()).withSelfRel());
    }

    @Operation(summary = "Find an account by id")
    @GetMapping("/accounts/{id}")
    public EntityModel<Account> findAccount(@PathVariable Long id){
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));

        return accountAssembler.toModel(account);
    }

    @Operation(summary = "Change account status to CANCELED")
    @DeleteMapping("/accounts/{id}")
    ResponseEntity<?> deleteAccount(@PathVariable Long id){
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));

        if (account.getStatus() == Account.Status.ACTIVE) {
            account.setStatus(Account.Status.CANCELED);
            return ResponseEntity.ok(accountAssembler.toModel(accountRepository.save(account)));
        }

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                        .withTitle("Method not allowed")
                        .withDetail("Can't cancel account already canceled"));
    }

    @Operation(summary = "Return current balance for account")
    @GetMapping("/accounts/{id}/balance")
    ResponseEntity<String> checkBalance(@PathVariable Long id){
        if (!accountRepository.existsById(id))
            throw new AccountNotFoundException(id);

        BigDecimal balance = accountRepository.checkBalance(id);
        return new ResponseEntity<>("Account current balance: " + balance, HttpStatus.OK);
    }

    @Operation(summary = "List transactions by account id")
    @GetMapping("/accounts/{id}/transactions")
    public CollectionModel<EntityModel<Transaction>> financialStatement(@PathVariable Long id){
        List<EntityModel<Transaction>> transactions = transactionRepository.findByAccountId(id).stream()
                .map(transactionAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(transactions,
                linkTo(methodOn(AccountController.class).financialStatement(id)).withSelfRel());
    }

    @Operation(summary = "Make a deposit (Add credit to account)")
    @PostMapping("/accounts/{id}/deposit")
    ResponseEntity<String> makeDeposit(@RequestBody Deposit deposit, @PathVariable Long id){
        this.makeTransaction(
                id,
                deposit.getAmount(),
                Transaction.Type.DEPOSIT,
                null,
                null);

        return new ResponseEntity<>("Deposit successful", HttpStatus.OK);
    }

    @Operation(summary = "Make a payment")
    @PostMapping("/accounts/{id}/payment")
    ResponseEntity<String> makePayment(@RequestBody Payment payment, @PathVariable Long id){
        Payment newPayment = this.paymentRepository.save(payment);

        this.makeTransaction(
                id,
                payment.getAmount().negate(),
                Transaction.Type.PAYMENT,
                null,
                newPayment);

        return new ResponseEntity<>("Payment successful", HttpStatus.OK);
    }

    @Operation(summary = "Transfer credit between accounts")
    @PostMapping("/accounts/transfer")
    ResponseEntity<String> makeTransfer(@RequestBody Transfer transfer){
        this.makeTransaction(
                transfer.getAccountOriginId(),
                transfer.getAmount().negate(),
                Transaction.Type.TRANSFER,
                transfer.getAccountDestinationId(),
                null);

        this.makeTransaction(
                transfer.getAccountDestinationId(),
                transfer.getAmount(),
                Transaction.Type.TRANSFER,
                transfer.getAccountOriginId(),
                null);

        return new ResponseEntity<>("Transfer successful", HttpStatus.OK);
    }
}
