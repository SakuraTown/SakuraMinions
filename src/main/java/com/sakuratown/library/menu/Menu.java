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

    public boolean isLock = true;

    public Consumer<InventoryOpenEvent> openEvent;
    public Consumer<InventoryCloseEvent> closeEvent;
    private Integer size = 3 * 9;
    private String title = "Menu";

    protected Inventory inventory = Bukkit.createInventory(this, size, title);
    Map<Integer, Button> buttonMap = new HashMap<>();

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = Message.toColor(title);
        inventory = Bukkit.createInventory(this, size, this.title);
//        setButton(buttonMap.values());
    }

    public void setRow(int row) {
        size = row * 9;
        inventory = Bukkit.createInventory(this, size, title);
        setButton(buttonMap.values());
    }

    public Button getButton(int slot) {
        return buttonMap.get(slot);
    }

    public void setButton(Collection<Button> buttons) {
        for (Button button : buttons) {
            setButton(button);
        }
    }

    public void setButton(Button button) {

        for (int slot : button.slots) {
            buttonMap.put(slot, button);
            inventory.setItem(slot, button.itemStack);
        }

        if (button.action != null) {
            setDefaultButtonAction(button);
            setButtonAction(button);
        }
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void setDefaultButtonAction(Button button) {
        if (button.action.equals("Close")) {
            button.clickEvent = event -> event.getWhoClicked().closeInventory();
        }
    }

    public abstract void setButtonAction(Button button);

    public int getSize() {
        return size;
    }

    public int getRow() {
        return size / 9;
    }
}

