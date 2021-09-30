package xyz.mlserver.mls.event;

import xyz.mlserver.java.sql.DataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class EventSQL {

    private final DataBase dataBase;
    private final String table_name;
    private HashMap<String, Integer> winData;
    private HashMap<String, Integer> gameData;

    public EventSQL(String table_name, DataBase dataBase) {
        this.table_name = table_name;
        this.dataBase = dataBase;
        this.winData = new HashMap<>();
        this.gameData = new HashMap<>();
    }

    public int getWin(String uuid) {
        winData.putIfAbsent(uuid, 0);
        return winData.get(uuid);
    }

    public int getWin(UUID uuid) {
        return getWin(uuid.toString());
    }

    public void addWin(String uuid, int i) {
        gameData.putIfAbsent(uuid, 0);
        winData.putIfAbsent(uuid, 0);
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


    public void addWin(String table_name, String uuid, int i) {
        gameData.putIfAbsent(uuid, 0);
        winData.putIfAbsent(uuid, 0);
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
        gameData.putIfAbsent(uuid, 0);
        return gameData.get(uuid);
    }

    public int getGame(UUID uuid) {
        return getGame(uuid.toString());
    }

    public void addGame(String uuid, int i) {
        gameData.putIfAbsent(uuid, 0);
        winData.putIfAbsent(uuid, 0);
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

}
