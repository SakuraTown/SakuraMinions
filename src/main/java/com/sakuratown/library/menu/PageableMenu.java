package com.sakuratown.library.menu;


import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class PageableMenu extends Menu implements InventoryHolder {

    public final Map<Integer, Map<Integer, Button>> pages = new HashMap<>();
    private int currentPage = 1;
    private int maxPage;

    public int getMaxPage() {
        return maxPage;
    }

    // 根据拥有行数设定最大页数
    public void setMaxPage(int storage) {

        int size = getSize();
        maxPage = (storage * 9 == size ? 0 : storage / size) + 1;
        for (int i = 0; i < maxPage; i++) {
            pages.put(i, new HashMap<>());
        }
    }

    // 通过按钮数量计算最大页数
    public void setMaxPage(List<Button> buttons) {
        setMaxPage(buttons.size());
    }

    // 统一设置所有界面的按钮
    public void setAllButton(Button button) {

        for (int i = 0; i < maxPage; i++) {

            for (int slot : button.slots) {
                pages.get(i).put(slot, button);
                inventory.setItem(slot, button.itemStack);
            }

            if (button.action != null) {
                setDefaultButtonAction(button);
                setButtonAction(button);
            }
        }
    }

    @Override
    public void open(Player player) {
        String title = getTitle();
        setTitle(title);

        buttonMap = pages.get(0);
        setButton(buttonMap.values());
        player.openInventory(inventory);
    }

    public void previousPage(Player player) {
        if (currentPage == 1) return;

        int index = currentPage - 1;
        Map<Integer, Button> buttons = pages.get(index - 1);
        setTitle(getTitle().replace(currentPage + "/" + maxPage, currentPage - 1 + "/" + maxPage));

        currentPage -= 1;
        setButton(buttons.values());
        open(player);
    }

    public void nextPage(Player player) {
        if (currentPage == maxPage) return;

        int index = currentPage - 1;
        Map<Integer, Button> buttons = pages.get(index + 1);

        setTitle(getTitle().replace(currentPage + "/" + maxPage, currentPage + 1 + "/" + maxPage));
        currentPage += 1;
        setButton(buttons.values());
        open(player);
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
                    setButton(new Button(new ItemStack(Material.BLUE_STAINED_GLASS_PANE), button.slots));
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
                    setButton(new Button(new ItemStack(Material.BLUE_STAINED_GLASS_PANE), button.slots));
                }

                break;

            case "Close":

                button.clickEvent = event -> event.getWhoClicked().closeInventory();
                break;
        }
    }

    public abstract void setButtonAction(Button button);
}
