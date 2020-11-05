package com.sakuratown.sakuraminions.minions;


import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;


public class InventoryGUIListener implements Listener {

    @EventHandler
    public void inventoryClickEvent(InventoryClickEvent event) {

        Player player = event.getWhoClicked() instanceof Player ? (Player) event.getWhoClicked() : null;
        Inventory clickedInventory = event.getClickedInventory();
        MinionInventory gui = getGui(event.getInventory());

        if (player == null || clickedInventory == null || gui == null) return;

        denyPutItem(event, clickedInventory);
        ItemStack currentItem = event.getCurrentItem();
        if (currentItem == null) return;
        String displayName = currentItem.getItemMeta().getDisplayName();
        if (displayName.equals(Config.getMenuSection().getString("LastPage.Name"))) {
            gui.showInventoryGUI(gui.getPlayerPage(player,-1), player);
            event.setCancelled(true);
        }
        if (displayName.equals(Config.getMenuSection().getString("NextPage.Name"))) {
            gui.showInventoryGUI(gui.getPlayerPage(player,1), player);
            event.setCancelled(true);
        }
        if (displayName.equals(Config.getMenuSection().getString("PlaceHolder.Name"))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void inventoryDragEvent(InventoryDragEvent event) {
        denyPutItem(event);
    }

    @EventHandler
    public void inventoryOpenEvent(InventoryOpenEvent event) {
        MinionInventory gui = getGui(event.getInventory());
        if (gui == null) return;
        gui.setCurrentInventory(event.getInventory());
        gui.sortItems();
    }

    private MinionInventory getGui(Inventory inventory) {
        InventoryHolder holder = inventory.getHolder();

        if (!(holder instanceof MinionInventory)) return null;

        return (MinionInventory) holder;
    }

    private void denyPutItem(InventoryClickEvent event, Inventory clickedInventory) {

        ClickType click = event.getClick();

        if (click.isShiftClick() && clickedInventory.getType() == InventoryType.PLAYER) {
            event.setCancelled(true);
            return;
        }

        if (click == ClickType.NUMBER_KEY && clickedInventory.getType() == InventoryType.CHEST) {
            event.setCancelled(true);
            return;
        }

        ItemStack cursor = event.getCursor();
        if (cursor == null) return;

        if (cursor.getType() != Material.AIR && clickedInventory.getType() == InventoryType.CHEST) {
            event.setCancelled(true);
        }
    }

    private void denyPutItem(InventoryDragEvent event) {
        Inventory inventory = event.getInventory();

        for (int i : event.getRawSlots()) {

            if (i < inventory.getSize()) {
                event.setCancelled(true);
            }
        }
    }

}
