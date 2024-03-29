package net.pinger.history.inventory.providers;

import net.pinger.history.History;
import net.pinger.history.item.ItemBuilder;
import net.pinger.history.item.XMaterial;
import net.pinger.history.type.HistoryType;
import net.pinger.history.user.HistoryUser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.intelligent.inventories.contents.InventoryContents;
import org.intelligent.inventories.contents.InventoryPagination;
import org.intelligent.inventories.contents.IteratorType;
import org.intelligent.inventories.item.IntelligentItem;
import org.intelligent.inventories.provider.IntelligentProvider;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class HistoryProvider implements IntelligentProvider {

    private final History history;
    private final OfflinePlayer player;

    public HistoryProvider(History history, OfflinePlayer player) {
        this.history = history;
        this.player = player;
    }

    @Override
    public void initialize(Player player, InventoryContents contents) {
        // Get the user object from the uuid of this player
        HistoryUser user = this.history.getHistoryUserManager().getHistoryUser(player.getUniqueId());

        // Get the pagination
        InventoryPagination pagination = contents.getPagination();

        // Load user history
        user.loadHistory(list -> {
            // Load the inventory here
            IntelligentItem[] items = new IntelligentItem[list.size()];

            for (int i = 0; i < items.length; i++) {
                // Get the type
                HistoryType type = list.get(i);

                // Add to the items
                items[i++] = IntelligentItem.createNew(getHistoryType(type));
            }

            // Add to pagination
            pagination.setItems(items);
            pagination.setItemsPerPage(36);
            pagination.addToIterator(contents.newIterator(IteratorType.HORIZONTAL, 0, 0));
        });

        // Add punish item
        ItemStack punish = new ItemBuilder(XMaterial.NETHER_STAR)
                .name(ChatColor.RED + "Punish " + player.getName())
                .build();

        contents.setItem(5, 4, IntelligentItem.createNew(punish, e -> {
            Bukkit.dispatchCommand(e.getWhoClicked(), "punish " + player.getName());
        }));
    }

    private ItemStack getHistoryType(HistoryType type) {
        ItemBuilder builder = new ItemBuilder(type.material());

        // Set name
        builder.name(ChatColor.GOLD + ChatColor.BOLD.toString() + type.name());

        // Create two different date formats
        // For outputting occurred and expiring date of the punishment
        DateFormat occurredFormat = new SimpleDateFormat("yyyy - MM - dd");
        DateFormat expiringFormat = new SimpleDateFormat("yyyy - MM - dd HH:mm:ss");

        // Build the lore
        builder.lore(ChatColor.GRAY + "Type:", ChatColor.GOLD + type.name(),
                ChatColor.GRAY + "Active:", ChatColor.GOLD + type.isActivePunishment(),
                ChatColor.GRAY + "Reason:", ChatColor.GOLD + type.getReason(),
                ChatColor.GRAY + "Occurred", ChatColor.GOLD + occurredFormat.format(type.getOccurred()),
                ChatColor.GRAY + "Expiring:", ChatColor.GOLD + expiringFormat.format(type.getExpiring()),
                ChatColor.GOLD + "Issued by:", ChatColor.GOLD + type.getExecutor());

        // Return this
        return builder.build();
    }
//
//    public Inventory getPlayerHistory(UUID id) {
//        OfflinePlayer off = Bukkit.getOfflinePlayer(id);
//        Inventory inventory = Bukkit.createInventory(null, 54, off.getName() + "'s History");
//
//        HistoryUser user = this.historyUsers.get(id);
//        if (user == null) {
//            user = new HistoryUser(id, this);
//            this.historyUsers.put(id, user);
//        }
//
//        user.loadHistory();
//        for (int i = 0; i < user.getHistoryTypes().size(); i++) {
//            HistoryType type = user.getHistoryTypes().get(i);
//            ItemBuilder builder = new ItemBuilder(type.material());
//
//            DateFormat df = new SimpleDateFormat("yyyy - MM - dd");
//            DateFormat sec = new SimpleDateFormat("yyyy - MM - dd HH:mm::ss");
//
//            builder.lore(" - " + ChatColor.GOLD + "Type: " + type.name(),
//                    " - " + ChatColor.GOLD + "Active: " + type.isActivePunishment(),
//                    " - " + ChatColor.GOLD + "Reason: " + type.getReason(),
//                    " - " + ChatColor.GOLD + "Occurred: " + df.format(type.getOccurred()),
//                    " - " + ChatColor.GOLD + "Expiring: " + sec.format(type.getExpiring()),
//                    " - " + ChatColor.GOLD + "Issued by: " + type.getExecutor());
//            inventory.setItem(i, builder.build());
//        }
//
//        inventory.setItem(53, new ItemBuilder(Material.NETHER_STAR).name(ChatColor.RED + "Punish " + off.getName()).build());
//        return inventory;
//    }
}
