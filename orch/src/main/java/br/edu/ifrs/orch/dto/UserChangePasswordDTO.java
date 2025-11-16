package br.edu.ifrs.orch.dto;

public record UserChangePasswordDTO(
        Long userId,
        String oldPassword,
        String newPassword
) {}
