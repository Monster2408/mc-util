package xyz.mlserver.mls.sponsor;

import org.bukkit.Bukkit;
import xyz.mlserver.mc.util.Color;
import xyz.mlserver.mls.listener.ChangeSponsorColor;

import java.util.UUID;

public class SponsorColor {

    private final UUID uuid;
    private Color color;

    public SponsorColor(UUID uuid, Color color) {
        this.uuid  = uuid;
        this.color = color;
    }

    public void setColor(Color color) {
        this.color = color;
        Bukkit.getPluginManager().callEvent(new ChangeSponsorColor(this.uuid, this.color));
    }

    public UUID getUuid() {
        return uuid;
    }

    public Color getColor() {
        return color;
    }

    public void save() {

    }

}
