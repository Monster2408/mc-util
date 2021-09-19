package xyz.mlserver.mls.sponsor;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import org.bukkit.plugin.Plugin;
import xyz.mlserver.java.Log;
import xyz.mlserver.java.MySQLUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class SponsorSQL {

    public static MySQLUtil mySQLUtil;

    private static String host, database, username, password;
    private static int port;

    /**
     * HighFunctionalityLib必須
     * @return {1: "成功", 0: "HighFunctionalityLibがねえ...", -1: "失敗"}
     */
    public static int OpenConnection() {
        Plugin highFLib = Bukkit.getPluginManager().getPlugin("HighFunctionalityLib");
        if (highFLib == null) return -1;
        if (mySQLUtil == null) {
            if (host == null) host = highFLib.getConfig().getString("host", "localhost");
            if (port < 0) port = highFLib.getConfig().getInt("host", 3306);
            if (database == null) database = highFLib.getConfig().getString("database", "database");
            if (username == null) username = highFLib.getConfig().getString("username", "username");
            if (password == null) password = highFLib.getConfig().getString("password", "password");

            mySQLUtil = new MySQLUtil(
                    host,
                    port,
                    database,
                    username,
                    password
            );
        }
        try {
            mySQLUtil.openConnection();
            return 1;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     *
     * @param uuid UUID of Player
     * @param i    Add sponsor month
     *
     */
    public static void savePlayer(UUID uuid, int i) {
        int error = OpenConnection();
        if (error == -1) {
            Log.error("HighFunctionalityLibがねえ...");
        } else if (error == 0) {
            Log.error("MySQLエラー");
        } else {
            try {
                String sql = "SELECT * FROM sponsor;";
                PreparedStatement statement = MySQLUtil.getConnection().prepareStatement(sql);
                ResultSet result = statement.executeQuery();
                String uuidStr;

                while (result.next()) {
                    uuidStr = result.getString(1);
                    if (uuidStr.equalsIgnoreCase(uuid.toString())) {
                        sql = "UPDATE sponsor SET date=? WHERE uuid=?;";
                        statement = MySQLUtil.getConnection().prepareStatement(sql);

                        if (i <= 0) statement.setString(1, "null");
                        else {
                            if (result.getString(2) == null || result.getString(2).equalsIgnoreCase("null")) {
                                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = new Date();

                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(date);

                                calendar.add(Calendar.MONTH, i);

                                statement.setString(1, df.format(calendar.getTime()));

                                if (Bukkit.getPlayer(uuid) != null) Bukkit.getPlayer(uuid).sendMessage(ChatColor.AQUA + "あなたは本日から" + df.format(calendar.getTime()) + "までスポンサーです。");
                            } else {
                                try {
                                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                    df.setLenient(false);
                                    String s1 = result.getString(2);

                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(df.parse(s1));

                                    calendar.add(Calendar.MONTH, i);

                                    statement.setString(1, df.format(calendar.getTime()));

                                    if (Bukkit.getPlayer(uuid) != null) Bukkit.getPlayer(uuid).sendMessage(ChatColor.AQUA + "あなたは本日から" + df.format(calendar.getTime()) + "までスポンサーです。");

                                } catch (ParseException p) {
                                    p.printStackTrace();
                                    return;
                                }
                            }
                        }
                        statement.setString(2, uuid.toString());
                        statement.executeUpdate();
                        return;
                    }
                }

                sql = "INSERT INTO sponsor (uuid, date) VALUES (?, ?);";
                PreparedStatement preparedStatement = MySQLUtil.getConnection().prepareStatement(sql);

                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);

                calendar.add(Calendar.MONTH, i);
                System.out.println(df.format(calendar.getTime()));

                preparedStatement.setString(1, uuid.toString());
                preparedStatement.setString(2, df.format(calendar.getTime()));

                if (Bukkit.getPlayer(uuid) != null) Bukkit.getPlayer(uuid).sendMessage(ChatColor.AQUA + "あなたは本日から" + df.format(calendar.getTime()) + "までスポンサーです。");


                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param uuid (String) UUID of Player
     * @param i    Add sponsor month
     *
     */
    public static void savePlayer(String uuid, int i) {
        try {
            OpenConnection();
            String sql = "SELECT * FROM sponsor;";
            PreparedStatement statement = MySQLUtil.getConnection().prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            String uuidStr;

            while (result.next()) {
                uuidStr = result.getString(1);
                if (uuidStr.equalsIgnoreCase(uuid)) {
                    sql = "UPDATE sponsor SET date=? WHERE uuid=?;";
                    statement = MySQLUtil.getConnection().prepareStatement(sql);

                    if (i <= 0) statement.setString(1, "null");
                    else {
                        if (result.getString(2) == null || result.getString(2).equalsIgnoreCase("null")) {
                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = new Date();

                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(date);

                            calendar.add(Calendar.MONTH, i);

                            statement.setString(1, df.format(calendar.getTime()));

                            if (Bukkit.getPlayer(UUID.fromString(uuid)) != null) Bukkit.getPlayer(UUID.fromString(uuid)).sendMessage(ChatColor.AQUA + "あなたは本日から" + df.format(calendar.getTime()) + "までスポンサーです。");
                        } else {
                            try {
                                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                df.setLenient(false);
                                String s1 = result.getString(2);

                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(df.parse(s1));

                                calendar.add(Calendar.MONTH, i);

                                statement.setString(1, df.format(calendar.getTime()));

                                if (Bukkit.getPlayer(UUID.fromString(uuid)) != null) Bukkit.getPlayer(UUID.fromString(uuid)).sendMessage(ChatColor.AQUA + "あなたは本日から" + df.format(calendar.getTime()) + "までスポンサーです。");

                            } catch (ParseException p) {
                                p.printStackTrace();
                                return;
                            }
                        }
                    }
                    statement.setString(2, uuid);
                    statement.executeUpdate();
                    return;
                }
            }

            sql = "INSERT INTO sponsor (uuid, date) VALUES (?, ?);";
            PreparedStatement preparedStatement = MySQLUtil.getConnection().prepareStatement(sql);

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            calendar.add(Calendar.MONTH, i);
            System.out.println(df.format(calendar.getTime()));

            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, df.format(calendar.getTime()));

            if (Bukkit.getPlayer(UUID.fromString(uuid)) != null) Bukkit.getPlayer(UUID.fromString(uuid)).sendMessage(ChatColor.AQUA + "あなたは本日から" + df.format(calendar.getTime()) + "までスポンサーです。");

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param  uuid - UUID of Player
     * @return Date - Number of days remaining in the sponsorship period
     */
    public static Date getSponsorTime(UUID uuid) {
        int error = OpenConnection();
        if (error == -1) {
            Log.error("HighFunctionalityLibがねえ...");
        } else if (error == 0) {
            Log.error("MySQLエラー");
        } else {
            try {
                String sql = "SELECT * FROM sponsor;";
                PreparedStatement statement = MySQLUtil.getConnection().prepareStatement(sql);
                ResultSet result = statement.executeQuery();
                String uuidStr;

                while (result.next()) {
                    uuidStr = result.getString(1);
                    if (uuidStr.equalsIgnoreCase(uuid.toString())) {
                        String strDate = result.getString(2);
                        if (strDate.equalsIgnoreCase("null")) return null;
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        return dateFormat.parse(strDate);
                    }
                }
            } catch (SQLException | ParseException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * Player is Sponsor
     * @param uuid - UUID of Player
     * @return
     */
    public static boolean isSponsor(UUID uuid) {
        Date date = getSponsorTime(uuid);
        Date today = new Date();
        if (date == null) return false;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return !calendar.after(today);
    }

    public static boolean isSponsor(Date date) {
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return !calendar.after(today);
    }

}