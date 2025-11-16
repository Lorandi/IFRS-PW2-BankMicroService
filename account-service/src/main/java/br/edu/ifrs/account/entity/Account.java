package br.edu.ifrs.account.entity;

import br.edu.ifrs.account.enums.AccountStatusEnum;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

import static jakarta.persistence.EnumType.STRING;

@Data
@With
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String accountId;

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @Enumerated(STRING)
    @Column(nullable = false)
    private AccountStatusEnum status;

    @Column(nullable = false, unique = true)
    private Long ownerId;
}
