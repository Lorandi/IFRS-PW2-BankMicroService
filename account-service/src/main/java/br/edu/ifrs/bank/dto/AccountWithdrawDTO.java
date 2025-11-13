package br.edu.ifrs.bank.dto;

import java.math.BigDecimal;

public record AccountWithdrawDTO(
        Long ownerId, BigDecimal amount
    ){}