package net.pinger.history;

import net.pinger.history.commands.HistoryCommand;
import net.pinger.history.item.ItemBuilder;
import net.pinger.history.sql.Database;
import net.pinger.history.type.HistoryType;
import net.pinger.history.user.HistoryUser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
    private HistoryUserManager historyUserManager;
    private final Map<UUID, HistoryUser> historyUsers = new HashMap<>();

    @Override
    public void onEnable() {
        this.database = new Database(getConfig().getString("name"),
                getConfig().getString("password"),
                getConfig().getString("database"),
                getConfig().getString("host"),
                getConfig().getInt("port"));

        this.addDefaultConfig();

        this.database.createConnection();
        this.historyUserManager = new HistoryUserManager(this);

        getCommand("history").setExecutor(new HistoryCommand(this));
    }

    @Override
    public void onDisable() {
        try {
            this.database.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addDefaultConfig() {
        getConfig().options().copyDefaults(true);
        getConfig().addDefault("host", "localhost");
        getConfig().addDefault("port", 3306);
        getConfig().addDefault("name", "root");
        getConfig().addDefault("password", "");
        getConfig().addDefault("database", "history");

        saveConfig();
        saveDefaultConfig();
    }

    public Inventory getPlayerHistory(UUID id) {
        OfflinePlayer off = Bukkit.getOfflinePlayer(id);
        Inventory inventory = Bukkit.createInventory(null, 54, off.getName() + "'s History");

        HistoryUser user = this.historyUsers.get(id);
        if (user == null) {
            user = new HistoryUser(id, this);
            this.historyUsers.put(id, user);
        }

        user.loadHistory();
        for (int i = 0; i < user.getHistoryTypes().size(); i++) {
            HistoryType type = user.getHistoryTypes().get(i);
            ItemBuilder builder = new ItemBuilder(type.material());

            DateFormat df = new SimpleDateFormat("yyyy - MM - dd");
            DateFormat sec = new SimpleDateFormat("yyyy - MM - dd HH:mm::ss");

            builder.lore(" - " + ChatColor.GOLD + "Type: " + type.name(),
                            " - " + ChatColor.GOLD + "Active: " + type.isActivePunishment(),
                            " - " + ChatColor.GOLD + "Reason: " + type.getReason(),
                            " - " + ChatColor.GOLD + "Occurred: " + df.format(type.getOccurred()),
                            " - " + ChatColor.GOLD + "Expiring: " + sec.format(type.getExpiring()),
                            " - " + ChatColor.GOLD + "Issued by: " + type.getExecutor());
            inventory.setItem(i, builder.build());
        }

        inventory.setItem(53, new ItemBuilder(Material.NETHER_STAR).name(ChatColor.RED + "Punish " + off.getName()).build());
        return inventory;
    }

    public Database getDatabaseServer() {
        return this.database;
    }

    public HistoryUserManager getHistoryUserManager() {
        return historyUserManager;
    }
}
