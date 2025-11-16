package br.edu.ifrs.orch.dto;

public record UserSignupRequestDTO(
        String username,
        String password,
        String cpf
) {}
