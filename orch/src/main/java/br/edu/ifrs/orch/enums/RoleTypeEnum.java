package br.edu.ifrs.orch.enums;

public enum RoleTypeEnum {
    CUSTOMER("Customer"),
    ADMIN("Admin"),
    AUDITOR("Auditor");

    private final String label;

    RoleTypeEnum(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
