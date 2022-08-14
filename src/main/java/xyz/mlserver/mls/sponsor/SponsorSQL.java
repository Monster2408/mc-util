package xyz.mlserver.mls.sponsor;

import org.bukkit.entity.Player;
import xyz.mlserver.java.sql.DataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SponsorSQL {

    private final DataBase dataBase;

    public SponsorSQL(DataBase dataBase) {
        this.dataBase = dataBase;
    }

    /**
     *
     * @param uuid
     * @param is_sponsor
     */
    public void setSponsor(String uuid, boolean is_sponsor) {
        createTable();

        int bool = 0;
        if (is_sponsor) bool = 1;

        String sql = "insert into sponsor (uuid, is_sponsor) "
                + "VALUES (?, ?) "
                +"ON DUPLICATE KEY UPDATE "
                +"uuid=?, "
                +"is_sponsor=?;";
        try(Connection con = dataBase.getDataSource().getConnection();
            PreparedStatement prestat = con.prepareStatement(sql)) {
            prestat.setString(1, uuid);
            prestat.setInt(2, bool);
            prestat.setString(3, uuid);
            prestat.setInt(4, bool);
            prestat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param uuid
     * @param is_sponsor
     */
    public void setSponsor(UUID uuid, boolean is_sponsor) { setSponsor(uuid.toString(), is_sponsor); }

    /**
     *
     * @param player
     * @param is_sponsor
     */
    public void setSponsor(Player player, boolean is_sponsor) { setSponsor(player.getUniqueId(), is_sponsor); }

    /**
     *
     * @param uuid
     * @return
     */
    public boolean isSponsor(String uuid) {
        createTable();

        String sql = "SELECT * FROM sponsor where uuid=?;";
        try(Connection con = dataBase.getDataSource().getConnection();
            PreparedStatement prestat = con.prepareStatement(sql)) {
            prestat.setString(1, uuid);
            ResultSet result = prestat.executeQuery();
            if (result.next()) {
                result.beforeFirst();
                result.next();
                int is_sponsor = result.getInt(2);
                return is_sponsor == 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     *
     * @param uuid
     * @return
     */
    public boolean isSponsor(UUID uuid) { return isSponsor(uuid.toString()); }

    /**
     *
     * @param player
     * @return
     */
    public boolean isSponsor(Player player) { return isSponsor(player.getUniqueId()); }

    private void createTable() {
        String sql = "create table if not exists sponsor (" +
                "uuid varchar(36) NOT NULL PRIMARY KEY," +
                "is_sponsor int(1) default '0'" +
                ");";
        try(Connection con = dataBase.getDataSource().getConnection();
            PreparedStatement prestat = con.prepareStatement(sql)) {
            prestat.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}