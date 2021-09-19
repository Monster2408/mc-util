package xyz.mlserver.java;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLUtil {

    public static Connection connection;
    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;

    public MySQLUtil(String host, int port, String database, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    public static Connection getConnection() { return connection; }

    public static void setConnection(Connection connection) { MySQLUtil.connection = connection; }

    public String getUser() { return username; }

    public String getHost() { return host; }

    public int getPort() { return port; }

    public String getDatabase() { return database; }

    public String getPassword() { return password; }

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

    public void openStaticConnection() {
        int port = mySQLUtil.getPort();
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

    public static MySQLUtil mySQLUtil;

    /**
     * HighFunctionalityLib必須
     * @return {1: "成功", 0: "HighFunctionalityLibがねえ...", -1: "失敗"}
     */
    public int OpenConnection() {
        Plugin highFLib = Bukkit.getPluginManager().getPlugin("HighFunctionalityLib");
        if (highFLib == null) return -1;
        if (mySQLUtil == null) {
            if (host == null) highFLib.getConfig().getString("host", "localhost");
            if (port < 0) highFLib.getConfig().getInt("host", 3306);
            if (database == null) highFLib.getConfig().getString("database", "database");
            if (username == null) highFLib.getConfig().getString("username", "username");
            if (password == null) highFLib.getConfig().getString("password", "password");

            mySQLUtil = new MySQLUtil(
                    host,
                    port,
                    database,
                    username,
                    password
            );
        }
        try {
            mySQLUtil.openConnection();
            return 1;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

}
