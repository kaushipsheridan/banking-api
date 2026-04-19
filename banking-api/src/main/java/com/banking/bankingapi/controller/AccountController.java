package com.banking.bankingapi.controller;

import com.banking.bankingapi.dto.AccountRequest;
import com.banking.bankingapi.model.Account;
import com.banking.bankingapi.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<Account> createAccount(
            @Valid @RequestBody AccountRequest request,
            Authentication authentication) {
        String performedBy = authentication.getName();
        Account account = accountService.createAccount(request, performedBy);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccount(
            @PathVariable Long id,
            Authentication authentication) {
        String performedBy = authentication.getName();
        Account account = accountService.getAccount(id, performedBy);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<BigDecimal> getBalance(
            @PathVariable Long id,
            Authentication authentication) {
        String performedBy = authentication.getName();
        BigDecimal balance = accountService.getBalance(id, performedBy);
        return ResponseEntity.ok(balance);
    }
}