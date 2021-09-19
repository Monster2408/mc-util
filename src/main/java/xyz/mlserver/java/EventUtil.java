package xyz.mlserver.java;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import xyz.mlserver.java.sql.DataBase;
import xyz.mlserver.java.sql.mysql.MySQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class EventUtil {

    private static String table_name;
    private static HashMap<String, Integer> winData;
    private static HashMap<String, Integer> gameData;
    private static DataBase dataBase;

    public static void addWin(String uuid, int i) {
        if (dataBase == null) {
            Log.error("load関数を実行してください。");
            return;
        }

        gameData.putIfAbsent(uuid, 0);
        winData.putIfAbsent(uuid, 0);
        winData.put(uuid, winData.get(uuid) + i);

        String sql = "insert into " + table_name + " (uuid, game, win) "
                + "VALUES ('"+uuid+"', " + gameData.get(uuid) + ", " + winData.get(uuid) + ") "
                +"ON DUPLICATE KEY UPDATE "
                +"uuid='" + uuid + "', "
                +"game=" + gameData.get(uuid) + ", "
                +"win=" + winData.get(uuid) + ";";
        try(Connection con = dataBase.getDataSource().getConnection();
            PreparedStatement prestat = con.prepareStatement(sql)) {
            prestat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addWin(String uuid) { addWin(uuid, 1); }

    public static void addWin(UUID uuid) { addWin(uuid.toString(), 1); }

    public static void addGame(String uuid, int i) {
        if (dataBase == null) {
            Log.error("load関数を実行してください。");
            return;
        }

        gameData.putIfAbsent(uuid, 0);
        winData.putIfAbsent(uuid, 0);
        gameData.put(uuid, gameData.get(uuid) + i);

        String sql = "insert into " + table_name + " (uuid, game, win) "
                + "VALUES ('"+uuid+"', " + gameData.get(uuid) + ", " + winData.get(uuid) + ") "
                +"ON DUPLICATE KEY UPDATE "
                +"uuid='" + uuid + "', "
                +"game=" + gameData.get(uuid) + ", "
                +"win=" + winData.get(uuid) + ";";
        try(Connection con = dataBase.getDataSource().getConnection();
            PreparedStatement prestat = con.prepareStatement(sql)) {
            prestat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addGame(String uuid) { addGame(uuid, 1); }

    public static void addGame(UUID uuid) { addGame(uuid.toString(), 1); }

    /* 以下、addWinsとaddGames */

    /**
     * @deprecated
     * @param list
     */
    public static void addWins(List<String> list) {
        try {
            MySQLUtil.openStaticConnection();
            String sql = "SELECT * FROM " + table_name + ";";
            PreparedStatement statement = MySQLUtil.getConnection().prepareStatement(sql);
            ResultSet result = statement.executeQuery();

            while(result.next()) {
                String uuidStr = result.getString(1);
                if (list.contains(uuidStr)) {
                    sql = "UPDATE " + table_name + " SET game=?, win=? WHERE uuid=?;";
                    gameData.putIfAbsent(uuidStr, 0);
                    winData.putIfAbsent(uuidStr, 0);
                    winData.put(uuidStr, (Integer)winData.get(uuidStr) + 1);
                    statement = MySQLUtil.getConnection().prepareStatement(sql);
                    statement.setInt(1, (Integer)gameData.get(uuidStr));
                    statement.setInt(2, (Integer)winData.get(uuidStr));
                    statement.setString(3, uuidStr);
                    list.remove(uuidStr);
                }
            }

            sql = "INSERT INTO " + table_name + " (uuid, game, win) VALUES (?, ?, ?);";
            PreparedStatement preparedStatement = MySQLUtil.getConnection().prepareStatement(sql);
            Iterator var6 = list.iterator();

            while(var6.hasNext()) {
                String uuid = (String)var6.next();
                preparedStatement.setString(1, uuid);
                preparedStatement.setInt(2, (Integer)gameData.get(uuid));
                preparedStatement.setInt(3, (Integer)winData.get(uuid));
                preparedStatement.executeUpdate();
            }
        } catch (SQLException var8) {
            var8.printStackTrace();
        }

    }

    /**
     * @deprecated
     * @param list
     */
    public static void addGames(List<String> list) {
        try {
            MySQLUtil.openStaticConnection();
            String sql = "SELECT * FROM " + table_name + ";";
            PreparedStatement statement = MySQLUtil.getConnection().prepareStatement(sql);
            ResultSet result = statement.executeQuery();

            while(result.next()) {
                String uuidStr = result.getString(1);
                if (list.contains(uuidStr)) {
                    sql = "UPDATE " + table_name + " SET game=?, win=? WHERE uuid=?;";
                    gameData.putIfAbsent(uuidStr, 0);
                    winData.putIfAbsent(uuidStr, 0);
                    gameData.put(uuidStr, (Integer)gameData.get(uuidStr) + 1);
                    statement = MySQLUtil.getConnection().prepareStatement(sql);
                    statement.setInt(1, (Integer)gameData.get(uuidStr));
                    statement.setInt(2, (Integer)winData.get(uuidStr));
                    statement.setString(3, uuidStr);
                    list.remove(uuidStr);
                }
            }

            sql = "INSERT INTO " + table_name + " (uuid, game, win) VALUES (?, ?, ?);";
            PreparedStatement preparedStatement = MySQLUtil.getConnection().prepareStatement(sql);
            Iterator var6 = list.iterator();

            while(var6.hasNext()) {
                String uuid = (String)var6.next();
                gameData.putIfAbsent(uuid, 0);
                winData.putIfAbsent(uuid, 0);
                gameData.put(uuid, gameData.get(uuid) + 1);
                preparedStatement.setString(1, uuid);
                preparedStatement.setInt(2, gameData.get(uuid));
                preparedStatement.setInt(3, winData.get(uuid));
                preparedStatement.executeUpdate();
            }
        } catch (SQLException var8) {
            var8.printStackTrace();
        }

    }

    /**
     * HighFunctionalityLib 必須
     * @param table
     * @param plugin
     */
    public static void load(String table, Plugin plugin) {
        table_name = table;
        if (winData == null) winData = new HashMap();
        if (gameData == null) gameData = new HashMap();

        Plugin highFLib = Bukkit.getPluginManager().getPlugin("HighFunctionalityLib");

        if (dataBase == null) {
            dataBase = new DataBase(plugin, new MySQL(
                    MySQL.getDataBaseFromHFL(highFLib),
                    MySQL.getPortFromHFL(highFLib),
                    MySQL.getDataBaseFromHFL(highFLib),
                    MySQL.getUsernameFromHFL(highFLib),
                    MySQL.getPasswordFromHFL(highFLib)
            ));
        }

        String sql = "SELECT * FROM " + table_name + ";";

        String uuid;
        int game, win;

        try(Connection con = dataBase.getDataSource().getConnection();
            PreparedStatement prestat = con.prepareStatement(sql)) {
            ResultSet result = prestat.executeQuery();
            while (result.next()) {
                uuid = result.getString(1);
                game = result.getInt(2);
                win = result.getInt(3);
                winData.put(uuid, win);
                gameData.put(uuid, game);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String, Integer> getWinData() {
        return winData;
    }

    public static HashMap<String, Integer> getGameData() {
        return gameData;
    }

}
