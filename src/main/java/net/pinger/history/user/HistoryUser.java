package net.pinger.history.user;

import net.pinger.history.History;
import net.pinger.history.type.HistoryType;
import net.pinger.history.type.types.BanHistory;
import net.pinger.history.type.types.KickHistory;
import net.pinger.history.type.types.MuteHistory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HistoryUser {

    private final UUID id;
    private final History history;

    private List<HistoryType> historyTypes = new ArrayList<>();

    public HistoryUser(UUID id, History history) {
        this.id = id;
        this.history = history;
    }

    // TO DO
    public void loadHistory() {
        if (!this.historyTypes.isEmpty()) this.historyTypes.clear();
        Connection connection = history.getDatabaseServer().getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM litebans_bans WHERE `uuid` = ?;");
            preparedStatement.setString(1, this.id.toString());
            preparedStatement.executeQuery();

            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                BanHistory banHistory = new BanHistory(this.history, id,
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
                KickHistory kickHistory = new KickHistory(this.history, id,
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
                MuteHistory muteHistory = new MuteHistory(this.history, id,
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
