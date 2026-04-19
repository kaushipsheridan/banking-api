package com.banking.bankingapi.controller;

import com.banking.bankingapi.dto.TransactionRequest;
import com.banking.bankingapi.model.Transaction;
import com.banking.bankingapi.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<Transaction> deposit(
            @Valid @RequestBody TransactionRequest request,
            Authentication authentication) {
        String performedBy = authentication.getName();
        Transaction transaction = transactionService.deposit(request, performedBy);
        return ResponseEntity.ok(transaction);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Transaction> withdraw(
            @Valid @RequestBody TransactionRequest request,
            Authentication authentication) {
        String performedBy = authentication.getName();
        Transaction transaction = transactionService.withdraw(request, performedBy);
        return ResponseEntity.ok(transaction);
    }

    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transfer(
            @Valid @RequestBody TransactionRequest request,
            Authentication authentication) {
        String performedBy = authentication.getName();
        Transaction transaction = transactionService.transfer(request, performedBy);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/history/{accountId}")
    public ResponseEntity<List<Transaction>> getHistory(
            @PathVariable Long accountId,
            Authentication authentication) {
        String performedBy = authentication.getName();
        List<Transaction> transactions = transactionService.getTransactionHistory(accountId, performedBy);
        return ResponseEntity.ok(transactions);
    }
}