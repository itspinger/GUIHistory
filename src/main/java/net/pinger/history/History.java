package net.pinger.history;

import net.pinger.history.commands.HistoryCommand;
import net.pinger.history.events.InventoryEvent;
import net.pinger.history.sql.Database;
import net.pinger.history.type.HistoryType;
import net.pinger.history.user.HistoryUser;
import net.pinger.history.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

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

        getConfig().addDefault("host", "localhost");
        getConfig().addDefault("port", 3306);
        getConfig().addDefault("name", "root");
        getConfig().addDefault("password", "");
        getConfig().addDefault("database", "history");
        getConfig().options().copyDefaults(true);
        saveConfig();


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
        Inventory inventory = Bukkit.createInventory(null, 54, off.getName() + "'s History");

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
                            " - " + ChatColor.RED + "Active: " + type.isActive(),
                            " - " + ChatColor.RED + "Reason: " + type.getReason(),
                            " - " + ChatColor.RED + "Occurred: " + df.format(type.getOccurred()),
                            " - " + ChatColor.RED + "Expiring: " + sec.format(type.getExpiring()),
                            " - " + ChatColor.RED + "Issued by: " + type.getExecutor());
            inventory.setItem(i, builder.toItemStack());
        }

        inventory.setItem(53, new ItemBuilder(Material.NETHER_STAR).setName(ChatColor.RED + "Punish " + off.getName()).toItemStack());
        return inventory;
    }

    public Database database() {
        return this.database;
    }
}
