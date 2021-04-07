package pageManagement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONException;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXPasswordField;
import javafx.fxml.FXML;

import networking.RequestBuilder;

/**
 * Class handling the JavaFX objects from the Login scene (defined in login.fxml).
 */
public class LoginController {
    // Logging
    private static final Logger log = LogManager.getLogger(LoginController.class);

    /**
     * Method that is automatically executed right after "login.fxml" is loaded.
     */
    @FXML
    private void initialize() {
        System.out.println("");
        log.info("Initializing login controller");

        currentUsernameEntry = "";
        currentPasswordEntry = "";
    }

    @FXML
    private JFXTextField usernameField;

    private static String currentUsernameEntry;

    @FXML
    private JFXPasswordField passwordField;

    private static String currentPasswordEntry;

    /**
     * Action linked to the "Login" JFXButton. Checks if the username and the password entries
     * are valid, then tries to connect to the server.
     */
    @FXML
    private void actionLoginButton() {
        currentUsernameEntry = usernameField.getText();
        boolean usernameIsValid;
        if ((currentUsernameEntry == null) || (currentUsernameEntry.length() == 0)) {
            usernameIsValid = false;
        }
        else {
            if (currentUsernameEntry.length() > 12) {
                usernameIsValid = false;
                System.out.println("");
                log.error("The current username entry has more than 12 characters !");
            }
            else {
                usernameIsValid = true;
            }
        }

        currentPasswordEntry = passwordField.getText();
        boolean passwordIsValid;
        if ((currentPasswordEntry == null) || (currentPasswordEntry.length() == 0)) {
            passwordIsValid = false;
        }
        else {
            passwordIsValid = true;
        }

        if (usernameIsValid && passwordIsValid) {
            boolean connectionIsSuccessful = MainController.getTcpClient().connectToServer();

            if (connectionIsSuccessful) {
                try {
                    String pingStatus = pingRequest();

                    if (pingStatus.equals("OK")) {
                        // communicating the login data (username + password) to the server

                        JSONObject loginData = new JSONObject();
                        loginData.put("username", currentUsernameEntry);
                        loginData.put("password", currentPasswordEntry);

                        // attempting to create a user
                        String createUserStatus = createUserRequest(loginData);

                        if (createUserStatus.equals("OK") || createUserStatus.equals("DENIED")) {
                            // attempting to login
                            String[] loginStatusAndUserID = loginRequest(loginData);

                            String loginStatus = loginStatusAndUserID[0];
                            String currentUserID = loginStatusAndUserID[1];

                            if (loginStatus.equals("OK")) {
                                // if we reach this "if" block, then it means that (at least) the login
                                // was successful ! (indeed, the creation of the new user might not have been
                                // successful, which isn't the end of the world)

                                System.out.println("");
                                log.debug("Current valid username entry : \"" + currentUsernameEntry + "\"");
                                log.debug("Current valid password entry : \"" + currentPasswordEntry + "\"");

                                DiscussionController.setCurrentSender(currentUsernameEntry);
                                DiscussionController.setCurrentSenderID(currentUserID);

                                System.out.println("");
                                log.info("Welcome !");
                                MainController.switchToHomeScene();

                                HomeController.getGroupThumbnailObjectList().clear();
                                HomeController.clearGroupThumbnailHBox();
                                HomeController.resetNbGroupsYouAreStillPartOf();
                                HomeController.aGroupIsCurrentlyBeingDeleted = false;
                                DiscussionController.getGroupObjectList().clear();

                                HomeController.clearHashMap();
                                HomeController.loadExistingGroups();
                            }
                            else {
                                log.error("Invalid username or password. Please try again ! (loginStatus : \"" + loginStatus + "\")");
                            }
                        }

                        else {
                            log.error("La communication avec le serveur pour le login est corrompue (createUserStatus : \"" + createUserStatus + "\")");
                            System.exit(1);
                        }
                    }

                    else {
                        log.error("La communication initiale avec le serveur est corrompue (pingStatus : \"" + pingStatus + "\")");
                        System.exit(1);
                    }
                }
                catch (JSONException jsonException) {
                    log.error("Erreur JSON détectée : " + jsonException);
                    System.exit(1);
                }
            }

            else {
                log.error("Le client n'a pas pu se connecter au serveur");
                System.exit(1);
            }
        }

        else {
            if (currentUsernameEntry.length() <= 12) {
                System.out.println("");
            }
            log.error("Invalid username or password. Please try again !");
        }
    }

    private static String pingRequest() {
        String pingRequest = RequestBuilder.buildWithoutData("PING").toString();
        String responseFromServer = MainController.getTcpClient().sendRequest(pingRequest);

        JSONObject wholeReceivedData = new JSONObject(responseFromServer);
        String pingStatus = wholeReceivedData.getString("status");

        return pingStatus;
    }

    private String createUserRequest(JSONObject loginData) {
        String createUserRequest = RequestBuilder.buildWithData("createUser", loginData).toString();
        String responseFromServer = MainController.getTcpClient().sendRequest(createUserRequest);

        JSONObject wholeReceivedData = new JSONObject(responseFromServer);
        String createUserStatus = wholeReceivedData.getString("status");

        return createUserStatus;
    }

    private String[] loginRequest(JSONObject loginData) {
        String loginRequest = RequestBuilder.buildWithData("login", loginData).toString();
        String responseFromServer = MainController.getTcpClient().sendRequest(loginRequest);

        // getting login status
        JSONObject wholeReceivedData = new JSONObject(responseFromServer);
        String loginStatus = wholeReceivedData.getString("status");

        // getting userID
        JSONObject usefulReceivedData = wholeReceivedData.getJSONObject("data");

        String userID;
        if (loginStatus.equals("OK")) {
            userID = usefulReceivedData.getString("user_id");
        }
        else {
            userID = "none";
        }

        String[] loginStatusAndUserID = {loginStatus, userID};
        return loginStatusAndUserID;
    }

    /**
     * Action that is executed when the eye icon/JFXButton is clicked.
     * Unmasks the current password entry.
     */
    @FXML
    private void unmaskPassword() {
        currentPasswordEntry = passwordField.getText();

        if ((currentPasswordEntry == null) || (currentPasswordEntry.length() == 0)) {
            return;
        }
        else {
            passwordField.clear();
            passwordField.setPromptText(currentPasswordEntry);
        }
    }

    /**
     * Action that is executed when the eye icon/JFXButton is released.
     * Masks the current (unmasked) password entry.
     */
    @FXML
    private void maskPassword() {
        if ((currentPasswordEntry == null) || (currentPasswordEntry.length() == 0)) {
            return;
        }
        else {
            passwordField.setText(currentPasswordEntry);
            passwordField.setPromptText("Password");
        }
    }
}
