package net.pinger.history.commands;

import net.pinger.history.History;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HistoryCommand implements CommandExecutor {

    private final History history;

    public HistoryCommand(History history) {
        this.history = history;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        Player player = (Player) commandSender;
        if (!player.hasPermission("history.view")) {
            player.sendMessage(ChatColor.RED + "You don't have enough permissions to use this command.");
            return true;
        }

        if (strings.length < 1 || strings.length > 1) {
            player.sendMessage(ChatColor.RED + "Use /history <player> to view players history.");
            return true;
        }

        String target = strings[0];
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(target);
        if (!offlinePlayer.hasPlayedBefore() || offlinePlayer.getUniqueId() == null) {
            player.sendMessage(ChatColor.RED + "This player has never played before");
            return true;
        }

        player.openInventory(history.getPlayerHistory(offlinePlayer.getUniqueId()));
        return true;
    }
}
