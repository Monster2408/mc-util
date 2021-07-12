package xyz.mlserver.lang;

import com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import xyz.mlserver.mcutil.OriginalItemStack;

import java.util.HashMap;
import java.util.UUID;

public class LanguageUtil {

    private static HashMap<String, Language> playerLang;
    private static Table<String, Language, String> languageTable;

    private final String key;

    private static final LanguageUtil LANGUAGE_PREFIX = LanguageUtil.register("LANGUAGE_PREFIX").jp("[言語] ").eng("[LANGUAGES] ");
    private static final LanguageUtil LANGUAGE_GUI = LanguageUtil.register("LANGUAGE_GUI").jp("言語設定").eng("Language Setting");

    public static LanguageUtil getPrefix() {
        return LANGUAGE_PREFIX;
    }


    public LanguageUtil(String key) {
        if (playerLang == null) playerLang = new HashMap<>();
        if (languageTable == null) languageTable = HashBasedTable.create();
        this.key = key;
    }

    public static LanguageUtil register(String key) {
        return new LanguageUtil(key);
    }

    public static HashMap<String, Language> getPlayerLang() {
        if (playerLang == null) playerLang = new HashMap<>();
        if (languageTable == null) languageTable = HashBasedTable.create();
        return playerLang;
    }

    public LanguageUtil eng(String eng) {
        languageTable.put(key, Language.ENGLISH, eng);
        return this;
    }

    public LanguageUtil jp(String jp) {
        languageTable.put(key, Language.JAPANESE, jp);
        return this;
    }

    public String get(Player player) {
        String uuid = player.getUniqueId().toString();
        playerLang.putIfAbsent(uuid, Language.ENGLISH);
        Language lang = playerLang.get(uuid);
        return languageTable.get(key, lang);
    }

    public String get(String uuid) {
        playerLang.putIfAbsent(uuid, Language.ENGLISH);
        Language lang = playerLang.get(uuid);
        return languageTable.get(key, lang);
    }

    public String get(UUID uuid) {
        playerLang.putIfAbsent(uuid.toString(), Language.ENGLISH);
        Language lang = playerLang.get(uuid.toString());
        return languageTable.get(key, lang);
    }

    public String replace(String replaceOld, String replaceNew, Player player) {
        String uuid = player.getUniqueId().toString();
        playerLang.putIfAbsent(uuid, Language.ENGLISH);
        Language lang = playerLang.get(uuid);
        String text = languageTable.get(key, lang);
        if (text == null) return null;
        text = text.replace(replaceOld, replaceNew);
        return text;
    }

    public String replace(String replaceOld, String replaceNew, UUID uuid) {
        playerLang.putIfAbsent(uuid.toString(), Language.ENGLISH);
        Language lang = playerLang.get(uuid.toString());
        String text = languageTable.get(key, lang);
        if (text == null) return null;
        text = text.replace(replaceOld, replaceNew);
        return text;
    }

    public String replace(String replaceOld, String replaceNew, String uuid) {
        playerLang.putIfAbsent(uuid, Language.ENGLISH);
        Language lang = playerLang.get(uuid);
        String text = languageTable.get(key, lang);
        if (text == null) return null;
        text = text.replace(replaceOld, replaceNew);
        return text;
    }

    public static void open(Player player) {
        String uuid = player.getUniqueId().toString();
        Inventory gui = Bukkit.createInventory(null , 9, LANGUAGE_GUI.get(uuid));



        ItemStack noClick, japaneseIcon,englishIcon;

        noClick = OriginalItemStack.createItem(XMaterial.GREEN_STAINED_GLASS_PANE.parseMaterial(), 1, " ");
        japaneseIcon = OriginalItemStack.createSkull(1, Language.JAPANESE.getName(), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODA0M2FlOWJiZmE4YjhiYmI1Yzk2NGJiY2U0NWZiZTc5YTNhZDc0MmJlMDdiNTY2MDdjNjhjOGUxMTE2NCJ9fX0=");
        englishIcon = OriginalItemStack.createSkull(1, Language.ENGLISH.getName(), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGNhYzk3NzRkYTEyMTcyNDg1MzJjZTE0N2Y3ODMxZjY3YTEyZmRjY2ExY2YwY2I0YjM4NDhkZTZiYzk0YjQifX19");

        for (int i = 0; i < 9; i++) {
            gui.setItem(i, noClick);
            if (i == 3) gui.setItem(i, japaneseIcon);
            if (i == 5) gui.setItem(i, englishIcon);
        }

        player.openInventory(gui);
    }

}

