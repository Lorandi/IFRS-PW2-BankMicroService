package br.edu.ifrs.orch.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminAuditUser {

    private Long id;

    private String operationId;

    private String action;

    private Long userId;

    private String result;

    private LocalDateTime timestamp;

    private String errorMessage;
}
