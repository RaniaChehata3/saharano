package atlantafx.sampler.view.dashboard;

import atlantafx.sampler.controller.AuthController;
import atlantafx.sampler.model.Role;
import atlantafx.sampler.model.User;

/**
 * Factory class that creates the appropriate dashboard view based on the user's role.
 */
public class DashboardFactory {

    private static DashboardFactory instance;
    private final AuthController authController;

    private DashboardFactory() {
        this.authController = AuthController.getInstance();
    }

    public static synchronized DashboardFactory getInstance() {
        if (instance == null) {
            instance = new DashboardFactory();
        }
        return instance;
    }

    /**
     * Creates a dashboard view appropriate for the current user's role.
     *
     * @return The role-specific dashboard view
     * @throws IllegalStateException if no user is authenticated
     */
    public DashboardView createDashboard() {
        User currentUser = authController.getCurrentUser();
        
        if (currentUser == null) {
            throw new IllegalStateException("No user is authenticated");
        }
        
        return createDashboardForRole(currentUser.getRole());
    }
    
    /**
     * Creates a dashboard view for the currently authenticated user.
     * Returns null if no user is authenticated.
     *
     * @return The dashboard view for the current user, or null if not authenticated
     */
    public DashboardView createDashboardForCurrentUser() {
        User currentUser = authController.getCurrentUser();
        
        if (currentUser == null) {
            return null;
        }
        
        return createDashboardForRole(currentUser.getRole());
    }
    
    /**
     * Creates a dashboard view for a specific role.
     *
     * @param role The role to create a dashboard for
     * @return The role-specific dashboard view
     */
    public DashboardView createDashboardForRole(Role role) {
        return switch (role) {
            case ADMINISTRATOR -> new AdminDashboardView();
            case PATIENT -> new PatientDashboardView();
            case DOCTOR -> new DoctorDashboardView();
            case LABORATORY -> new LaboratoryDashboardView();
            case VISITOR -> new VisitorDashboardView();
        };
    }
} 