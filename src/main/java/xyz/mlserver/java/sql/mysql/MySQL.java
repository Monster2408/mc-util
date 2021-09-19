package xyz.mlserver.java.sql.mysql;

public class MySQL {

    private final String host, database, username, password, properties;
    private final int port;

    public MySQL(String host, int port, String database, String username, String password, String properties) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.properties = properties;
    }

    public MySQL(String host, String database, String username, String password, String properties) {
        this.host = host;
        this.port = -1;
        this.database = database;
        this.username = username;
        this.password = password;
        this.properties = properties;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getProperties() {
        return properties;
    }

}
