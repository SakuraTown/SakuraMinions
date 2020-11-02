package com.sakuratown.sakuraminions.minions;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class InventoryGUIListener implements Listener {
    public static void denyClick(InventoryClickEvent event, Inventory targetInventory) {

        int size = targetInventory.getSize();

        if (event.getRawSlot() < size || event.isShiftClick()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void inventoryClickEvent(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = ((Player) event.getWhoClicked()).getPlayer();
        Inventory inventory = event.getInventory();
        ItemStack currentItem = event.getCurrentItem();
        MinionInventory gui = getGui(event);
        int totalPage = gui.getPage();
        int nowPage = gui.getNowPage();
        if (gui == null) return;
        if (currentItem == null) {
            return;
        }
        if (currentItem.getItemMeta().getDisplayName().equals(Config.getMenuSection().getString("LastPage.Name"))) {

            gui.showInventoryGUI(--nowPage, player);
            gui.setNowPage(nowPage);
            denyClick(event, inventory);
        }
        if (currentItem.getItemMeta().getDisplayName().equals(Config.getMenuSection().getString("NextPage.Name"))) {

            gui.showInventoryGUI(++nowPage, player);
            gui.setNowPage(nowPage);
            denyClick(event, inventory);
        }
        if (currentItem.getItemMeta().getDisplayName().equals(Config.getMenuSection().getString("PlaceHolder.Name"))) {
            denyClick(event, inventory);
        }


    }

    private MinionInventory getGui(InventoryEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();

        if (!(holder instanceof MinionInventory)) return null;

        return (MinionInventory) holder;
    }
}
