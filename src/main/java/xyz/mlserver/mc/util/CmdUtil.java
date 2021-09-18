package xyz.mlserver.mc.util;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CmdUtil {

    private final HashMap<String, String> commandUtilHash;
    private String format;
    private String header;

    private final String temp_heder = ChatColor.YELLOW + "-------- " + ChatColor.WHITE + "HELP: page" + "%NOW-PAGE%/%MAX-PAGE%" + ChatColor.YELLOW + " --------";
    private final String temp_format = ChatColor.GOLD + "%CMD%: " + ChatColor.WHITE + "%DESCRIPTION%";

    public CmdUtil(String format, String header) {
        this.commandUtilHash = new HashMap<>();
        this.format = format;
        this.header = header;
    }

    public CmdUtil(String format) {
        this.commandUtilHash = new HashMap<>();
        this.format = format;
        this.header = temp_heder;
    }

    public CmdUtil() {
        this.commandUtilHash = new HashMap<>();
        this.format = temp_format;
        this.header = temp_heder;
    }

    public CmdUtil add(String cmd, String description) {
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

    public List<String> getHelpMsg(int page) {
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
        int COMMAND_LINES = 7;
        List<String> _list = new ArrayList<>();
        if ((list.size() % COMMAND_LINES) > 0) {
            max = (list.size() / COMMAND_LINES) + 1;
        } else {
            max = (list.size() / COMMAND_LINES);
        }
        int ListI;
        if (max < page) {
            ListI = 0;
            page = 1;
        } else {
            ListI = page - 1;
        }
        ListI = ListI * COMMAND_LINES;
        text = header;
        text = text.replace("%NOW-PAGE%", String.valueOf(page));
        text = text.replace("%MAX-PAGE%", String.valueOf(max));
        _list.add(text);
        for (int n = 0; n < COMMAND_LINES; n++) {
            if (ListI >= list.size()) return _list;
            _list.add(list.get(ListI));
            ListI++;
        }
        return _list;
    }

    public List<TextComponent> getAdvanceHelpMsg(int page) {
        List<TextComponent> list = new ArrayList<>();
        TextComponent component;
        String text;
        HashMap<String, String> hash = this.getCommandUtilHash();
        for (String cmd : hash.keySet()) {
            text = format;
            text = text.replace("%CMD%", cmd);
            text = text.replace("%DESCRIPTION%", hash.get(cmd));
            component = new TextComponent(text);
            component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, cmd));
            list.add(component);
        }
        int max;
        int COMMAND_LINES = 7;
        List<TextComponent> _list = new ArrayList<>();
        if ((list.size() % COMMAND_LINES) > 0) {
            max = (list.size() / COMMAND_LINES) + 1;
        } else {
            max = (list.size() / COMMAND_LINES);
        }
        int ListI;
        if (max < page) {
            ListI = 0;
            page = 1;
        } else {
            ListI = page - 1;
        }
        ListI = ListI * COMMAND_LINES;
        text = header;
        text = text.replace("%NOW-PAGE%", String.valueOf(page));
        text = text.replace("%MAX-PAGE%", String.valueOf(max));
        _list.add(new TextComponent(text));
        for (int n = 0; n < COMMAND_LINES; n++) {
            if (ListI >= list.size()) return _list;
            _list.add(list.get(ListI));
            ListI++;
        }
        return _list;
    }
}
