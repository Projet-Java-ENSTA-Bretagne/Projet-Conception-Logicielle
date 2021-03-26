package pageManagement;

import FSM.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;

/**
 * Class handling the JavaFX objects from the Login scene (defined in login.fxml).
 */
public class LoginController {
    // Logging
    private static final Logger log = LogManager.getLogger(LoginController.class);

    /**
     * Method that is executed right before "login.fxml" is loaded.
     */
    @FXML
    private void initialize() {
        System.out.println("");
        log.info("Initializing login controller");

        nbSuccessfulLogins = 0;

        allowedToChangeUsername = false;
        username = null;
        currentUsernameEntry = null;

        allowedToChangePassword = false;
        password = null;
        currentPasswordEntry = null;

        // fsm

        IState idleState = new State("Idle");
        IState waitState = new State("Wait");
        IState sendingState = new State("Sending");

        Action connected =  new Action("Connected");
        Action disconnected = new Action("Disconnected");
        Action sending = new Action("Sending");
        Action quitSending = new Action("QuitSending");


        IFiniteStateMachine fsm = new FiniteStateMachine();
        fsm.setStartState(idleState);
        log.info("initializing FSM:"+ fsm.getCurrentState().getStateDesc());

        fsm.addState(idleState, waitState, connected);
        fsm.addState(waitState, sendingState, sending);
        fsm.addState(sendingState, waitState, quitSending);
        fsm.addState(waitState, idleState, disconnected);

        // to switch states do fsm.transit(Action)


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
    private void makeLogin() {
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
                // changing/setting username and password
                setUsername(currentUsernameEntry);
                setPassword(currentPasswordEntry);

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

    /**
     * Action that is executed when the eye icon/JFXButton is clicked.
     * Unmasks the current password entry.
     */
    @FXML
    private void unmaskPassword() {
        currentPasswordEntry = passwordField.getText();
        passwordField.clear();
        passwordField.setPromptText(currentPasswordEntry);
    }

    /**
     * Action that is executed when the eye icon/JFXButton is released.
     * Masks the current password entry.
     */
    @FXML
    private void maskPassword() {
        passwordField.setText(currentPasswordEntry);
        passwordField.setPromptText("Password");
    }
}
