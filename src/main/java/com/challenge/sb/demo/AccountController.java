package com.challenge.sb.demo;

import com.challenge.sb.demo.entities.Account;
import com.challenge.sb.demo.entities.AccountNotFoundException;
import com.challenge.sb.demo.entities.Transaction;
import com.challenge.sb.demo.repositories.AccountRepository;
import com.challenge.sb.demo.repositories.TransactionRepository;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
public class AccountController {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    private Account makeTransaction(Long accountId, BigDecimal amount, Transaction.Type type){
        return accountRepository.findById(accountId)
                .map(account -> {
                    account.setBalance(account.getBalance().add(amount));
                    transactionRepository.save(new Transaction(amount, type, account));
                    return accountRepository.save(account);
                }).orElseThrow(() -> new AccountNotFoundException(accountId));
    }

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

    @PutMapping("/accounts/{id}")
    Account updateAccount(@PathVariable Long id, @RequestBody Account newAccount){
        return accountRepository.findById(id)
                .map(account -> {
                    account.setName(newAccount.getName());
                    account.setDescription(newAccount.getDescription());
                    account.setStatus(newAccount.getStatus());
                    return accountRepository.save(account);
                }).orElseThrow(() -> new AccountNotFoundException(id));
    }

    @GetMapping("/accounts/{id}")
    Account findAccount(@PathVariable Long id){
        return accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
    }

    @DeleteMapping("/accounts/{id}")
    Account deleteAccount(@PathVariable Long id){
        return accountRepository.findById(id)
                .map(account -> {
                    account.setStatus(Account.Status.CANCELED);
                    return accountRepository.save(account);
                }).orElseThrow(() -> new AccountNotFoundException(id));
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
