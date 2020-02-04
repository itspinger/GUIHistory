package net.pinger.history.user;

import net.pinger.history.History;
import net.pinger.history.type.HistoryType;

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

    }

    public List<HistoryType> getHistoryTypes() {
        return historyTypes;
    }

    public UUID getUniqueId() {
        return id;
    }
}
