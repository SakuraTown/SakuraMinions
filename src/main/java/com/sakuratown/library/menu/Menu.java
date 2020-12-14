package com.sakuratown.library.menu;


import com.sakuratown.library.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class Menu implements InventoryHolder {

    public final Map<Integer, Button> buttonMap = new HashMap<>();
    public boolean isLock = true;

    public Consumer<InventoryOpenEvent> openEvent;
    public Consumer<InventoryCloseEvent> closeEvent;

    public String title = "Menu";
    private Integer size = 3 * 9;
    public Inventory inventory = Bukkit.createInventory(this, size, title);

    public Button getButton(int slot) {
        return buttonMap.get(slot);
    }

    public void setTitle(String title) {
        this.title = Message.toColor(title);
        inventory = Bukkit.createInventory(this, size, this.title);
        setButton(buttonMap.values());
    }

    public void setRow(int row) {
        this.size = row * 9;
        inventory = Bukkit.createInventory(this, size, title);
        setButton(buttonMap.values());
    }

    public void setButton(Collection<Button> buttons) {
        for (Button button : buttons) {
            setButton(button);
        }
    }

    public void setButton(Button button) {

        if (button.action != null) {
            if (button.action.equals("Close")) {
                button.clickEvent = event -> event.getWhoClicked().closeInventory();
            } else {
                setButtonAction(button);
            }
        }

        for (int slot : button.slots) {
            buttonMap.put(slot, button);
            inventory.setItem(slot, button.itemStack);
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

    public boolean isButton(int slot) {
        return buttonMap.get(slot) != null;
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public abstract void setButtonAction(Button button);
}

