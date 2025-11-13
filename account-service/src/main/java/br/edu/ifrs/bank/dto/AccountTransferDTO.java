package br.edu.ifrs.bank.dto;

import java.math.BigDecimal;

public record AccountTransferDTO(
        Long ownerId,
        String targetAccountId,
        BigDecimal amount
    ){}