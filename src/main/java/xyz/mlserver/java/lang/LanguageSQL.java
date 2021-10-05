package xyz.mlserver.java.lang;

import org.bukkit.entity.Player;
import xyz.mlserver.java.sql.DataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class LanguageSQL {

    public static void putLang(DataBase dataBase, Player player, Language lang) {
        putLang(dataBase, player.getUniqueId().toString(), lang);
    }

    public static void putLang(DataBase dataBase, UUID uuid, Language lang) {
        putLang(dataBase, uuid.toString(), lang);
    }

    public static void putLang(DataBase dataBase, String uuid, Language lang) {
        LanguageUtil.getPlayerLang().put(uuid, lang);

        String sql = "insert into languages (uuid, lang) "
                + "VALUES (?, ?) "
                +"ON DUPLICATE KEY UPDATE "
                +"uuid=?, "
                +"lang=?;";
        try(Connection con = dataBase.getDataSource().getConnection();
            PreparedStatement prestat = con.prepareStatement(sql)) {
            prestat.setString(1, uuid);
            prestat.setString(2, lang.getId());
            prestat.setString(3, uuid);
            prestat.setString(4, lang.getId());
            prestat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void load(DataBase dataBase, UUID uuid) {
        load(dataBase, uuid.toString());
    }

    public static void load(DataBase dataBase, Player player) {
        load(dataBase, player.getUniqueId().toString());
    }

    public static void load(DataBase dataBase, String uuid) {
        String sql = "SELECT * FROM languages where uuid=?;";
        try(Connection con = dataBase.getDataSource().getConnection();
            PreparedStatement prestat = con.prepareStatement(sql)) {
            prestat.setString(1, uuid);
            ResultSet result = prestat.executeQuery();
            if (result.next()) LanguageUtil.getPlayerLang().put(uuid, Language.fromId(result.getString(2)));
            else LanguageUtil.getPlayerLang().put(uuid, Language.ENGLISH);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
