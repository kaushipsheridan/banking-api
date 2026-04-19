package com.banking.bankingapi.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "audit_logs")
public class AuditLog {
    
    @Id
    private String id;

    private String action; // what happened e.g. DEPOSIT, LOGIN, CREATE_ACCOUNT

    private String performedBy; // which user did it

    private String details; // extra info about the action

    private String status; // SUCCESS or FAILED

    private LocalDateTime timestamp;
}
