package xyz.mlserver.mls.sponsor;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import xyz.mlserver.java.sql.DataBase;
import xyz.mlserver.mc.util.Color;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SponsorColor {

    private final DataBase dataBase;

    public SponsorColor(DataBase dataBase) {
        this.dataBase = dataBase;
    }

    public void set(String uuid, ChatColor color) {
        createTable();

        String sql = "insert into sponsor_color (uuid, color) "
                + "VALUES (?, ?) "
                + "ON DUPLICATE KEY UPDATE "
                + "uuid=?, "
                + "color=?;";
        try(Connection con = dataBase.getDataSource().getConnection();
            PreparedStatement prestat = con.prepareStatement(sql)) {
            prestat.setString(1, uuid);
            prestat.setString(2, color.toString());
            prestat.setString(3, uuid);
            prestat.setString(4, color.toString());
            prestat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void set(UUID uuid, ChatColor color) { set(uuid.toString(), color);}
    public void set(Player player, ChatColor color) { set(player.getUniqueId().toString(), color);}

    public ChatColor load(String uuid, ChatColor defaultColor) {
        createTable();

        String sql = "select * from sponsor_color where uuid=?;";
        ChatColor color;
        try(Connection con = dataBase.getDataSource().getConnection();
            PreparedStatement prestat = con.prepareStatement(sql)) {
            prestat.setString(1, uuid);
            ResultSet rs = prestat.executeQuery();
            try {
                if(rs.next()) {
                    rs.beforeFirst();
                    rs.next();
                    // Log.debug(rs.getString("color"));
                    color = Color.getNameToColor(rs.getString("color"));
                } else {
                    color = defaultColor;
                }
            } catch (Exception e) {
                color = defaultColor;
            }
        } catch (SQLException e) {
            color = defaultColor;
        }
        return color;
    }

    public ChatColor load(UUID uuid, ChatColor defaultColor) { return load(uuid.toString(), defaultColor); }
    public ChatColor load(String uuid) { return load(uuid, null); }
    public ChatColor load(UUID uuid) { return load(uuid.toString(), null); }

    public ChatColor get(String uuid, ChatColor defaultColor) {
        createTable();

        String sql = "select * from sponsor_color where uuid=?;";
        ChatColor color;
        try(Connection con = dataBase.getDataSource().getConnection();
            PreparedStatement prestat = con.prepareStatement(sql)) {
            prestat.setString(1, uuid);
            ResultSet rs = prestat.executeQuery();
            try {
                if(rs.next()) {
                    rs.beforeFirst();
                    rs.next();
                    color = Color.getNameToColor(rs.getString("color"));
                } else {
                    color = defaultColor;
                }
            } catch (Exception e) {
                color = defaultColor;
            }
        } catch (SQLException e) {
            color = defaultColor;
        }
        return color;
    }

    public ChatColor get(UUID uuid, ChatColor defaultColor) { return get(uuid.toString(), defaultColor); }
    public ChatColor get(String uuid) { return get(uuid, null); }
    public ChatColor get(UUID uuid) { return get(uuid.toString(), null); }

    private void createTable() {
        String sql = "create table if not exists sponsor (" +
                "uuid varchar(36) NOT NULL PRIMARY KEY," +
                "color varchar(30)" +
                ");";
        try(Connection con = dataBase.getDataSource().getConnection();
            PreparedStatement prestat = con.prepareStatement(sql)) {
            prestat.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
