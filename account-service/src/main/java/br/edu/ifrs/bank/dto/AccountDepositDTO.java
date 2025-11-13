package br.edu.ifrs.bank.dto;

import java.math.BigDecimal;

public record AccountDepositDTO(
        BigDecimal amount
    ){}