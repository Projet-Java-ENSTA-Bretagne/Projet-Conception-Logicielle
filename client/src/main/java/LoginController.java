import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;

public class LoginController {

    @FXML
    private JFXTextField usernameField;

    @FXML
    private JFXPasswordField passwordField;

    private String password;

    @FXML
    void makeLogin() {
        if (usernameField.getText().equals("test") && passwordField.getText().equals("test")) {
            System.out.println("Welcome");
            MainController.switchToHomeScene();
        }
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
