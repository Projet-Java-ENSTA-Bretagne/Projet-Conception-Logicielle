package database;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import database.entities.User;
import server.ConfigurationManagement;
import server.ServerConfiguration;

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

    public User getLoggedUser() throws UserNotLoggedException {
        // security if the user is note logged in
        if (!this.isUserLoggedIn()) {
            throw new UserNotLoggedException("An user needs to be logged in in order to do that");
        }

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

    public DecodedJWT checkToken(String token)
    {
        try {
            ConfigurationManagement configurationManagement = new ConfigurationManagement();
            ServerConfiguration serverConfiguration = configurationManagement.getServerConfiguration();
            Algorithm algorithm = Algorithm.HMAC512(serverConfiguration.getMasterKey());
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build();
            return verifier.verify(token);
        }
        catch (JWTVerificationException exception)
        {
            exception.printStackTrace();
            return null;
        }
    }
}
