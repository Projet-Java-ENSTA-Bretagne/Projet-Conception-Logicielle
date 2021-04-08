import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

public class StartingApp {
    public static void main(String[] args) {
        // Setting logging level
        Configurator.setRootLevel(Level.DEBUG);

        // Executes the "start" method
        MainClient.startApp(args);
    }
}
