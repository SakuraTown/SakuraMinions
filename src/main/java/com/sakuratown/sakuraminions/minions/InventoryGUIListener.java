package com.sakuratown.sakuraminions.minions;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
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

        Player player = ((Player) event.getWhoClicked()).getPlayer();
        Inventory clickedInventory = event.getClickedInventory();

        if (player == null || clickedInventory == null) return;

        MinionInventory gui = getGui(event.getInventory());
        if (gui == null) return;

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
            gui.sortItems();
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

        if (event.getCursor() != null && clickedInventory == event.getInventory()) {
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
