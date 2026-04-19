package com.banking.bankingapi.service;

import com.banking.bankingapi.dto.TransactionRequest;
import com.banking.bankingapi.model.Account;
import com.banking.bankingapi.model.AuditLog;
import com.banking.bankingapi.model.Transaction;
import com.banking.bankingapi.repository.AccountRepository;
import com.banking.bankingapi.repository.AuditLogRepository;
import com.banking.bankingapi.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.transaction.annotation.Propagation;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final AuditLogRepository auditLogRepository;

    @Transactional(propagation = Propagation.NOT_SUPPORTED)  //If anything fails midway, the entire operation rolls back because of this 
    public Transaction deposit(TransactionRequest request, String performedBy) {
        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setBalance(account.getBalance().add(request.getAmount()));
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setTransactionType("DEPOSIT");
        transaction.setAmount(request.getAmount());
        transaction.setSourceAccountId(account.getId());
        transaction.setStatus("SUCCESS");
        transactionRepository.save(transaction);

        logToMongo("DEPOSIT", performedBy, "Deposited " + request.getAmount() + " to " + account.getAccountNumber(), "SUCCESS");

        return transaction;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)//If anything fails midway, the entire operation rolls back because of this 
    public Transaction withdraw(TransactionRequest request, String performedBy) {
        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            logToMongo("WITHDRAWAL", performedBy, "Insufficient funds for: " + account.getAccountNumber(), "FAILED");
            throw new RuntimeException("Insufficient funds");
        }

        account.setBalance(account.getBalance().subtract(request.getAmount()));
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setTransactionType("WITHDRAWAL");
        transaction.setAmount(request.getAmount());
        transaction.setSourceAccountId(account.getId());
        transaction.setStatus("SUCCESS");
        transactionRepository.save(transaction);

        logToMongo("WITHDRAWAL", performedBy, "Withdrew " + request.getAmount() + " from " + account.getAccountNumber(), "SUCCESS");

        return transaction;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public Transaction transfer(TransactionRequest request, String performedBy) {
        Account source = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new RuntimeException("Source account not found"));

        Account destination = accountRepository.findByAccountNumber(request.getDestinationAccountNumber())
                .orElseThrow(() -> new RuntimeException("Destination account not found"));

        if (source.getBalance().compareTo(request.getAmount()) < 0) {
            logToMongo("TRANSFER", performedBy, "Insufficient funds for transfer from: " + source.getAccountNumber(), "FAILED");
            throw new RuntimeException("Insufficient funds");
        }

        source.setBalance(source.getBalance().subtract(request.getAmount()));
        destination.setBalance(destination.getBalance().add(request.getAmount()));
        accountRepository.save(source);
        accountRepository.save(destination);

        Transaction transaction = new Transaction();
        transaction.setTransactionType("TRANSFER");
        transaction.setAmount(request.getAmount());
        transaction.setSourceAccountId(source.getId());
        transaction.setDestinationAccountId(destination.getId());
        transaction.setStatus("SUCCESS");
        transactionRepository.save(transaction);

        logToMongo("TRANSFER", performedBy, "Transferred " + request.getAmount() + " from " + source.getAccountNumber() + " to " + destination.getAccountNumber(), "SUCCESS");

        return transaction;
    }

    public List<Transaction> getTransactionHistory(Long accountId, String performedBy) {
        List<Transaction> transactions = transactionRepository.findBySourceAccountIdOrderByCreatedAtDesc(accountId);
        logToMongo("VIEW_HISTORY", performedBy, "Transaction history viewed for account ID: " + accountId, "SUCCESS");
        return transactions;
    }

    private void logToMongo(String action, String performedBy, String details, String status) {
    try {
        AuditLog log = new AuditLog();
        log.setAction(action);
        log.setPerformedBy(performedBy);
        log.setDetails(details);
        log.setStatus(status);
        log.setTimestamp(LocalDateTime.now());
        auditLogRepository.save(log);
        System.out.println("MongoDB log saved: " + action);
    } catch (Exception e) {
        System.out.println("MongoDB log FAILED: " + e.getMessage());
    }
}
}