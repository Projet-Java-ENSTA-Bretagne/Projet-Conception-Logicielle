package database;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import database.entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import server.ConfigurationManagement;
import server.ServerConfiguration;

import java.net.Socket;
import java.sql.Timestamp;
import java.util.*;

public class SecurityManager {

    private HashMap<Socket, Boolean> loggedIn = new HashMap<Socket, Boolean>();
    private HashMap<Socket, User> loggedUsers = new HashMap<>();

    private final String ISSUER = "ENSTA-Bretagne";

    /* Logger */
    Logger log = LogManager.getLogger(SecurityManager.class);

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

    public boolean isUserLoggedIn(Socket client) {
        return this.loggedIn.get(client);
    }

    public User getLoggedUser(Socket client) throws UserNotLoggedException {
        // security if the user is note logged in
        if (!this.isUserLoggedIn(client)) {
            throw new UserNotLoggedException("An user needs to be logged in in order to do that");
        }

        return this.loggedUsers.get(client);
    }

    public void setLoggedUser(User newUser, Socket client) {
        this.loggedIn.put(client, true);
        this.loggedUsers.put(client, newUser);
    }

    public void removeLoggedUser(Socket client) {
        this.loggedIn.remove(client);
        this.loggedUsers.remove(client);
    }

    /**
     * Check if the role of the user is high enough to perform an action
     * @param requiredRole : The user role
     * @throws UnauthorizedException
     */
    public void hasRoleHighEnoughTo(Socket client, User.Role requiredRole) throws UnauthorizedException {
        // comparing roles to check if the user can do it
        if (this.loggedUsers.get(client).getRole().compareTo(requiredRole) < 0) {
            throw new UnauthorizedException("Role not high enough to perform action");
        }
    }

    /**
     * Function allow to create a JWT (Json Web Token)
     * @param user : The user
     * @return the token in string format
     */
    public String createJWT(User user)
    {
        try {
            ConfigurationManagement configurationManagement = new ConfigurationManagement();
            ServerConfiguration serverConfiguration = configurationManagement.getServerConfiguration();
            Algorithm algorithm = Algorithm.HMAC512(serverConfiguration.getTokenKey());

            Calendar calendar = Calendar.getInstance();
            Date now = calendar.getTime();
            calendar.add(Calendar.HOUR, 24);
            Date end = calendar.getTime();

            HashMap<String, String> payload = new HashMap<>();
            payload.put("user_id", user.getId());

            return JWT.create()
                    .withIssuer(ISSUER)
                    .withIssuedAt(new Timestamp(now.getTime()))
                    .withExpiresAt(new Timestamp(end.getTime()))
                    .withPayload(payload)
                    .sign(algorithm);
        }
        catch (JWTCreationException exception)
        {
            log.error("Error while the creation of token: " + exception.toString());
            return "";
        }
    }

    /**
     * Function allow you to decode token
     * @param token : the token JWT
     * @return the JWT decoded
     * @throws JWTVerificationException
     */
    public DecodedJWT decodeJWT(String token) throws JWTVerificationException {
        ConfigurationManagement configurationManagement = new ConfigurationManagement();
        ServerConfiguration serverConfiguration = configurationManagement.getServerConfiguration();
        Algorithm algorithm = Algorithm.HMAC512(serverConfiguration.getTokenKey());

        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(ISSUER)
                .build();

        return verifier.verify(token);
    }

    /**
     * Check if the token is valid
     * @param token : the token JWT
     * @return true if the token is valid, false else
     */
    public boolean isTokenValid(String token) {
        try {
            // testing the validity of the token
            DecodedJWT decodedJWT = decodeJWT(token);

            String payload = new String(Base64.getDecoder().decode(decodedJWT.getPayload()));
            log.info(payload);

            // TODO: add other checks

            return true;
        } catch (JWTVerificationException e) {
            log.warn("Error while verifying the token: " + e.toString());
            return false;
        }
    }
}
