package xyz.mlserver.mcutil;

import org.bukkit.ChatColor;
import xyz.mlserver.lang.LanguageUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandUtil {

    private HashMap<String, String> commandUtilHash;
    private final String cmd;
    private String format;

    public CommandUtil(String cmd, String format) {
        this.commandUtilHash = new HashMap<>();
        this.cmd = cmd;
        this.format = format;
    }

    public CommandUtil(String cmd) {
        this.commandUtilHash = new HashMap<>();
        this.cmd = cmd;
        this.format = ChatColor.GOLD + "%CMD%: " + ChatColor.WHITE + "%DESCRIPTION%";
    }

    public CommandUtil add(String cmd, String description) {
        this.commandUtilHash.put(cmd, description);
        return this;
    }

    public String getFormat() {
        return format;
    }

    public HashMap<String, String> getCommandUtilHash() {
        return commandUtilHash;
    }

    public String getCmd() {
        return cmd;
    }

    public String getCommand() {
        return cmd;
    }

    public void setFormat(String format) {
        this.format = format;
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
        _list.add(ChatColor.YELLOW + "-------- " + ChatColor.WHITE + "HELP: page" + i + "/" + max + ChatColor.YELLOW + " --------");
        for (int n = 0; n < COMMAND_LINES; n++) {
            if (ListI >= list.size()) return _list;
            _list.add(list.get(ListI));
            ListI++;
        }
        return _list;
    }
}
