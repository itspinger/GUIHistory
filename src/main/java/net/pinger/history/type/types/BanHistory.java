package net.pinger.history.type.types;

import net.pinger.history.History;
import net.pinger.history.type.HistoryType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class BanHistory extends HistoryType {

    public BanHistory(History history, UUID id, String reason, long occurred, long expiring, String executor, boolean active) {
        super(history, id, reason, occurred, expiring, executor, active);
    }

    @Override
    public String name() {
        return ChatColor.GOLD + "BAN";
    }

    @Override
    public Material material() {
        return new ItemStack(Material.SKULL_ITEM, (byte) 1).getType();
    }
}
