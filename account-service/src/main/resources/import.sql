-- População inicial de contas bancárias (para ambiente DEV/TEST)
-- Cada conta pertence a um usuário identificado pelo ownerId numérico.

INSERT INTO account (accountId, balance, status, ownerId)
VALUES ( 'ACC1001', 1500.00, 'ACTIVE', 1);

INSERT INTO account (accountId, balance, status, ownerId)
VALUES ('ACC1002', 2750.50, 'ACTIVE', 2);

INSERT INTO account (accountId, balance, status, ownerId)
VALUES ('ACC1003', 0.00, 'BLOCKED', 3);

INSERT INTO account (accountId, balance, status, ownerId)
VALUES ('ACC2001', 12500.75, 'ACTIVE', 99);