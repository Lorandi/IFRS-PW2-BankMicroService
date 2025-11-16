package br.edu.ifrs.orch.dto;

import java.math.BigDecimal;

public record AccountDTO(
        Long id,
        String accountId,
        BigDecimal balance,
        String status,
        Long ownerId
) {}
