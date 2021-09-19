package xyz.mlserver.java.sql.db;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataBaseUtil {
    private final Plugin plugin;

    public DataBaseUtil(Plugin plugin) {
        this.plugin = plugin;
    }

    public void debug(Object msg) {
        log("§cDebug: §7" + msg.toString());
        debugToFile(msg);
    }

    public void debugException(Exception exc) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exc.printStackTrace(pw);
        debug(sw.toString());
    }

    /**
     * Debug.
     *
     * @param ex the ex
     */
    public void debugSqlException(SQLException ex) {
        debug("§7An error has occurred with the database, the error code is: '" + ex.getErrorCode() + "'");
        debug("§7The state of the sql is: " + ex.getSQLState());
        debug("§7Error message: " + ex.getMessage());
        debugException(ex);
    }

    private void debugToFile(Object msg) {
        File debugFile = new File(plugin.getDataFolder(), "logs/latest.log");
        if (!debugFile.exists()) {
            System.out.print("Seems that a problem has occurred while creating the latest.log file in the startup.");
            try { debugFile.createNewFile(); }
            catch (IOException ex) {
                System.out.print("An error has occurred creating the 'latest.log' file again, check your server.");
                System.out.print("Error message" + ex.getMessage());
            }
        }
        try {
            FileUtils.writeStringToFile(debugFile, "[" + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "] " + ChatColor.stripColor(msg.toString()) + "\n", Charsets.UTF_8, true);
        }
        catch (IOException ex) {
            System.out.print("An error has occurred writing to 'latest.log' file.");
            System.out.print(ex.getMessage());
        }
    }

    public void log(String message, int severity) {
        switch (severity) {
            case 1:
                plugin.getLogger().warning(message);
                break;
            case 2:
                plugin.getLogger().severe("! " + message);
                break;
            case 0:
            default:
                plugin.getLogger().info(message);
                break;
        }
    }

    public void logToFile(String message) {
        try {
            File saveTo = new File(plugin.getDataFolder(), "debug.log");
            if (!saveTo.exists()) saveTo.createNewFile();

            FileWriter fw = new FileWriter(saveTo, true);
            PrintWriter pw = new PrintWriter(fw);
            pw.println(getDateTime() + " " + message);
            pw.flush();
            pw.close();
        }
        catch (IOException e) { e.printStackTrace(); }
    }

    public void log(String message) {
        log(message, 0);
    }

    public String getDateTime() {
        Format formatter = new SimpleDateFormat("[dd/MM/yyyy | HH:mm:ss]");
        return formatter.format(new Date());
    }

    public Plugin getPlugin() {
        return plugin;
    }
}