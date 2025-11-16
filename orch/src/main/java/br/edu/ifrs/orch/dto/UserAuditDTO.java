package br.edu.ifrs.orch.dto;

import java.time.LocalDateTime;

public record UserAuditDTO(
        Long id,
        String operationId,
        String action,
        Long userId,
        String result,
        LocalDateTime timestamp,
        String errorMessage
) {}
