package br.edu.ifrs.bank.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChangePasswordDTO(
        @NotNull
        Long userId,

        @NotBlank String
        oldPassword,

        @NotBlank String
        newPassword
) {}
