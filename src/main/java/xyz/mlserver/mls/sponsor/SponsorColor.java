package xyz.mlserver.mls.sponsor;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
    private Color color;

    public SponsorColor(UUID uuid) {
        this.uuid  = uuid;
        this.color = null;
    }

    public void setColor(Color color) {
        this.color = color;
        Bukkit.getPluginManager().callEvent(new ChangeSponsorColor(this.uuid, this.color));
    }

    public UUID getUUID() {
        return uuid;
    }

    public Color getColor() {
        return color;
    }

    public void save(DataBase dataBase) {
        String sql = "insert into sponsor_color (uuid, color) "
                + "VALUES ('"+uuid+"', " + color.toString() + ") "
                + "ON DUPLICATE KEY UPDATE "
                + "uuid='" + uuid + "', "
                + "color=" + color.toString() + ";";
        try(Connection con = dataBase.getDataSource().getConnection();
            PreparedStatement prestat = con.prepareStatement(sql)) {
            prestat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ChatColor load(String uuid, DataBase dataBase) {
        String sql = "select * from sponsor_color where uuid=?;";
        try(Connection con = dataBase.getDataSource().getConnection();
            PreparedStatement prestat = con.prepareStatement(sql)) {
            prestat.setString(1, uuid);
            ResultSet rs = prestat.executeQuery();
            if(rs.next()) {
                return Color.getNameToColor(rs.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ChatColor load(UUID uuid, DataBase dataBase) { return load(uuid.toString(), dataBase); }

    public static ChatColor load(DataBase dataBase, UUID uuid) { return load(uuid.toString(), dataBase); }

    public static ChatColor load(DataBase dataBase, String uuid) { return load(uuid, dataBase); }

}
