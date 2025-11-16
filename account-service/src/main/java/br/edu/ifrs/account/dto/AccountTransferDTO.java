package br.edu.ifrs.account.dto;

import java.math.BigDecimal;

public record AccountTransferDTO(
        String targetAccountId,
        BigDecimal amount
    ){}