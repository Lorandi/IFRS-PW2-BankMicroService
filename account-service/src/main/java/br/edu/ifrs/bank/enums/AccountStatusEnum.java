package br.edu.ifrs.bank.enums;

public enum AccountStatusEnum {
    ACTIVE("Active"),
    BLOCKED("Blocked");

    private final String label;

    AccountStatusEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public boolean isActive() {
        return this == ACTIVE;
    }

    public boolean isBlocked() {
        return this == BLOCKED;
    }

    public AccountStatusEnum toggle() {
        return this == ACTIVE ? BLOCKED : ACTIVE;
    }

    @Override
    public String toString() {
        return label;
    }
}
