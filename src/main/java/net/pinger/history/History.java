package net.pinger.history;

import net.pinger.history.commands.HistoryCommand;
import net.pinger.history.events.InventoryEvent;
import net.pinger.history.sql.Database;
import net.pinger.history.type.HistoryType;
import net.pinger.history.user.HistoryUser;
import net.pinger.history.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class History extends JavaPlugin {

    private Database database;
    private Map<UUID, HistoryUser> historyUsers = new HashMap<>();

    @Override
    public void onEnable() {
        database = new Database(getConfig().getString("name"),
                getConfig().getString("password"),
                getConfig().getString("database"),
                getConfig().getString("host"),
                getConfig().getInt("port"));

        database.createConnection();
        Bukkit.getPluginManager().registerEvents(new InventoryEvent(), this);
        getCommand("history").setExecutor(new HistoryCommand(this));
    }

    @Override
    public void onDisable() {
        try {
            database.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Inventory getPlayerHistory(UUID id) {
        OfflinePlayer off = Bukkit.getOfflinePlayer(id);
        Inventory inventory = Bukkit.createInventory(null, 54, ChatColor.RED + off.getName() + "'s History");

        HistoryUser user = historyUsers.get(id);
        if (user == null) {
            user = new HistoryUser(id, this);
            historyUsers.put(id, user);
        }

        user.loadHistory();
        for (int i = 0; i < user.getHistoryTypes().size(); i++) {
            HistoryType type = user.getHistoryTypes().get(i);
            ItemBuilder builder = new ItemBuilder(type.material());

            DateFormat df = new SimpleDateFormat("yyyy - MM - dd");
            DateFormat sec = new SimpleDateFormat("yyyy - MM - dd HH:mm::ss");

            builder.setLore(" - " + ChatColor.RED + "Type: " + type.name(),
                            " - " + ChatColor.RED + "Reason: " + type.getReason(),
                            " - " + ChatColor.RED + "Occurred: " + df.format(type.getOccurred()),
                            " - " + ChatColor.RED + "Expiring: " + sec.format(type.getExpiring()),
                            " - " + ChatColor.RED + "Issued by: " + type.getExecutor());
            inventory.setItem(i, builder.toItemStack());
        }

        return inventory;
    }

    public Database database() {
        return this.database;
    }
}
