package com.sakuratown.sakuraminions.minions;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class InventoryGUI {
    private MenuButton[] menuButtons;
    MinionInventory inventories;

    public InventoryGUI(MinionInventory inventories) {
    this.inventories = inventories;

    }
    public void showInventoryGUI(int page,Player player){
        Inventory inventory = inventories.getInventoryList().get(page-1);
        player.openInventory(inventory);
    }

}
