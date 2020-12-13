package com.sakuratown.library.menu;


import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class PageableMenu implements Iterable<Menu> {


    private final int maxPage;
    public List<Menu> menus = new ArrayList<>();
    private int currentPage = 1;

    PageableMenu(List<Button> buttons) {
        int pageSize = buttons.get(0).slots.length;
        int buttonSize = buttons.size();

        int maxPage = buttonSize / pageSize;

        if (buttonSize % pageSize != 0) {
            maxPage += 1;
        }

        this.maxPage = maxPage;
    }

    protected PageableMenu(int row) {
        if (row <= 6) {
            maxPage = 1;
        } else {
            maxPage = row % 5 == 0 ? (row / 5) : (row / 5 + 1);
        }
    }

    public int getMaxPage() {
        return maxPage;
    }

    public void setLock(boolean lock) {
        for (Menu menu : menus) {
            menu.isLock = lock;
        }
    }

    public void open(Player player) {
        menus.get(0).open(player);
    }

    public void previousPage(Player player) {
        if (currentPage == 1) return;

        int index = --currentPage;
        menus.get(index - 1).open(player);
    }

    public void nextPage(Player player) {
        if (currentPage == maxPage) return;
        menus.get(currentPage++).open(player);
    }

    // 设置按钮的默认行为
    public void setDefaultAction(Button button) {
        switch (button.action) {

            case "NextPage":

                button.clickEvent = event -> {
                    HumanEntity whoClicked = event.getWhoClicked();
                    if (whoClicked instanceof Player) {
                        nextPage((Player) whoClicked);
                    }
                };

                break;

            case "PreviousPage":

                button.clickEvent = event -> {
                    HumanEntity whoClicked = event.getWhoClicked();
                    if (whoClicked instanceof Player) {
                        previousPage((Player) event.getWhoClicked());
                    }
                };

                if (currentPage == 1) {
                    button.itemStack = null;
                }

                break;

            case "Close":

                button.clickEvent = event -> event.getWhoClicked().closeInventory();
                break;
        }
    }

    @Override
    public Iterator<Menu> iterator() {
        return menus.iterator();
    }
}
