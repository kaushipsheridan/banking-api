package com.banking.bankingapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class AccountRequest {

    @NotBlank(message = "Owner name is required")
    private String ownerName;

    @NotNull(message = "Initial balance is required")
    @Positive(message = "Initial balance must be positive")
    private BigDecimal initialBalance;

    @NotBlank(message = "Account type is required")
    private String accountType; // SAVINGS or CHECKING
}