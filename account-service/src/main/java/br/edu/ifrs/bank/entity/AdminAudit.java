package br.edu.ifrs.bank.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "admin_audit")
public class AdminAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String operationId;

    private String action;

    private Long ownerId;

    private String sourceAccountId;

    private String targetAccountId;

    @Column(precision = 18, scale = 2)
    private BigDecimal amount;

    private String result;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(length = 200)
    private String errorMessage;

    @PrePersist
    public void prePersist() {
        if (operationId == null) {
            operationId = UUID.randomUUID().toString();
        }
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }
}
