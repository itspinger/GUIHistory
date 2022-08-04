package net.pinger.history.user;

import net.pinger.history.History;
import net.pinger.history.type.HistoryType;
import net.pinger.history.type.types.BanHistory;
import net.pinger.history.type.types.KickHistory;
import net.pinger.history.type.types.MuteHistory;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class HistoryUser {

    private final UUID id;
    private final History historyPlugin;

    private List<HistoryType> historyTypes = new ArrayList<>();

    public HistoryUser(UUID id, History history) {
        this.id = id;
        this.historyPlugin = history;
    }

    public void loadHistory(Consumer<List<HistoryType>> historyTypes) {
        Bukkit.getScheduler().runTaskAsynchronously(this.historyPlugin, new Runnable() {
            @Override
            public void run() {
                // Get the list of history types
                List<HistoryType> history = getHistory();

                // Go back and submit the list
                // Synchronously
                Bukkit.getScheduler().runTask(historyPlugin, new Runnable() {
                    @Override
                    public void run() {
                        historyTypes.accept(history);
                    }
                });
            }
        });
    }

    private List<HistoryType> getHistory() {
        // Create a new history list
        final List<HistoryType> list = new ArrayList<>();

        try (Connection connection = historyPlugin.getDatabaseServer().getConnection()) {
            // Bans
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM litebans_bans WHERE `uuid` = ?;")) {
                statement.setString(1, id.toString());
                statement.executeQuery();

                // Try to get the result set
                try (ResultSet set = statement.getResultSet()) {
                    while (set.next()) {
                        list.add(new BanHistory(historyPlugin, id,
                                set.getString("reason"),
                                set.getLong("time"),
                                set.getLong("until"),
                                set.getString("banned_by_name"),
                                set.getBoolean("active")));
                    }
                }
            }

            // Kicks
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM litebans_kicks WHERE `uuid` = ?;")) {
                statement.setString(1, id.toString());
                statement.executeQuery();

                // Get the result set
                try (ResultSet set = statement.getResultSet()) {
                    while (set.next()) {
                        list.add(new KickHistory(historyPlugin, id,
                                set.getString("reason"),
                                set.getLong("time"),
                                set.getLong("until"),
                                set.getString("banned_by_name"),
                                set.getBoolean("active")));
                    }
                }
            }

            // Mutes
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM litebans_mutes WHERE `uuid` = ?;")) {
                statement.setString(1, id.toString());
                statement.executeQuery();

                // Get the result set
                try (ResultSet set = statement.getResultSet()) {
                    while (set.next()) {
                        list.add(new MuteHistory(historyPlugin, id,
                                set.getString("reason"),
                                set.getLong("time"),
                                set.getLong("until"),
                                set.getString("banned_by_name"),
                                set.getBoolean("active")));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Return the list
        return list;
    }

    // TO DO
    public void loadHistory() {
        if (!this.historyTypes.isEmpty()) this.historyTypes.clear();
        Connection connection = historyPlugin.getDatabaseServer().getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM litebans_bans WHERE `uuid` = ?;");
            preparedStatement.setString(1, this.id.toString());
            preparedStatement.executeQuery();

            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                BanHistory banHistory = new BanHistory(this.historyPlugin, id,
                        resultSet.getString("reason"),
                        resultSet.getLong("time"),
                        resultSet.getLong("until"),
                        resultSet.getString("banned_by_name"),
                        resultSet.getBoolean("active"));

                historyTypes.add(banHistory);
            }

            preparedStatement = connection.prepareStatement("SELECT * FROM litebans_kicks WHERE `uuid` = ?;");
            preparedStatement.setString(1, this.id.toString());
            preparedStatement.executeQuery();

            resultSet = preparedStatement.getResultSet();

            while (resultSet.next()) {
                KickHistory kickHistory = new KickHistory(this.historyPlugin, id,
                        resultSet.getString("reason"),
                        resultSet.getLong("time"),
                        resultSet.getLong("until"),
                        resultSet.getString("banned_by_name"),
                        resultSet.getBoolean("active"));

                historyTypes.add(kickHistory);
            }

            preparedStatement = connection.prepareStatement("SELECT * FROM litebans_mutes WHERE `uuid` = ?;");
            preparedStatement.setString(1, this.id.toString());
            preparedStatement.executeQuery();

            resultSet = preparedStatement.getResultSet();

            while (resultSet.next()) {
                MuteHistory muteHistory = new MuteHistory(this.historyPlugin, id,
                        resultSet.getString("reason"),
                        resultSet.getLong("time"),
                        resultSet.getLong("until"),
                        resultSet.getString("banned_by_name"),
                        resultSet.getBoolean("active"));

                historyTypes.add(muteHistory);
            }

            preparedStatement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<HistoryType> getHistoryTypes() {
        return historyTypes;
    }

    public UUID getUniqueId() {
        return id;
    }
}
