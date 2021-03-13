package server;

import org.json.JSONObject;

public class ServerConfiguration {
    private String host;
    private int port;
    private int maxClients;
    private String masterKey;

    public ServerConfiguration(String host, int port, int maxClients, String masterKey) {
        this.host = host;
        this.maxClients = maxClients;
        this.port = port;
        this.masterKey = masterKey;
    }

    public static ServerConfiguration fromJsonObject(JSONObject jsonObject) {
        return new ServerConfiguration(
                jsonObject.getString("host"),
                jsonObject.getInt("port"),
                jsonObject.getInt("maxClients"),
                jsonObject.getString("masterKey")
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

    public String getMasterKey() {
        return masterKey;
    }

    public void setMasterKey(String masterKey) {
        this.masterKey = masterKey;
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
