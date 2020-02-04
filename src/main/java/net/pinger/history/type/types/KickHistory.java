package net.pinger.history.type.types;

import net.pinger.history.History;
import net.pinger.history.type.HistoryType;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.UUID;

public class KickHistory extends HistoryType {

    public KickHistory(History history, UUID id, String reason, long occurred, long expiring, String executor) {
        super(history, id, reason, occurred, expiring, executor);
    }

    @Override
    public String name() {
        return ChatColor.GOLD +  "KICK";
    }

    @Override
    public Material material() {
        return Material.BLAZE_ROD;
    }
}
