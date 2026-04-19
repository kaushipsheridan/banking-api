package com.banking.bankingapi.service;

import com.banking.bankingapi.dto.AccountRequest;
import com.banking.bankingapi.model.Account;
import com.banking.bankingapi.model.AuditLog;
import com.banking.bankingapi.repository.AccountRepository;
import com.banking.bankingapi.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AuditLogRepository auditLogRepository;

    public Account createAccount(AccountRequest request, String performedBy) {
        Account account = new Account();
        account.setAccountNumber(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        account.setOwnerName(request.getOwnerName());
        account.setBalance(request.getInitialBalance());
        account.setAccountType(request.getAccountType());

        Account saved = accountRepository.save(account);
        logToMongo("CREATE_ACCOUNT", performedBy, "Account created: " + saved.getAccountNumber(), "SUCCESS");
        return saved;
    }

    public Account getAccount(Long id, String performedBy) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        logToMongo("VIEW_ACCOUNT", performedBy, "Account viewed: " + account.getAccountNumber(), "SUCCESS");
        return account;
    }

    public BigDecimal getBalance(Long id, String performedBy) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        logToMongo("CHECK_BALANCE", performedBy, "Balance checked for: " + account.getAccountNumber(), "SUCCESS");
        return account.getBalance();
    }

    public List<Account> getAllAccounts(String performedBy) {
        List<Account> accounts = accountRepository.findAll();
        logToMongo("VIEW_ALL_ACCOUNTS", performedBy, "All accounts viewed", "SUCCESS");
        return accounts;
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