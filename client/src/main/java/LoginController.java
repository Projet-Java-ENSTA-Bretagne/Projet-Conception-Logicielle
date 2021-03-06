import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;

public class LoginController {

    @FXML
    void initialize() {
        System.out.println("init login controller");

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

                System.out.printf("\nNew username : %s", username);
                System.out.printf("\nNew password : %s\n", password);
            }

            System.out.println("Welcome !");
            MainController.switchToHomeScene();
        }

        else {
            System.out.println("\nInvalid username or password. Please try again");
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
