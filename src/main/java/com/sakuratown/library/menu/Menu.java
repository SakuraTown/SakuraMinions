package com.sakuratown.library.menu;


import com.sakuratown.library.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Menu implements InventoryHolder {

    public final Map<Integer, Button> buttonMap = new HashMap<>();
    private final Inventory inventory;
    public boolean isLock = true;
    public Consumer<InventoryOpenEvent> openEvent;
    public Consumer<InventoryCloseEvent> closeEvent;

    public Menu(String name, int row) {
        int size = row * 9;
        String title = Message.toColor(name);

        inventory = Bukkit.createInventory(this, size, title);
    }

    public Button getButton(int slot) {
        return buttonMap.get(slot);
    }

    public void setButton(Button button) {

        if (button == null) return;

        for (int slot : button.slots) {
            buttonMap.put(slot, button);
            inventory.setItem(slot, button.getItemStack());
        }

    }

    public void removeButton(int slot) {
        buttonMap.remove(slot);
    }

    public boolean isOpen(Player player) {
        Inventory topInventory = player.getOpenInventory().getTopInventory();
        Inventory menInventory = getInventory();

        return topInventory.equals(menInventory);
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}

