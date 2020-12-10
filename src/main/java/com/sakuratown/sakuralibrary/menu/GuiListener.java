package com.sakuratown.sakuralibrary.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class GuiListener implements Listener {

    @EventHandler
    public void inventoryClickEvent(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player)) return;

        Inventory inventory = event.getInventory();
        ItemStack currentItem = event.getCurrentItem();

        Gui gui = getGui(event);
        if (gui == null) return;

        if (gui.isLock) {
            InventoryUtil.denyClick(event, inventory);
        }

        int rawSlot = event.getRawSlot();

        Button button;

        if (event.isShiftClick()) {
            int shiftClickSlot = InventoryUtil.getShiftClickSlot(inventory, currentItem);
            button = gui.getButton(shiftClickSlot);
        } else {
            button = gui.getButton(rawSlot);
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

        Gui gui = getGui(event);
        if (gui == null) return;

        if (gui.isLock) {
            for (int i : event.getRawSlots()) {

                if (gui.isLock && i < inventory.getSize()) {
                    event.setCancelled(true);
                }

                Button button = gui.getButton(i);

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

        Gui gui = getGui(event);
        if (gui == null || gui.closeEvent == null) return;

        gui.closeEvent.accept(event);
    }

    @EventHandler
    public void inventoryOpenEvent(InventoryOpenEvent event) {

        Gui gui = getGui(event);
        if (gui == null || gui.openEvent == null) return;

        gui.openEvent.accept(event);
    }

    private Gui getGui(InventoryEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();

        if (!(holder instanceof Gui)) return null;

        return (Gui) holder;
    }
}