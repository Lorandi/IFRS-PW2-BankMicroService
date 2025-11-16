package br.edu.ifrs.orch.dto;

import java.math.BigDecimal;

public record AccountTransferDTO(
        String targetAccountId,
        BigDecimal amount
    ){}