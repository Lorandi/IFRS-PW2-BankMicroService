package br.edu.ifrs.bank.dto;

import br.edu.ifrs.bank.enums.RoleTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserMinDTO(
        Long id,
        String username,
        RoleTypeEnum role
) {}
