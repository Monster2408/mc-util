package xyz.mlserver.java.sql;

import com.zaxxer.hikari.HikariDataSource;
import xyz.mlserver.java.sql.db.AdvanceDataBase;
import xyz.mlserver.java.sql.db.DataBaseUtil;
import xyz.mlserver.java.sql.mysql.MySQL;

import org.bukkit.plugin.Plugin;
import xyz.mlserver.java.sql.sqlite.SQLite;

import javax.sql.rowset.RowSetFactory;

public class DataBase {

    private final DataBaseUtil dataBaseUtil;

    private MySQL mysql = null;
    private SQLite sqlite = null;
    private AdvanceDataBase advanceDataBase = null;

    private HikariDataSource dataSource;
    private RowSetFactory factory;

    public DataBase(Plugin plugin, MySQL mysql) {
        this.dataBaseUtil = new DataBaseUtil(plugin);
        this.mysql = mysql;
        try {
            this.advanceDataBase = new AdvanceDataBase(this.mysql, null);
            this.dataSource = this.advanceDataBase.generateDataSource();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public DataBase(Plugin plugin, SQLite sqlite) {
        this.dataBaseUtil = new DataBaseUtil(plugin);
        this.sqlite = sqlite;
        try {
            this.advanceDataBase = new AdvanceDataBase(null, this.sqlite);
            this.dataSource = this.advanceDataBase.generateDataSource();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public MySQL getMySQL() {
        return mysql;
    }

    public SQLite getSQLite() {
        return sqlite;
    }

    public AdvanceDataBase getAdvanceDataBase() {
        return advanceDataBase;
    }

    public HikariDataSource getDataSource() {
        return dataSource;
    }

}
