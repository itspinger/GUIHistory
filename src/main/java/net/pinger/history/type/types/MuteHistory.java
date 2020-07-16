package net.pinger.history.type.types;

import net.pinger.history.History;
import net.pinger.history.type.HistoryType;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.UUID;

public class MuteHistory extends HistoryType {

    public MuteHistory(History history, UUID id, String reason, long occurred, long expiring, String executor, boolean active) {
        super(history, id, reason, occurred, expiring, executor, active);
    }

    @Override
    public String name() {
        return ChatColor.GOLD + "MUTE";
    }

    @Override
    public Material material() {
        return Material.ARROW;
    }
}
