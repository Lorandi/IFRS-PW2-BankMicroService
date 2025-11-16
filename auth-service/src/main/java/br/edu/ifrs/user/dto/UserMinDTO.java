package br.edu.ifrs.user.dto;

import br.edu.ifrs.user.enums.RoleTypeEnum;

public record UserMinDTO(
        Long id,
        String username,
        RoleTypeEnum role
) {}
