package pageManagement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXPasswordField;
import javafx.fxml.FXML;

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

        nbSuccessfulLogins = 0;

        allowedToChangeUsername = false;
        username = "";
        currentUsernameEntry = "";

        allowedToChangePassword = false;
        password = "";
        currentPasswordEntry = "";
    }

    @FXML
    private JFXTextField usernameField;

    private static boolean allowedToChangeUsername;
    private static String username;
    private static String currentUsernameEntry;

    private static void setUsername(String newUsername) {
        if (allowedToChangeUsername) {
            username = newUsername;
            DiscussionController.setCurrentSender(username);
            allowedToChangeUsername = false;
        }
    }

    @FXML
    private JFXPasswordField passwordField;

    private static boolean allowedToChangePassword;
    private static String password;
    private static String currentPasswordEntry;

    private static void setPassword(String newPassword) {
        if (allowedToChangePassword) {
            password = newPassword;
            allowedToChangePassword = false;
        }
    }

    private static int nbSuccessfulLogins;

    /**
     * Action linked to the "Login" JFXButton.
     * Checks if the username and the password entries are valid, then
     * tries to connect to the server.
     * TODO : Link this method to network
     */
    @FXML
    private void actionLoginButton() {
        if (nbSuccessfulLogins == 0) {
            allowedToChangeUsername = true;
            allowedToChangePassword = true;
        }

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
                usernameIsValid = (allowedToChangeUsername || currentUsernameEntry.equals(username));
            }
        }

        currentPasswordEntry = passwordField.getText();
        boolean passwordIsValid;
        if ((currentPasswordEntry == null) || (currentPasswordEntry.length() == 0)) {
            passwordIsValid = false;
        }
        else {
            passwordIsValid = (allowedToChangePassword || currentPasswordEntry.equals(password));
        }

        if (usernameIsValid && passwordIsValid) {
            nbSuccessfulLogins += 1;

            if (allowedToChangeUsername) {
                setUsername(currentUsernameEntry);
                System.out.println("");
                log.debug("New username : \"" + username + "\"");
            }

            if (allowedToChangePassword) {
                setPassword(currentPasswordEntry);
                log.debug("New password : \"" + password + "\"");
            }

            System.out.println("");
            log.info("Welcome !");
            MainController.switchToHomeScene();
        }

        else {
            if (currentUsernameEntry.length() <= 12) {
                System.out.println("");
            }
            log.error("Invalid username or password. Please try again !");
        }
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
     * Masks the current password entry.
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
