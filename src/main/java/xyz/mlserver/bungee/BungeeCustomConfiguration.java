package xyz.mlserver.bungee;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

public class BungeeCustomConfiguration {
    private Configuration config = null;
    private final File configFile;
    private final String file;
    private final Plugin plugin;

    /**
     * config.ymlを生成する
     * @param plugin onEnableでthisつかって...
     */
    public BungeeCustomConfiguration(Plugin plugin) {
        this(plugin, "config.yml");
    }

    /**
     * 任意の名前.ymlを生成する
     * @param plugin onEnableでthisつかって...
     * @param fileName 任意の名前
     */
    public BungeeCustomConfiguration(Plugin plugin, String fileName) {
        this.plugin = plugin;
        this.file = fileName;
        configFile = new File(plugin.getDataFolder(), file);
    }

    public void saveDefaultConfig() {
        if (!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdir();
        if (!configFile.exists()) copyResource(plugin.getResourceAsStream(file), configFile);
    }

    public void reloadConfig() {
        try { config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile); }
        catch (IOException ex) { plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex); }
    }

    public Configuration getConfig() {
        if (config == null) reloadConfig();
        return config;
    }

    public void saveConfig() {
        if (config == null) return;
        try { ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, configFile); }
        catch (IOException ex) { plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex); }
    }

    private void copyResource(InputStream is, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len=is.read(buf))>0) out.write(buf,0,len);
            out.close();
            is.close();
        } catch (Exception e) { e.printStackTrace(); }
    }

}
