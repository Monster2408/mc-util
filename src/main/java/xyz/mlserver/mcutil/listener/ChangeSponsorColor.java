package xyz.mlserver.mcutil.listener;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import xyz.mlserver.mcutil.Color;

import java.util.UUID;

public class ChangeSponsorColor extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private final UUID uuid;
    private final Color color;

    public ChangeSponsorColor(UUID uuid, Color color) {
        this.uuid  = uuid;
        this.color = color;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Color getColor() {
        return color;
    }

}
