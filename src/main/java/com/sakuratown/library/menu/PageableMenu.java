package com.sakuratown.library.menu;


import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public abstract class PageableMenu extends Menu implements Iterable<Menu>, InventoryHolder {

    private final List<Menu> pages = new ArrayList<>();
    private int currentPage = 1;
    private int maxPage = 1;

    public void initPages() {

        for (int i = 0; i < maxPage; i++) {
            int page = i + 1;

            Menu menu = new Menu() {
                @Override
                public void setButtonAction(Button button) {
                    PageableMenu.this.setDefaultButtonAction(button, page);
                }
            };

            menu.setTitle(getTitle().concat(" " + page + "/" + maxPage));
            menu.setRow(getRow());
            menu.isLock = isLock;

            pages.add(menu);
        }
    }

    public void previousPage(Player player) {

        if (currentPage == 1) return;

        int index = currentPage - 1;
        currentPage--;

        Menu menu = pages.get(index - 1);
        menu.open(player);
    }

    public void nextPage(Player player) {
        if (currentPage == maxPage) return;

        int index = currentPage - 1;
        currentPage++;

        Menu menu = pages.get(index + 1);
        menu.open(player);
    }

    public int getMaxPage() {
        return maxPage;
    }

    public void setTotalRow(int row) {
        int pageSize = getSize();
        int totalSize = row * 9;
        maxPage = (totalSize == pageSize ? 0 : totalSize / pageSize) + 1;
    }

    // 通过按钮数量计算最大页数
    public void setTotalRow(List<Button> buttons) {
        setTotalRow(buttons.size());
    }

    @Override
    public void open(Player player) {
        currentPage = 1;

        Menu menu = pages.get(0);
        menu.open(player);
    }

    @Override
    public void setButton(Button button) {
        for (Menu menu : pages) {
            menu.setButton(button);
        }
    }

    @Override
    public Iterator<Menu> iterator() {
        return pages.iterator();
    }

    public void setDefaultButtonAction(Button button, int page) {

        if (button.action == null) return;

        switch (button.action) {

            case "NextPage":

                button.clickEvent = event -> {
                    HumanEntity whoClicked = event.getWhoClicked();
                    if (whoClicked instanceof Player) {
                        nextPage((Player) whoClicked);
                    }
                };

                if (page == maxPage) {
                    ItemStack itemStack = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
                    pages.get(maxPage - 1).setButton(new Button(itemStack, button.slots));
                }

                break;

            case "PreviousPage":

                button.clickEvent = event -> {
                    HumanEntity whoClicked = event.getWhoClicked();
                    if (whoClicked instanceof Player) {
                        previousPage((Player) whoClicked);
                    }
                };

                if (page == 1) {
                    ItemStack itemStack = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
                    pages.get(0).setButton(new Button(itemStack, button.slots));

                }

                break;

            case "Close":

                button.clickEvent = event -> event.getWhoClicked().closeInventory();
                break;
        }
    }
}
