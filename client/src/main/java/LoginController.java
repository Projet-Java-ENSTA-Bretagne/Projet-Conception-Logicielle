import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;

public class LoginController {

    // Logging
    private static final Logger log = LogManager.getLogger(LoginController.class);

    @FXML
    void initialize() {
        System.out.println("");
        log.info("Initializing login controller");

        nbSuccessfulLogins = 0;

        allowedToChangeUsername = false;
        username = null;

        allowedToChangePassword = false;
        password = null;
    }

    @FXML
    private JFXTextField usernameField;

    private static boolean allowedToChangeUsername;
    private static String username;

    private static void setUsername(String newUsername) {
        if (allowedToChangeUsername) {
            username = newUsername;
        }
    }

    @FXML
    private JFXPasswordField passwordField;

    private static boolean allowedToChangePassword;
    private static String password;

    private static void setPassword(String newPassword) {
        if (allowedToChangePassword) {
            password = newPassword;
        }
    }

    private static int nbSuccessfulLogins;

    @FXML
    void makeLogin() {
        if (nbSuccessfulLogins == 0) {
            allowedToChangeUsername = true;
            allowedToChangePassword = true;
        }

        // can easily be changed manually
        boolean allowedToChangeLoginData = (allowedToChangeUsername && allowedToChangePassword);

        String currentUsernameText = usernameField.getText();
        boolean usernameIsvalid = ((allowedToChangeLoginData && (currentUsernameText != null) && (currentUsernameText.length() > 0))
                                   ||
                                   ((!allowedToChangeLoginData) && currentUsernameText.equals(username)));

        String currentPasswordText = passwordField.getText();
        boolean passwordIsvalid = ((allowedToChangeLoginData && (currentPasswordText != null) && (currentPasswordText.length() > 0))
                                   ||
                                   ((!allowedToChangeLoginData) && currentPasswordText.equals(password)));

        if (usernameIsvalid && passwordIsvalid) {
            nbSuccessfulLogins += 1;

            if (allowedToChangeLoginData) {
                // changing/setting username
                setUsername(currentUsernameText);
                allowedToChangeUsername = false;

                // changing/setting password
                setPassword(currentPasswordText);
                allowedToChangePassword = false;

                System.out.println("\n");
                log.debug("New username : \"" + username + "\"");
                log.debug("New password : \"" + password + "\"\n");
            }

            log.info("Welcome !");
            MainController.switchToHomeScene();
        }

        else {
            System.out.println("\n");
            log.error("Invalid username or password. Please try again !\n");
        }

        System.out.flush();
    }

    @FXML
    void unmaskPassword() {
        password = passwordField.getText();
        passwordField.clear();
        passwordField.setPromptText(password);
    }

    @FXML
    void maskPassword() {
        passwordField.setText(password);
        passwordField.setPromptText("Password");
    }
}
