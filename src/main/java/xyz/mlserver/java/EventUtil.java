package xyz.mlserver.java;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import xyz.mlserver.java.sql.DataBase;
import xyz.mlserver.java.sql.mysql.MySQL;
import xyz.mlserver.mls.MLSEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class EventUtil {

    private static String table_name;
    private static HashMap<String, Integer> winData;
    private static HashMap<String, Integer> gameData;
    private static DataBase dataBase;

    private static Table<MLSEvent, String, Integer> winDataTable;
    private static Table<MLSEvent, String, Integer> gameDataTable;
    private static Table<MLSEvent, String, Date> winDataTableLastUpdate;
    private static Table<MLSEvent, String, Date> gameDataTableLastUpdate;

    public static int getWin(DataBase dataBase, MLSEvent event, String uuid) {
        if (winDataTableLastUpdate == null) winDataTableLastUpdate = HashBasedTable.create();;
        if (winDataTableLastUpdate.get(event, uuid) != null && winDataTable.get(event, uuid) != null) {
            Date lastUpdate = winDataTableLastUpdate.get(event, uuid);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.MINUTE, -1);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(lastUpdate);
            if(calendar1.after(calendar)){ // 前回のロードが現在から5分前よりも未来なら
                return winDataTable.get(event, uuid);
            }
        }

        Date now = new Date();

        String sql = "select * from " + event.getDatabase() + " where uuid=?";
        try(Connection con = dataBase.getDataSource().getConnection();
            PreparedStatement prestat = con.prepareStatement(sql)) {
            prestat.setString(1, uuid);
            ResultSet result = prestat.executeQuery();
            if (result.next()) {
                int num = result.getInt(3);
                winDataTable.put(event, uuid, num);
                winDataTableLastUpdate.put(event, uuid, now);
                return num;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        winDataTableLastUpdate.put(event, uuid, now);
        winDataTable.put(event, uuid, 0);
        return 0;
    }

    public static int getWin(DataBase dataBase, MLSEvent event, UUID uuid) {
        return getWin(dataBase, event, uuid.toString());
    }

    /**
     * @deprecated {@link EventAPI}
     */
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

    /**
     * @deprecated {@link EventAPI}
     */
    public static void addWin(String table_name, String uuid, int i) {
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

    /**
     * @deprecated {@link EventAPI}
     */
    public static void addWin(DataBase dataBase, String table_name, String uuid, int i) {
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

    /**
     * @deprecated {@link EventAPI}
     */
    public static void addWin(String uuid) { addWin(uuid, 1); }

    /**
     * @deprecated {@link EventAPI}
     */
    public static void addWin(UUID uuid) { addWin(uuid.toString(), 1); }

    /**
     * @deprecated {@link EventAPI}
     */
    public static void addWin(String table_name, String uuid) { addWin(table_name, uuid, 1); }

    /**
     * @deprecated {@link EventAPI}
     */
    public static void addWin(String table_name, UUID uuid) { addWin(table_name, uuid.toString(), 1); }

    /**
     * @deprecated {@link EventAPI}
     */
    public static void addWin(DataBase dataBase, String table_name, String uuid) { addWin(dataBase, table_name, uuid, 1); }

    /**
     * @deprecated {@link EventAPI}
     */
    public static void addWin(DataBase dataBase, String table_name, UUID uuid) { addWin(dataBase, table_name, uuid.toString(), 1); }

    /**
     * @deprecated {@link EventAPI}
     */
    public static void addWin(MLSEvent event, String uuid) { addWin(event.getDatabase(), uuid, 1); }

    /**
     * @deprecated {@link EventAPI}
     */
    public static void addWin(MLSEvent event, UUID uuid) { addWin(event.getDatabase(), uuid.toString(), 1); }

    /**
     * @deprecated {@link EventAPI}
     */
    public static void addWin(DataBase dataBase, MLSEvent event, String uuid) { addWin(dataBase, event.getDatabase(), uuid, 1); }

    /**
     * @deprecated {@link EventAPI}
     */
    public static void addWin(DataBase dataBase, MLSEvent event, UUID uuid) { addWin(dataBase, event.getDatabase(), uuid.toString(), 1); }


    public static int getGame(DataBase dataBase, MLSEvent event, String uuid) {
        if (gameDataTableLastUpdate == null) gameDataTableLastUpdate = HashBasedTable.create();;
        if (gameDataTableLastUpdate.get(event, uuid) != null && gameDataTable.get(event, uuid) != null) {
            Date lastUpdate = gameDataTableLastUpdate.get(event, uuid);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.MINUTE, -1);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(lastUpdate);
            if(calendar1.after(calendar)){ // 前回のロードが現在から5分前よりも未来なら
                return gameDataTable.get(event, uuid);
            }
        }

        Date now = new Date();

        String sql = "select * from " + event.getDatabase() + " where uuid=?";
        try(Connection con = dataBase.getDataSource().getConnection();
            PreparedStatement prestat = con.prepareStatement(sql)) {
            prestat.setString(1, uuid);
            ResultSet result = prestat.executeQuery();
            if (result.next()) {
                int num = result.getInt(2);
                gameDataTable.put(event, uuid, num);
                gameDataTableLastUpdate.put(event, uuid, now);
                return num;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        gameDataTableLastUpdate.put(event, uuid, now);
        gameDataTable.put(event, uuid, 0);
        return 0;
    }

    public static int getGame(DataBase dataBase, MLSEvent event, UUID uuid) {
        return getGame(dataBase, event, uuid.toString());
    }

    /**
     * @deprecated {@link EventAPI}
     */
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

    /**
     * @deprecated {@link EventAPI}
     */
    public static void addGame(String table_name, String uuid, int i) {
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

    /**
     * @deprecated {@link EventAPI}
     */
    public static void addGame(DataBase dataBase, String table_name, String uuid, int i) {
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

    /**
     * @deprecated {@link EventAPI}
     */
    public static void addGame(String uuid) { addGame(uuid, 1); }

    /**
     * @deprecated {@link EventAPI}
     */
    public static void addGame(UUID uuid) { addGame(uuid.toString(), 1); }

    /**
     * @deprecated {@link EventAPI}
     */
    public static void addGame(String table_name, String uuid) { addGame(table_name, uuid, 1); }

    /**
     * @deprecated {@link EventAPI}
     */
    public static void addGame(String table_name, UUID uuid) { addGame(table_name, uuid.toString(), 1); }

    /**
     * @deprecated {@link EventAPI}
     */
    public static void addGame(DataBase dataBase, String table_name, String uuid) { addGame(dataBase, table_name, uuid, 1); }

    /**
     * @deprecated {@link EventAPI}
     */
    public static void addGame(DataBase dataBase, String table_name, UUID uuid) { addGame(dataBase, table_name, uuid.toString(), 1); }

    /**
     * @deprecated {@link EventAPI}
     */
    public static void addGame(MLSEvent event, String uuid) { addGame(event.getDatabase(), uuid, 1); }

    /**
     * @deprecated {@link EventAPI}
     */
    public static void addGame(MLSEvent event, UUID uuid) { addGame(event.getDatabase(), uuid.toString(), 1); }

    /**
     * @deprecated {@link EventAPI}
     */
    public static void addGame(DataBase dataBase, MLSEvent event, String uuid) { addGame(dataBase, event.getDatabase(), uuid, 1); }

    /**
     * @deprecated {@link EventAPI}
     */
    public static void addGame(DataBase dataBase, MLSEvent event, UUID uuid) { addGame(dataBase, event.getDatabase(), uuid.toString(), 1); }

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
     * @deprecated
     * @param table
     * @param plugin
     */
    public static void load(String table, Plugin plugin) {
        table_name = table;
        if (winData == null) winData = new HashMap();
        if (gameData == null) gameData = new HashMap();

        Plugin highFLib = Bukkit.getPluginManager().getPlugin("HighFunctionalityLib");
        if (highFLib == null) {
            Log.error("はいふぁんくしょなりてぃりぶぅがないよ～");
            return;
        }
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

    /**
     * HighFunctionalityLib 必須
     * @param event
     * @param plugin
     */
    public static void load(MLSEvent event, Plugin plugin) {
        load(event.getDatabase(), plugin);
    }

    public static HashMap<String, Integer> getWinData() {
        return winData;
    }

    public static HashMap<String, Integer> getGameData() {
        return gameData;
    }

}
