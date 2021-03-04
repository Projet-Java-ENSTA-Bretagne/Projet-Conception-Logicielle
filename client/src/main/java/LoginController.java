import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;

public class LoginController {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label enstarLabel;

    @FXML
    private Label welcomeLabel;

    @FXML
    private JFXTextField usernameField;

    @FXML
    private JFXPasswordField passwordField;

    @FXML
    private JFXButton login;

    @FXML
    private JFXButton showPasswordButton;
    private String password;

    @FXML
    void makeLogin() {
        if(usernameField.getText().equals("test")&&passwordField.getText().equals("test"))
        {
            System.out.println("Welcome");
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
