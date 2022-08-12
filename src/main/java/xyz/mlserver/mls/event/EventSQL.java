package xyz.mlserver.mls.event;

import org.bukkit.entity.Player;
import xyz.mlserver.java.lang.Language;
import xyz.mlserver.java.sql.DataBase;
import xyz.mlserver.mls.MLSEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class EventSQL {

    private final DataBase dataBase;
    private final String table_name;
    private final HashMap<String, Integer> winData;
    private final HashMap<String, Integer> gameData;

    public EventSQL(String table_name, DataBase dataBase) {
        this.table_name = table_name;
        this.dataBase = dataBase;
        this.winData = new HashMap<>();
        this.gameData = new HashMap<>();
    }

    public EventSQL(MLSEvent event, DataBase dataBase) { this(event.getDatabase(), dataBase); }

    public int getWin(String uuid) {
        if (table_name == null) return -1;

        createTable();
        if (winData.get(uuid) != null) return winData.get(uuid);
        String sql = "select * from " + table_name + " where uuid=?";
        try(Connection con = dataBase.getDataSource().getConnection();
            PreparedStatement prestat = con.prepareStatement(sql)) {
            prestat.setString(1, uuid);
            ResultSet result = prestat.executeQuery();
            if (result.next()) {
                int num = result.getInt(3);
                winData.put(uuid, num);
            } else {
                winData.putIfAbsent(uuid, 0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            winData.putIfAbsent(uuid, 0);
        }
        return winData.get(uuid);
    }

    public int getWin(UUID uuid) {
        return getWin(uuid.toString());
    }
    public int getWin(Player player) {
        return getWin(player.getUniqueId());
    }

    public void addWin(String uuid, int i) {
        if (table_name == null) return ;

        getGame(uuid);
        getWin(uuid);
        winData.put(uuid, winData.get(uuid) + i);

        String sql = "insert into " + table_name + " (uuid, game, win) "
                + "VALUES (?, ?, ?) "
                +"ON DUPLICATE KEY UPDATE "
                +"uuid=?, "
                +"game=?, "
                +"win=?;";
        try(Connection con = dataBase.getDataSource().getConnection();
            PreparedStatement prestat = con.prepareStatement(sql)) {
            prestat.setString(1, uuid);
            prestat.setInt(2, gameData.get(uuid));
            prestat.setInt(3, winData.get(uuid));
            prestat.setString(4, uuid);
            prestat.setInt(5, gameData.get(uuid));
            prestat.setInt(6, winData.get(uuid));
            prestat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addWin(String uuid) { addWin(uuid, 1); }
    public void addWin(UUID uuid) { addWin(uuid.toString(), 1); }

    public int getGame(String uuid) {
        if (table_name == null) return -1;

        createTable();
        if (gameData.get(uuid) != null) return gameData.get(uuid);
        String sql = "select * from " + table_name + " where uuid=?";
        try(Connection con = dataBase.getDataSource().getConnection();
            PreparedStatement prestat = con.prepareStatement(sql)) {
            prestat.setString(1, uuid);
            ResultSet result = prestat.executeQuery();
            if (result.next()) {
                int num = result.getInt(2);
                gameData.put(uuid, num);
            } else {
                gameData.putIfAbsent(uuid, 0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            gameData.putIfAbsent(uuid, 0);
        }
        return gameData.get(uuid);
    }

    public int getGame(UUID uuid) {
        return getGame(uuid.toString());
    }

    public void addGame(String uuid, int i) {
        if (table_name == null) return ;

        getGame(uuid);
        getWin(uuid);
        gameData.put(uuid, gameData.get(uuid) + i);

        String sql = "insert into " + table_name + " (uuid, game, win) "
                + "VALUES (?, ?, ?) "
                +"ON DUPLICATE KEY UPDATE "
                +"uuid=?, "
                +"game=?, "
                +"win=?;";
        try(Connection con = dataBase.getDataSource().getConnection();
            PreparedStatement prestat = con.prepareStatement(sql)) {
            prestat.setString(1, uuid);
            prestat.setInt(2, gameData.get(uuid));
            prestat.setInt(3, winData.get(uuid));
            prestat.setString(4, uuid);
            prestat.setInt(5, gameData.get(uuid));
            prestat.setInt(6, winData.get(uuid));
            prestat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addGame(String uuid) { addGame(uuid, 1); }
    public void addGame(UUID uuid) { addGame(uuid.toString(), 1); }

    public HashMap<String, Integer> getWinData() {
        return winData;
    }

    public HashMap<String, Integer> getGameData() {
        return gameData;
    }

    private void createTable() {
        String sql = "create table if not exists " + table_name + " (" +
                "uuid text NOT NULL PRIMARY KEY," +
                "game int(1000) default '0' not null" +
                "win int(1000) default '0' not null" +
                ");"
                ;
        try(Connection con = dataBase.getDataSource().getConnection();
            PreparedStatement prestat = con.prepareStatement(sql)) {
            prestat.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getWin(MLSEvent event, String uuid) {
        createTable(event);

        int num;
        String sql = "select * from " + event.getDatabase() + " where uuid=?";
        try(Connection con = dataBase.getDataSource().getConnection();
            PreparedStatement prestat = con.prepareStatement(sql)) {
            prestat.setString(1, uuid);
            ResultSet result = prestat.executeQuery();
            if (result.next()) {
                num = result.getInt(3);
                winData.put(uuid, num);
            } else {
                num = 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            num = 0;
        }
        return num;
    }

    public int getWin(MLSEvent event, UUID uuid) {
        return getWin(event, uuid.toString());
    }
    public int getWin(MLSEvent event, Player player) {
        return getWin(event, player.getUniqueId());
    }

    public int getGame(MLSEvent event, String uuid) {
        createTable(event);

        int num;
        String sql = "select * from " + event.getDatabase() + " where uuid=?";
        try(Connection con = dataBase.getDataSource().getConnection();
            PreparedStatement prestat = con.prepareStatement(sql)) {
            prestat.setString(1, uuid);
            ResultSet result = prestat.executeQuery();
            if (result.next()) {
                num = result.getInt(3);
            } else {
                num = 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            num = 0;
        }
        return num;
    }

    public int getGame(MLSEvent event, UUID uuid) {
        return getGame(event, uuid.toString());
    }
    public int getGame(MLSEvent event, Player player) {
        return getGame(event, player.getUniqueId());
    }

    private void createTable(MLSEvent event) {
        String sql = "create table if not exists " + event.getDatabase() + " (" +
                "uuid text NOT NULL PRIMARY KEY," +
                "game int(1000) default '0' not null" +
                "win int(1000) default '0' not null" +
                ");"
                ;
        try(Connection con = dataBase.getDataSource().getConnection();
            PreparedStatement prestat = con.prepareStatement(sql)) {
            prestat.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
