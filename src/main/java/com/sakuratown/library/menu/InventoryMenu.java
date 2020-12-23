package com.sakuratown.library.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;
import java.util.List;

public abstract class InventoryMenu extends Menu {

    private final List<Inventory> inventories = new LinkedList<>();
    private int currentPage = 1;
    private int maxPage;

    public InventoryMenu(Player player) {
        inventories.add(Bukkit.createInventory(this, 9, "第一页"));
        inventories.add(Bukkit.createInventory(this, 18, "第二页"));

        inventory = inventories.get(0);
        Button next = new Button(new ItemStack(Material.STONE), 8);
        Button previous = new Button(new ItemStack(Material.DIAMOND), 0);

        next.clickEvent = event -> nextPage(player);
        previous.clickEvent = event -> previousPage(player);

        setButton(next);
        setButton(previous);

    }

    public void previousPage(Player player) {
        inventory = inventories.get(0);
        open(player);
    }

    public void nextPage(Player player) {
        inventory = inventories.get(1);
        player.updateInventory();
//        open(player);

    }

    @Override
    public void setDefaultButtonAction(Button button) {

        if (button.action == null) return;

        switch (button.action) {

            case "NextPage":

                button.clickEvent = event -> {
                    HumanEntity whoClicked = event.getWhoClicked();
                    if (whoClicked instanceof Player) {
                        nextPage((Player) whoClicked);
                    }
                };

                if (currentPage == maxPage) {
                    ItemStack itemStack = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
                    Button nextButton = new Button(itemStack, button.slots);
                    setButton(nextButton);
                }

                break;

            case "PreviousPage":

                button.clickEvent = event -> {
                    HumanEntity whoClicked = event.getWhoClicked();
                    if (whoClicked instanceof Player) {
                        previousPage((Player) event.getWhoClicked());
                    }
                };

                if (currentPage == 1) {
                    ItemStack itemStack = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
                    Button previousButton = new Button(itemStack, button.slots);
                    setButton(previousButton);
                }

                break;

            case "Close":

                button.clickEvent = event -> event.getWhoClicked().closeInventory();
                break;
        }
    }


    public abstract void setButtonAction(Button button);
}
