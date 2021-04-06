package server;

import org.json.JSONObject;

public class ServerConfiguration {
    /**
     * The hostname of the server
     */
    private String host;

    /**
     * The port of the server
     */
    private int port;

    /**
     * The number of max clients on this server
     */
    private int maxClients;

    /**
     * The unique token key
     */
    private String tokenKey;

    /**
     * Constructeur of ServerConfiguration
     * @param host : the hostname
     * @param port : the port
     * @param maxClients : the number of max clients
     * @param masterKey : the master key
     */
    public ServerConfiguration(String host, int port, int maxClients, String masterKey) {
        this.host = host;
        this.maxClients = maxClients;
        this.port = port;
        this.tokenKey = masterKey;
    }

    /**
     * Get server configuration from a json object
     * @param jsonObject : the server configuration in json format
     * @return a ServerConfiguration object
     */
    public static ServerConfiguration fromJsonObject(JSONObject jsonObject) {
        return new ServerConfiguration(
                jsonObject.getString("host"),
                jsonObject.getInt("port"),
                jsonObject.getInt("maxClients"),
                jsonObject.getString("tokenKey")
        );
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public int getMaxClients() {
        return maxClients;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setMaxClients(int maxClients) {
        this.maxClients = maxClients;
    }

    public String getTokenKey() {
        return tokenKey;
    }

    public void setTokenKey(String tokenKey) {
        this.tokenKey = tokenKey;
    }

    @Override
    public String toString() {
        return "ServerConfiguration{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", maxClients=" + maxClients +
                '}';
    }
}
