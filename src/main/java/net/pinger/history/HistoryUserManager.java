package net.pinger.history;

import net.pinger.history.user.HistoryUser;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HistoryUserManager {

    private final History history;
    private final Map<UUID, HistoryUser> users = new HashMap<>();

    public HistoryUserManager(History history) {
        this.history = history;
    }

    public HistoryUser getHistoryUser(UUID id) {
        HistoryUser user = this.users.get(id);

        if (user == null) {
            // Put the new user
            this.users.put(id, (user = new HistoryUser(id, this.history)));
        }

        // Return the user
        return user;
    }

}
