package xyz.mlserver.mls.sponsor;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import xyz.mlserver.java.Log;
import xyz.mlserver.java.sql.DataBase;
import xyz.mlserver.mls.listener.ChangeSponsorColor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SponsorColor {

    public static void set(DataBase dataBase, String uuid, ChatColor color) {
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

    public static void set(DataBase dataBase, UUID uuid, ChatColor color) { set(dataBase, uuid.toString(), color);}
    public static void set(DataBase dataBase, Player player, ChatColor color) { set(dataBase, player.getUniqueId().toString(), color);}

    public static ChatColor load(DataBase dataBase, String uuid, ChatColor defaultColor) {
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
                    color = ChatColor.valueOf(rs.getString("color"));
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

    public static ChatColor load(DataBase dataBase, UUID uuid, ChatColor defaultColor) { return load(dataBase, uuid.toString(), defaultColor); }

    public static ChatColor load(DataBase dataBase, String uuid) { return load(dataBase, uuid, null); }

    public static ChatColor load(DataBase dataBase, UUID uuid) { return load(dataBase, uuid.toString(), null); }

}
