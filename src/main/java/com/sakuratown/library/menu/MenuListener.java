package com.sakuratown.library.menu;

import com.sakuratown.minions.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class MenuListener implements Listener {

    //TODO SHIFT 点击可以拿下菜单
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
    public void inventoryOpenEvent(InventoryOpenEvent event) {

        Menu menu = getMenu(event);
        if (menu == null || menu.openEvent == null) return;

        // 防止每次翻页都触发 InventoryOpenEvent, 也许有更好的写法
        if (menu instanceof PageableMenu) {

            new BukkitRunnable() {
                @Override
                public void run() {
                    Inventory topInventory = event.getPlayer().getOpenInventory().getTopInventory();
                    if (!topInventory.equals(menu.inventory)) {
                        menu.openEvent.accept(event);
                    }
                }
            }.runTaskLater(Main.getInstance(), 1);

        } else {
            menu.openEvent.accept(event);
        }
    }

    @EventHandler
    public void inventoryCloseEvent(InventoryCloseEvent event) {

        Menu menu = getMenu(event);

        if (menu == null || menu.closeEvent == null) return;
        if (menu instanceof PageableMenu) {

            new BukkitRunnable() {
                @Override
                public void run() {
                    Inventory topInventory = event.getPlayer().getOpenInventory().getTopInventory();
                    if (!topInventory.equals(menu.inventory)) {
                        menu.closeEvent.accept(event);
                    }
                }
            }.runTaskLater(Main.getInstance(), 1);

        } else {
            menu.closeEvent.accept(event);
        }
    }

    private Menu getMenu(InventoryEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();

        if (!(holder instanceof Menu)) return null;

        return (Menu) holder;
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getInventory().getHolder() instanceof Menu) {
                player.closeInventory();
            }
        }
    }
}