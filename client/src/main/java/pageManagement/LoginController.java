package pageManagement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;

public class LoginController {
    /**
     * Class handling the JavaFX objects from the Login scene (defined in login.fxml).
     */

    // Logging
    private static final Logger log = LogManager.getLogger(LoginController.class);

    @FXML
    void initialize() {
        /**
         * Method that is executed right before "login.fxml" is loaded.
         * @param void
         * @return void
         */

        System.out.println("");
        log.info("Initializing login controller");

        nbSuccessfulLogins = 0;

        allowedToChangeUsername = false;
        username = null;
        currentUsernameEntry = null;

        allowedToChangePassword = false;
        password = null;
        currentPasswordEntry = null;
    }

    @FXML
    private JFXTextField usernameField;

    private static boolean allowedToChangeUsername;
    private static String username;
    private static String currentUsernameEntry;

    private static void setUsername(String newUsername) {
        if (allowedToChangeUsername) {
            username = newUsername;
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
        }
    }

    private static int nbSuccessfulLogins;

    @FXML
    void makeLogin() {
        /**
         * Action linked to the "Login" JFXButton.
         * Checks if the username and the password entries are valid, then
         * tries to connect to the server.
         * @param void
         * @return void
         * TODO : Link this method to network
         */

        if (nbSuccessfulLogins == 0) {
            allowedToChangeUsername = true;
            allowedToChangePassword = true;
        }

        // can easily be changed manually
        boolean allowedToChangeLoginData = (allowedToChangeUsername && allowedToChangePassword);

        currentUsernameEntry = usernameField.getText();
        boolean usernameIsvalid = ((allowedToChangeLoginData && (currentUsernameEntry != null) && (currentUsernameEntry.length() > 0))
                                   ||
                                   ((!allowedToChangeLoginData) && currentUsernameEntry.equals(username)));

        currentPasswordEntry = passwordField.getText();
        boolean passwordIsvalid = ((allowedToChangeLoginData && (currentPasswordEntry != null) && (currentPasswordEntry.length() > 0))
                                   ||
                                   ((!allowedToChangeLoginData) && currentPasswordEntry.equals(password)));

        if (usernameIsvalid && passwordIsvalid) {
            nbSuccessfulLogins += 1;

            if (allowedToChangeLoginData) {
                // changing/setting username
                setUsername(currentUsernameEntry);
                allowedToChangeUsername = false;

                // changing/setting password
                setPassword(currentPasswordEntry);
                allowedToChangePassword = false;

                System.out.println("");
                log.debug("New username : \"" + username + "\"");
                log.debug("New password : \"" + password + "\"");
            }

            System.out.println("");
            log.info("Welcome !");
            MainController.switchToHomeScene();
        }

        else {
            System.out.println("");
            log.error("Invalid username or password. Please try again !");
        }

        System.out.flush();
    }

    @FXML
    void unmaskPassword() {
        /**
         * Action that is executed when the eye icon/JFXButton is clicked.
         * Unmasks the current password entry.
         * @param void
         * @return void
         */

        currentPasswordEntry = passwordField.getText();
        passwordField.clear();
        passwordField.setPromptText(currentPasswordEntry);
    }

    @FXML
    void maskPassword() {
        /**
         * Action that is executed when the eye icon/JFXButton is released.
         * Masks the current password entry.
         * @param void
         * @return void
         */

        passwordField.setText(currentPasswordEntry);
        passwordField.setPromptText("Password");
    }
}
