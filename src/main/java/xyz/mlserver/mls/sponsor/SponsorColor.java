package xyz.mlserver.mls.sponsor;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import xyz.mlserver.java.Log;
import xyz.mlserver.java.sql.DataBase;
import xyz.mlserver.mc.util.Color;
import xyz.mlserver.mls.listener.ChangeSponsorColor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SponsorColor {

    private final UUID uuid;
    private ChatColor color;

    public SponsorColor(UUID uuid) {
        this.uuid  = uuid;
        this.color = null;
    }

    public void setColor(ChatColor color) {
        this.color = color;
        Bukkit.getPluginManager().callEvent(new ChangeSponsorColor(this.uuid, this.color));
    }

    public UUID getUUID() {
        return uuid;
    }

    public ChatColor getColor() {
        return color;
    }

    public void save(DataBase dataBase) {
        String sql = "insert into sponsor_color (uuid, color) "
                + "VALUES (?, ?) "
                + "ON DUPLICATE KEY UPDATE "
                + "uuid=?, "
                + "color=?;";
        try(Connection con = dataBase.getDataSource().getConnection();
            PreparedStatement prestat = con.prepareStatement(sql)) {
            prestat.setString(1, uuid.toString());
            prestat.setString(2, color.toString());
            prestat.setString(3, uuid.toString());
            prestat.setString(4, color.toString());
            prestat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ChatColor load(DataBase dataBase, String uuid, ChatColor color) {
        String sql = "select * from sponsor_color where uuid=?;";
        try(Connection con = dataBase.getDataSource().getConnection();
            PreparedStatement prestat = con.prepareStatement(sql)) {
            prestat.setString(1, uuid);
            ResultSet rs = prestat.executeQuery();
            if(rs.next()) {
                try {
                    Log.debug(rs.getString(2));
                    color = ChatColor.valueOf(rs.getString(2));
                } catch (IllegalArgumentException e) {
                    Log.error("Illegal ChatColor: " + rs.getString(2));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return color;
    }

    public static ChatColor load(DataBase dataBase, UUID uuid, ChatColor defaultColor) { return load(dataBase, uuid.toString(), defaultColor); }

    public static ChatColor load(DataBase dataBase, String uuid) { return load(dataBase, uuid, null); }

    public static ChatColor load(DataBase dataBase, UUID uuid) { return load(dataBase, uuid.toString(), null); }

}
