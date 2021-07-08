package xyz.mlserver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class CustomConfig {
    private FileConfiguration config;
    private final File configFile;
    private final String file;
    private final Plugin plugin;

    public CustomConfig(Plugin plugin) {
        this(plugin, "config.yml");
    }

    public CustomConfig(Plugin plugin, String fileName) {
        this.config = null;
        this.plugin = plugin;
        this.file = fileName;
        this.configFile = new File(plugin.getDataFolder(), this.file);
    }

    public void saveDefaultConfig() {
        if (!this.configFile.exists()) {
            this.plugin.saveResource(this.file, false);
        }

    }

    public void reloadConfig() {
        this.config = YamlConfiguration.loadConfiguration(this.configFile);
        InputStream defConfigStream = this.plugin.getResource(this.file);
        if (defConfigStream != null) {
            this.config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, StandardCharsets.UTF_8)));
        }
    }

    public FileConfiguration getConfig() {
        if (this.config == null) {
            this.reloadConfig();
        }

        return this.config;
    }

    public void saveConfig() {
        if (this.config != null) {
            try {
                this.getConfig().save(this.configFile);
            } catch (IOException var2) {
                this.plugin.getLogger().log(Level.SEVERE, "Could not save config to " + this.configFile, var2);
            }

        }
    }

    public void setLocation(String path, Location loc) {
        if (this.config == null) {
            this.reloadConfig();
        }

        try {
            this.config.set(path + ".W", loc.getWorld().getName());
            this.config.set(path + ".X", loc.getX());
            this.config.set(path + ".Y", loc.getY());
            this.config.set(path + ".Z", loc.getZ());
            this.config.set(path + ".Yaw", String.valueOf(loc.getYaw()));
            this.config.set(path + ".Pitch", String.valueOf(loc.getPitch()));
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    public Location getLocation(String path) {
        if (this.config == null) {
            this.reloadConfig();
        }

        if (this.config.getString(path + ".W") == null) {
            return null;
        } else if (Bukkit.getWorld(this.config.getString(path + ".W")) == null) {
            return null;
        } else {
            return this.config.get(path + ".Yaw") == null && this.config.get(path + ".Pitch") == null ? new Location(Bukkit.getWorld(this.config.getString(path + ".W")), this.config.getDouble(path + ".X"), this.config.getDouble(path + ".Y"), this.config.getDouble(path + ".Z")) : new Location(Bukkit.getWorld(this.config.getString(path + ".W")), this.config.getDouble(path + ".X"), this.config.getDouble(path + ".Y"), this.config.getDouble(path + ".Z"), Float.parseFloat(this.config.getString(path + ".Yaw")), Float.parseFloat(this.config.getString(path + ".Pitch")));
        }
    }
}