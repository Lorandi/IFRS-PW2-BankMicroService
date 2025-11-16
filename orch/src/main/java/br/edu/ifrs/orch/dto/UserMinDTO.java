package br.edu.ifrs.orch.dto;


import br.edu.ifrs.orch.enums.RoleTypeEnum;

public record UserMinDTO(
        Long id,
        String username,
        RoleTypeEnum role
) {}
