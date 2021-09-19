package xyz.mlserver.java.sql;

import com.zaxxer.hikari.HikariDataSource;
import xyz.mlserver.java.sql.db.DataBaseUtil;
import xyz.mlserver.java.sql.mysql.MySQL;

import org.bukkit.plugin.Plugin;
import xyz.mlserver.java.sql.sqlite.SQLite;

import javax.sql.rowset.RowSetFactory;

public class DataBase {

    private final DataBaseUtil dataBaseUtil;

    private MySQL mysql = null;
    private SQLite sqlite = null;

    private HikariDataSource dataSource;
    private RowSetFactory factory;

    public DataBase(Plugin plugin, MySQL mysql) {
        this.dataBaseUtil = new DataBaseUtil(plugin);
        this.mysql = mysql;
    }

    public DataBase(Plugin plugin, SQLite sqlite) {
        this.dataBaseUtil = new DataBaseUtil(plugin);
        this.sqlite = sqlite;
    }

}
