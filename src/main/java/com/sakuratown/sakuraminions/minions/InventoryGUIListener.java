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

        int nowPage = gui.getNowPage();

        ItemStack currentItem = event.getCurrentItem();
        if (currentItem == null) return;

        String displayName = currentItem.getItemMeta().getDisplayName();

        if (displayName.equals(Config.getMenuSection().getString("LastPage.Name"))) {
            gui.showInventoryGUI(--nowPage, player);
            gui.setNowPage(nowPage);
            event.setCancelled(true);
        }

        if (displayName.equals(Config.getMenuSection().getString("NextPage.Name"))) {
            gui.showInventoryGUI(++nowPage, player);
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
    }
    @EventHandler
    public void inventoryCloseEvent(InventoryCloseEvent event) {//关闭后自动排序
        MinionInventory gui = getGui(event.getInventory());
        if (gui == null) return;
        gui.sortItems();
    }

    private MinionInventory getGui(Inventory inventory) {
        InventoryHolder holder = inventory.getHolder();

        if (!(holder instanceof MinionInventory)) return null;

        return (MinionInventory) holder;
    }

    private void denyPutItem(InventoryClickEvent event, Inventory clickedInventory) {

        if ((event.getClick().isShiftClick() && clickedInventory.getType() == InventoryType.PLAYER)) {
            event.setCancelled(true);
            return;
        }

        ItemStack cursor = event.getCursor();
        if (cursor == null) return;

        if (cursor.getType() != Material.AIR && clickedInventory == event.getInventory()) {
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
