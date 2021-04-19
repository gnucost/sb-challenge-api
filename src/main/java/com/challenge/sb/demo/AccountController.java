package com.challenge.sb.demo;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
public class AccountController {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    // Dependency Injection
    AccountController(AccountRepository accountRepository, TransactionRepository transactionRepository){
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @PostMapping("/accounts")
    Account newAccount(@RequestBody Account account){
        Account newAccount = new Account(account.getName(), account.getDescription());
        return accountRepository.save(newAccount);
    }

    @PutMapping("/accounts")
    Optional<Account> updateAccount(@RequestBody Account newAccount){
        return accountRepository.findById(newAccount.getId())
                .map(account -> {
                    account.setName(newAccount.getName());
                    account.setDescription(newAccount.getDescription());
                    account.setStatus(newAccount.getStatus());
                    return accountRepository.save(account);
                });
    }

    @GetMapping("/accounts/{id}")
    Optional<Account> findAccount(@PathVariable Long id){
        return accountRepository.findById(id);
    }

    @DeleteMapping("/accounts/{id}")
    Optional<Account> deleteAccount(@PathVariable Long id){
        return accountRepository.findById(id)
                .map(account -> {
                    account.setStatus(Account.Status.CANCELED);
                    return accountRepository.save(account);
                });
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
        accountRepository.findById(id)
                .map(account -> {
                    account.setBalance(account.getBalance().add(deposit.getAmount()));
                    accountRepository.save(account);
                    return transactionRepository.save(new Transaction(deposit.getAmount(), Transaction.Type.DEPOSIT, account));
                });
    }

    @PostMapping("/accounts/{id}/payment")
    void makePayment(@RequestBody Payment payment, @PathVariable Long id){
        accountRepository.findById(id)
                .map(account -> {
                    account.setBalance(account.getBalance().subtract(payment.getAmount()));
                    accountRepository.save(account);
                    return transactionRepository.save(new Transaction(payment.getAmount(), Transaction.Type.PAYMENT, account));
                });
    }

    @PostMapping("/accounts/transfer")
    void makeTransfer(@RequestBody Transfer transfer){
        accountRepository.findById(transfer.getAccountOrigin())
                .map(account -> {
                    account.setBalance(account.getBalance().subtract(transfer.getAmount()));
                    accountRepository.save(account);
                    return transactionRepository.save(new Transaction(transfer.getAmount().negate(), Transaction.Type.TRANSFER, account));
                });

        accountRepository.findById(transfer.getAccountDestination())
                .map(account -> {
                    account.setBalance(account.getBalance().add(transfer.getAmount()));
                    accountRepository.save(account);
                    return transactionRepository.save(new Transaction(transfer.getAmount(), Transaction.Type.TRANSFER, account));
                });
    }
}
