package com.sakuratown.sakuralibrary.menu;


import com.sakuratown.sakuralibrary.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Gui implements InventoryHolder {

    public boolean isLock = true;

    public Consumer<InventoryOpenEvent> openEvent;
    public Consumer<InventoryCloseEvent> closeEvent;

    private final Inventory inventory;
    private final Map<Integer, Button> buttonMap = new HashMap<>();

    public Gui(String name, int row) {
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
        Inventory guiInventory = getInventory();

        return topInventory.equals(guiInventory);
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}

