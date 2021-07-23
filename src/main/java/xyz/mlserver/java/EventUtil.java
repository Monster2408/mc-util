package xyz.mlserver.java;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class EventUtil {

    private static String table_name;
    private static HashMap<String, Integer> winData;
    private static HashMap<String, Integer> gameData;

    public static void addWin(String uuid) {
        try {
            MySQLUtil.openStaticConnection();
            String sql = "SELECT * FROM " + table_name + ";";
            PreparedStatement statement = MySQLUtil.getConnection().prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            gameData.putIfAbsent(uuid, 0);
            winData.putIfAbsent(uuid, 0);
            winData.put(uuid, (Integer)winData.get(uuid) + 1);

            while(result.next()) {
                String uuidStr = result.getString(1);
                if (uuidStr.equalsIgnoreCase(uuid)) {
                    sql = "UPDATE " + table_name + " SET game=?, win=? WHERE uuid=?;";
                    statement = MySQLUtil.getConnection().prepareStatement(sql);
                    statement.setInt(1, (Integer)gameData.get(uuid));
                    statement.setInt(2, (Integer)winData.get(uuid));
                    statement.setString(3, uuid);
                    statement.executeUpdate();
                    return;
                }
            }

            sql = "INSERT INTO " + table_name + " (uuid, game, win) VALUES (?, ?, ?);";
            PreparedStatement preparedStatement = MySQLUtil.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, uuid);
            preparedStatement.setInt(2, (Integer)gameData.get(uuid));
            preparedStatement.setInt(3, (Integer)winData.get(uuid));
            preparedStatement.executeUpdate();
        } catch (SQLException var6) {
            var6.printStackTrace();
        }

    }

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

    public static void addGame(String uuid) {
        try {
            MySQLUtil.openStaticConnection();
            String sql = "SELECT * FROM " + table_name + ";";
            PreparedStatement statement = MySQLUtil.getConnection().prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            gameData.putIfAbsent(uuid, 0);
            winData.putIfAbsent(uuid, 0);
            gameData.put(uuid, (Integer)gameData.get(uuid) + 1);

            while(result.next()) {
                String uuidStr = result.getString(1);
                if (uuidStr.equalsIgnoreCase(uuid)) {
                    sql = "UPDATE " + table_name + " SET game=?, win=? WHERE uuid=?;";
                    statement = MySQLUtil.getConnection().prepareStatement(sql);
                    statement.setInt(1, (Integer)gameData.get(uuid));
                    statement.setInt(2, (Integer)winData.get(uuid));
                    statement.setString(3, uuid);
                    statement.executeUpdate();
                    return;
                }
            }

            sql = "INSERT INTO " + table_name + " (uuid, game, win) VALUES (?, ?, ?);";
            PreparedStatement preparedStatement = MySQLUtil.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, uuid);
            preparedStatement.setInt(2, (Integer)gameData.get(uuid));
            preparedStatement.setInt(3, (Integer)winData.get(uuid));
            preparedStatement.executeUpdate();
        } catch (SQLException var6) {
            var6.printStackTrace();
        }

    }

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
                gameData.put(uuid, (Integer)gameData.get(uuid) + 1);
                preparedStatement.setString(1, uuid);
                preparedStatement.setInt(2, (Integer)gameData.get(uuid));
                preparedStatement.setInt(3, (Integer)winData.get(uuid));
                preparedStatement.executeUpdate();
            }
        } catch (SQLException var8) {
            var8.printStackTrace();
        }

    }

    public static void load(String table) {
        table_name = table;
        winData = new HashMap();
        gameData = new HashMap();

        try {
            MySQLUtil.openStaticConnection();
            String sql = "SELECT * FROM " + table_name + ";";
            PreparedStatement statement = MySQLUtil.getConnection().prepareStatement(sql);
            ResultSet result = statement.executeQuery();

            while(result.next()) {
                String uuid = result.getString(1);
                Integer game = result.getInt(2);
                Integer win = result.getInt(3);
                winData.put(uuid, win);
                gameData.put(uuid, game);
            }
        } catch (SQLException var7) {
            var7.printStackTrace();
        }

    }

    public static HashMap<String, Integer> getWinData() {
        return winData;
    }

    public static HashMap<String, Integer> getGameData() {
        return gameData;
    }

}
