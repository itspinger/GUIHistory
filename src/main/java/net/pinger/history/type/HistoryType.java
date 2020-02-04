package net.pinger.history.type;

import net.pinger.history.History;
import org.bukkit.Material;

import java.util.UUID;

public abstract class HistoryType {

    private final History history;

    private final String reason;
    private final long occurred, expiring;

    private final String executor;
    private final UUID id;

    public HistoryType(History history, UUID id, String reason, long occurred, long expiring, String executor) {
        this.history = history;
        this.id = id;
        this.reason = reason;
        this.occurred = occurred;
        this.expiring = expiring;
        this.executor = executor;
    }

    public abstract String name();
    public abstract Material material();


    public String getReason() {
        return reason;
    }

    public long getOccurred() {
        return occurred;
    }

    public long getExpiring() {
        return expiring;
    }

    public String getExecutor() {
        return executor;
    }
}
