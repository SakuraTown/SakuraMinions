package com.sakuratown.library.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class MenuListener implements Listener {

    @EventHandler
    public void inventoryClickEvent(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player)) return;

        Inventory inventory = event.getInventory();
        ItemStack currentItem = event.getCurrentItem();

        Menu menu = getMenu(event);
        if (menu == null) return;

        if (menu.isLock) {
            InventoryUtil.denyClick(event, inventory);
        }

        int rawSlot = event.getRawSlot();

        Button button;

        if (event.isShiftClick()) {
            int shiftClickSlot = InventoryUtil.getShiftClickSlot(inventory, currentItem);
            button = menu.getButton(shiftClickSlot);
        } else {
            button = menu.getButton(rawSlot);
        }

        if (button == null) return;

        event.setCancelled(button.isLock);

        Player player = (Player) event.getWhoClicked();

        if (button.clickEvent != null) {
            button.clickEvent.accept(event);
        }

        button.runCommand(player);
    }

    @EventHandler
    public void inventoryDragEvent(InventoryDragEvent event) {

        Inventory inventory = event.getInventory();

        Menu menu = getMenu(event);
        if (menu == null) return;

        if (menu.isLock) {
            for (int i : event.getRawSlots()) {

                if (menu.isLock && i < inventory.getSize()) {
                    event.setCancelled(true);
                }

                Button button = menu.getButton(i);

                if (button == null) return;

                if (button.isLock) {
                    event.setCancelled(true);
                    return;
                }
            }
            event.setCancelled(false);
        }
    }

    @EventHandler
    public void inventoryCloseEvent(InventoryCloseEvent event) {

        Menu menu = getMenu(event);
        if (menu == null || menu.closeEvent == null) return;

        menu.closeEvent.accept(event);
    }

    @EventHandler
    public void inventoryOpenEvent(InventoryOpenEvent event) {

        Menu menu = getMenu(event);
        if (menu == null || menu.openEvent == null) return;

        menu.openEvent.accept(event);
    }

    private Menu getMenu(InventoryEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();

        if (!(holder instanceof Menu)) return null;

        return (Menu) holder;
    }
}