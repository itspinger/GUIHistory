package net.pinger.history.inventory;

import net.pinger.history.History;
import org.intelligent.inventories.manager.IntelligentManager;

public class InventoryManager {

    private final History history;
    private final IntelligentManager manager;

    public InventoryManager(History history) {
        this.history = history;

        // Create a new inventory manager
        this.manager = new IntelligentManager(history);
    }




}
