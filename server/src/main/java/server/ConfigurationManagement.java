package server;

import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * ConfigurationManagement is use for edit your server configuration so if you want edit le hostname, port or maxClient allowed.
 */
public class ConfigurationManagement {
    // The path of server configuration file
    private String configurationPath;

    // The instance of server configuration
    private ServerConfiguration serverConfiguration;

    // The JSONObject with server configuration
    private JSONObject jsonObject;

    private static ConfigurationManagement INSTANCE = null;

    /**
     * Constructor for ConfigurationManagement
     */
    public ConfigurationManagement()
    {
        this.configurationPath = "config.json";
        try {
            this.jsonObject = new JSONObject(Files.readString(Paths.get(configurationPath)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.serverConfiguration = ServerConfiguration.fromJsonObject(this.jsonObject);
    }

    /**
     * Fonction to getInstance
     * @return the instance
     */
    public static synchronized ConfigurationManagement getInstance()
    {
        if(INSTANCE == null)
        {
            INSTANCE = new ConfigurationManagement();
        }
        return INSTANCE;
    }

    /**
     * Function used to configure the jsonObject fromPath
     * @param filePath, the path of the file
     */
    public void fromPath(String filePath)
    {
        this.configurationPath = filePath;
        File file = new File(this.configurationPath);
        fromFile(file);
    }

    /**
     * Function used to configure the JSONObject with a file
     * @param file, the file used
     */
    public void fromFile(File file)
    {
        try {
            this.jsonObject = new JSONObject(Files.readString(Paths.get(configurationPath)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.serverConfiguration = ServerConfiguration.fromJsonObject(this.jsonObject);
    }

    /**
     * Function to configure the JSONObject with a instance of ServerConfiguration
     * @param serverConfiguration, the instance used
     */
    public void fromConfig(ServerConfiguration serverConfiguration) {
        this.configurationPath = "config.json";
        this.serverConfiguration = serverConfiguration;
        try {
            this.jsonObject = new JSONObject(Files.readString(Paths.get(configurationPath)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fonction used for edit the server configuration
     * @param serverConfiguration, the serverConfiguration used
     */
    public void editServerConfiguration(ServerConfiguration serverConfiguration)
    {
        jsonObject.remove("host");
        jsonObject.put("maxClients", serverConfiguration.getMaxClients());
        jsonObject.put("port", serverConfiguration.getPort());
        jsonObject.put("host", serverConfiguration.getHost());
        FileWriter writer = null;
        try {
            writer = new FileWriter(this.configurationPath);
            writer.write(jsonObject.toString(4));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Function allowed to save the server configuration file
     * @param filePath, the path of the server configuration file
     */
    public void saveServerConfiguration(String filePath)
    {
        JSONObject jsonData = new JSONObject();
        jsonData.put("maxClients", serverConfiguration.getMaxClients());
        jsonData.put("port", serverConfiguration.getPort());
        jsonData.put("host", serverConfiguration.getHost());
        FileWriter writer = null;
        try {
            writer = new FileWriter(filePath);
            writer.write(jsonData.toString(4));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Function allowed to get the server configuration
     * @return the content of server configuration file
     */
    public ServerConfiguration getServerConfiguration()
    {
        return this.serverConfiguration;
    }
}
