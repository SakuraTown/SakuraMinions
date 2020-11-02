package com.sakuratown.sakuraminions.minions;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class InventoryGUI {
    MinionInventory inventories;
    private MenuButton[] menuButtons;

    public InventoryGUI(MinionInventory inventories) {
        this.inventories = inventories;

    }

    public void showInventoryGUI(int page, Player player) {
        Inventory inventory = inventories.getInventoryList().get(page - 1);
        inventories.setNowPage(page);
        player.openInventory(inventory);
    }
    public void setInventories(MinionInventory inventories){
        this.inventories = inventories;
    }

}
