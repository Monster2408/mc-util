package xyz.mlserver.mcutil;

import org.bukkit.ChatColor;
import xyz.mlserver.lang.LanguageUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandUtil {

    private final HashMap<String, String> commandUtilHash;
    private String format;
    private String header;

    private final String temp_heder = ChatColor.YELLOW + "-------- " + ChatColor.WHITE + "HELP: page" + "%NOW-PAGE%/%MAX-PAGE%" + ChatColor.YELLOW + " --------";
    private final String temp_format = ChatColor.GOLD + "%CMD%: " + ChatColor.WHITE + "%DESCRIPTION%";

    public CommandUtil(String format, String header) {
        this.commandUtilHash = new HashMap<>();
        this.format = format;
        this.header = header;
    }

    public CommandUtil(String format) {
        this.commandUtilHash = new HashMap<>();
        this.format = format;
        this.header = temp_heder;
    }

    public CommandUtil() {
        this.commandUtilHash = new HashMap<>();
        this.format = temp_format;
        this.header = temp_heder;
    }

    public CommandUtil add(String cmd, String description) {
        this.commandUtilHash.put(cmd, description);
        return this;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public HashMap<String, String> getCommandUtilHash() {
        return commandUtilHash;
    }

    public List<String> getHelpMsg() {
        List<String> list = new ArrayList<>();
        String text;
        HashMap<String, String> hash = this.getCommandUtilHash();
        for (String cmd : hash.keySet()) {
            text = format;
            text = text.replace("%CMD%", cmd);
            text = text.replace("%DESCRIPTION%", hash.get(cmd));
            list.add(text);
        }
        int max;
        int i = 8;
        int COMMAND_LINES = 7;
        List<String> _list = new ArrayList<>();
        if ((list.size() % COMMAND_LINES) > 0) {
            max = (list.size() / COMMAND_LINES) + 1;
        } else {
            max = (list.size() / COMMAND_LINES);
        }
        int ListI;
        if (max < i) {
            ListI = 0;
            i = 1;
        } else {
            ListI = i - 1;
        }
        ListI = ListI * COMMAND_LINES;
        _list.add(header);
        for (int n = 0; n < COMMAND_LINES; n++) {
            if (ListI >= list.size()) return _list;
            _list.add(list.get(ListI));
            ListI++;
        }
        return _list;
    }
}
