package com.sakuratown.sakuraminions.minions;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
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
        if (gui == null) return;
        Inventory inventory1 = gui.getCurrentInventory();
        if(event.getRawSlot() > inventory1.getSize()-1){return;}
        int nowPage = gui.getNowPage();
        if (currentItem == null) {
            return;
        }
        if (currentItem.getItemMeta().getDisplayName().equals(Config.getMenuSection().getString("LastPage.Name"))) {
            event.setCancelled(true);
            gui.showInventoryGUI(--nowPage, player);
            gui.setNowPage(nowPage);

            //denyClick(event, inventory);
        }
        if (currentItem.getItemMeta().getDisplayName().equals(Config.getMenuSection().getString("NextPage.Name"))) {
            event.setCancelled(true);
            gui.showInventoryGUI(++nowPage, player);
            gui.setNowPage(nowPage);

            //denyClick(event, inventory);
        }
        if (currentItem.getItemMeta().getDisplayName().equals(Config.getMenuSection().getString("PlaceHolder.Name"))) {
            event.setCancelled(true);
           // denyClick(event, inventory);
        }


    }

    @EventHandler
    public void inventoryCloseEvent(InventoryOpenEvent event){//这个打开事件是防止打开工人背包后快速点击按钮会失灵的情况
        if (!(event.getPlayer() instanceof Player)) return;
        Player player = ((Player) event.getPlayer()).getPlayer();
        MinionInventory gui = getGui(event);
        if (gui == null) return;
        gui.setCurrentInventory(event.getInventory());
    }

    private MinionInventory getGui(InventoryEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();

        if (!(holder instanceof MinionInventory)) return null;

        return (MinionInventory) holder;
    }
}
