package com.sakuratown.library.menu;


import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class PageableMenu implements Iterable<Menu> {


    public List<Menu> menus = new ArrayList<>();
    private int currentPage = 1;
    private int maxPage;

    public int getMaxPage() {
        return maxPage;
    }

    private void setMaxPage(List<Button> buttons) {

        int pageSize = buttons.get(0).slots.length;
        int buttonSize = buttons.size();

        int maxPage = buttonSize / pageSize;

        if (buttonSize % pageSize != 0) {
            maxPage += 1;
        }

        this.maxPage = maxPage;
    }

    protected void setMaxPage(int row) {
        if (row <= 6) {
            maxPage = 1;
        } else {
            maxPage = row % 5 == 0 ? (row / 5) : (row / 5 + 1);
        }
    }

    public void initMenu() {

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

    @Override
    public Iterator<Menu> iterator() {
        return menus.iterator();
    }
}
