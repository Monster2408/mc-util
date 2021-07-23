package xyz.mlserver.java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLUtil {

    public static Connection connection;
    private static String host;
    private static int port;
    private static String database;
    private static String username;
    private static String password;

    public MySQLUtil(String host, int port, String database, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    public static Connection getConnection() { return connection; }

    public static void setConnection(Connection connection) { MySQLUtil.connection = connection; }

    public static String getUser() { return username; }

    public static String getHost() { return host; }

    public static int getPort() { return port; }

    public static String getDatabase() { return database; }

    public static String getPassword() { return password; }

    public void openConnection() throws SQLException, ClassNotFoundException {
        if (connection != null && !connection.isClosed()) {
            return;
        }

        synchronized (this) {
            if (connection != null && !connection.isClosed()) {
                return;
            }
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&useLegacyDatetimeCode=false&autoReconnect=true&serverTimezone=JST", username, password);
        }
    }

    public static void openStaticConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                return;
            }

            synchronized (EventUtil.class) {
                if (connection != null && !connection.isClosed()) {
                    return;
                }
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&useLegacyDatetimeCode=false&autoReconnect=true&serverTimezone=JST", username, password);
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

}
