package xyz.mlserver.java.lang;

import org.bukkit.entity.Player;
import xyz.mlserver.java.sql.DataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class LanguageSQL {

    private final DataBase dataBase;

    /**
     * 言語システムのホニャララ
     * @param dataBase 言語データを格納するデータベース
     */
    LanguageSQL(DataBase dataBase, int interval) {
        this.dataBase = dataBase;
    }

    public DataBase getDataBase() {
        return dataBase;
    }

    /**
     * Playerを元に設定言語を取得する
     * @param player 取得元の{@Player}
     * @return {@Language}
     */
    public Language getLanguage(Player player) { return getLanguage(player.getUniqueId()); }

    public Language getLanguage(UUID uuid) { return getLanguage(uuid.toString()); }

    public Language getLanguage(String uuid) {
        createTable();
        String sql = "SELECT * FROM languages where uuid=?;";
        Language language;
        try(Connection con = dataBase.getDataSource().getConnection();
            PreparedStatement prestat = con.prepareStatement(sql)) {
            prestat.setString(1, uuid);
            ResultSet result = prestat.executeQuery();
            if (result.next()) language = Language.fromId(result.getString(2));
            else {
                setLanguage(uuid, Language.ENGLISH);
                language = Language.ENGLISH;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            language = Language.ENGLISH;
        }
        return language;
    }

    public void setLanguage(String uuid, Language language) {
        createTable();
        String sql = "insert into languages (uuid, lang) "
                + "VALUES (?, ?) "
                +"ON DUPLICATE KEY UPDATE "
                +"uuid=?, "
                +"lang=?;";
        try(Connection con = dataBase.getDataSource().getConnection();
            PreparedStatement prestat = con.prepareStatement(sql)) {
            prestat.setString(1, uuid);
            prestat.setString(2, language.getId());
            prestat.setString(3, uuid);
            prestat.setString(4, language.getId());
            prestat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable() {
        String sql = "create table if not exists languages (" +
                "uuid text NOT NULL PRIMARY KEY," +
                "lang text default '" + Language.JAPANESE.getId() + "' not null" +
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
