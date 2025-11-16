package br.edu.ifrs.account.dto;

import java.math.BigDecimal;

public record AccountDTO(
        Long id,
        String accountId,
        BigDecimal balance,
        String status,
        Long ownerId
) {}
