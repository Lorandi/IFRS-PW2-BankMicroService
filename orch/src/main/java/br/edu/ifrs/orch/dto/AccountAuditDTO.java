package br.edu.ifrs.orch.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountAuditDTO(
        Long id,
        String operationId,
        String action,
        Long ownerId,
        String sourceAccountId,
        String targetAccountId,
        BigDecimal amount,
        String result,
        LocalDateTime timestamp,
        String errorMessage
) {
}
