package pageManagement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import java.io.IOException;
import java.io.File;
import java.net.URL;

import fsm.*;

/**
 * Class handling the switches between the main scenes (Login, Home, Discussion).
 */
public class MainController {
    // Logging
    private static final Logger log = LogManager.getLogger(MainController.class);

    private static Stage mainStage;

    public static void initializeMainStage(Stage newMainStage) {
        mainStage = newMainStage;
    }

    // the 3 main scenes
    private static Scene loginScene;
    private static Scene homeScene;
    private static Scene discussionScene;

    public static Scene getLoginScene() {
        return loginScene;
    }

    public static Scene getHomeScene() {
        return homeScene;
    }

    public static Scene getDiscussionScene() {
        return discussionScene;
    }

    private enum MainScene {
        NONE_YET,
        LOGIN,
        HOME,
        DISCUSSION
    }

    // NB : here "currentScene" is only a descriptive feature, not a Scene object !
    private static MainScene currentScene;

    private static void setCurrentScene(MainScene scene) {
        currentScene = scene;
    }

    /**
     * Loads the 3 main FXML files (login, home, discussion), so that the associated scenes
     * can be easily reused (statically) throughout the code.
     *
     * @throws IOException If error when FXMLLoader.load() is called
     */
    public static void initializeMainScenes() throws IOException {
        URL loginURL = new File("src/main/pages/login.fxml").toURI().toURL();
        Parent loginRoot = FXMLLoader.load(loginURL);
        loginScene = new Scene(loginRoot, 659, 402);

        URL homeURL = new File("src/main/pages/home.fxml").toURI().toURL();
        Parent homeRoot = FXMLLoader.load(homeURL);
        homeScene = new Scene(homeRoot, 659, 402);

        URL discussionURL = new File("src/main/pages/discussion.fxml").toURI().toURL();
        Parent discussionRoot = FXMLLoader.load(discussionURL);
        discussionScene = new Scene(discussionRoot, 659, 402);

        mainStage.getIcons().add(new Image("duck-icon.png")); // adding duck icon to main stage
        setCurrentScene(MainScene.NONE_YET);

        hasAlreadySwitchedToHomeScene = false;
        hasAlreadySwitchedToDiscussionScene = false;
    }

    private static IFiniteStateMachine fsm;

    public static IFiniteStateMachine getFSM() {
        return fsm;
    }

    /**
     * Initializes the finite state machine (FSM) that will be associated with
     * the state of the current user/client (relative to the server).
     */
    public static void initializeFSM() {
        // FSM

        IState idleState = new State("Idle");
        IState waitState = new State("Wait");
        IState sendingState = new State("Sending");

        Action connected =  new Action("Connected");
        Action disconnected = new Action("Disconnected");
        Action sending = new Action("Sending");
        Action quitSending = new Action("QuitSending");

        fsm = new FiniteStateMachine();
        fsm.setStartState(idleState);

        log.info("Initializing FSM - Current state : " + fsm.getCurrentState().getStateDesc());
        System.out.println("");

        fsm.addState(idleState, waitState, connected);
        fsm.addState(waitState, sendingState, sending);
        fsm.addState(sendingState, waitState, quitSending);
        fsm.addState(waitState, idleState, disconnected);

        // --> to switch states, do "fsm.transit(Action)"
    }

    /**
     * Switches to the Login scene. If already in the Login scene, nothing is done.
     */
    public static void switchToLoginScene() {
        if (currentScene != MainScene.LOGIN) {
            setCurrentScene(MainScene.LOGIN);

            mainStage.setTitle("Login");
            mainStage.setScene(loginScene);
            mainStage.show();
        }

        else {
            log.warn("Already in login scene !");
        }
    }

    private static boolean hasAlreadySwitchedToHomeScene;

    /**
     * Switches to the Home scene. If already in the Home scene, nothing is done.
     */
    public static void switchToHomeScene() {
        if (currentScene != MainScene.HOME) {
            setCurrentScene(MainScene.HOME);

            mainStage.setTitle("Home");
            mainStage.setScene(homeScene);
            mainStage.show();

            if (!hasAlreadySwitchedToHomeScene) {
                HomeController.initializeStaticComponents();
                hasAlreadySwitchedToHomeScene = true;
            }
        }

        else {
            log.warn("Already in home scene !");
        }
    }

    private static boolean hasAlreadySwitchedToDiscussionScene;

    /**
     * Switches to the Discussion scene. If already in the Discussion scene, nothing is done.
     */
    public static void switchToDiscussionScene() {
        if (currentScene != MainScene.DISCUSSION) {
            setCurrentScene(MainScene.DISCUSSION);

            mainStage.setTitle("Discussion");
            mainStage.setScene(discussionScene);
            mainStage.show();

            if (!hasAlreadySwitchedToDiscussionScene) {
                DiscussionController.initializeStaticComponents();
                hasAlreadySwitchedToDiscussionScene = true;
            }
        }

        else {
            log.warn("Already in discussion scene !");
        }
    }
}
