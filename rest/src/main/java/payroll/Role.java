package payroll;

public enum Role {
    DEVELOPER("Developer"),
    MANAGER("Manager"),
    DESIGNER("Designer"),
    QA_ENGINEER("QA Engineer"),
    DEVOPS("DevOps"),
    PRODUCT_MANAGER("Product Manager");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Role fromString(String roleString) {
        if (roleString == null) {
            return null;
        }

        for (Role role : Role.values()) {
            if (role.displayName.equalsIgnoreCase(roleString.trim())) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid role: '" + roleString
                + "'. Valid roles are: Developer, Manager, Designer, QA Engineer, DevOps, Product Manager");
    }

    @Override
    public String toString() {
        return displayName;
    }
}
