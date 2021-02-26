package database;

import database.entities.User;

public class SecurityManager {

    private boolean loggedIn = false;
    private User loggedUser;

    /** private constructor */
    private SecurityManager() {}

    /** unique instance */
    private static SecurityManager INSTANCE = null;

    /** unique instantiation and getting the instance
     * this function is threadsafe */
    public static synchronized SecurityManager getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new SecurityManager();
        }
        return INSTANCE;
    }

    /** anti-deserialization security */
    private Object readResolve() {
        return INSTANCE;
    }

    public boolean isUserLoggedIn() {
        return this.loggedIn;
    }

    public User getLoggedUser() {
        // security if the user is note logged in
        if (!this.isUserLoggedIn()) { return null; }

        return this.loggedUser;
    }

    public void setLoggedUser(User newUser) {
        this.loggedIn = true;
        this.loggedUser = newUser;
    }

    public void removeLoggedUser() {
        this.loggedIn = false;
        this.loggedUser = null;
    }

    public void hasRoleHighEnoughTo(User.Role requiredRole) throws UnauthorizedException {
        // comparing roles to check if the user can do it
        if (this.loggedUser.getRole().compareTo(requiredRole) < 0) {
            throw new UnauthorizedException("Role not high enough to perform action");
        }
    }
}
