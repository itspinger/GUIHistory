package net.pinger.history.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryEvent implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null)
            return;

        if (event.getClickedInventory().getTitle().endsWith("History")) {
            event.setCancelled(true);

            if (event.getCurrentItem().getType() == null || event.getCurrentItem().getType().equals(Material.AIR))
                return;

            if (event.getCurrentItem().getType() == Material.NETHER_STAR) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(event.getCurrentItem().getItemMeta().getDisplayName().split("\\s++")[1]);
                event.getWhoClicked().closeInventory();
                Bukkit.dispatchCommand(event.getWhoClicked(), "punish " + offlinePlayer.getName());
            }
        }
    }
}
