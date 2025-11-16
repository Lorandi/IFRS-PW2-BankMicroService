package br.edu.ifrs.user.dto;

import java.time.LocalDateTime;

public record AdminAuditUserDTO(
        Long id,
        String operationId,
        String action,
        Long userId,
        String result,
        LocalDateTime timestamp,
        String errorMessage
) {}
