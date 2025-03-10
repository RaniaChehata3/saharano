package atlantafx.sampler.model;

/**
 * Represents user roles in the system with specific access privileges.
 */
public enum Role {
    ADMINISTRATOR("Administrator"),
    DOCTOR("Doctor"),
    PATIENT("Patient"),
    LABORATORY("Laboratory"),
    VISITOR("Visitor");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
} 