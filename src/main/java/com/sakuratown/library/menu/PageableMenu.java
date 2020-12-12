package com.sakuratown.library.menu;


import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class PageableMenu implements Iterable<Menu> {

    //TODO 这里要设置默认的按钮行为, 下一页, 上一页
    public List<Menu> menus = new ArrayList<>();
    private int currentPage = 1;

    public PageableMenu() {

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
        menus.get(currentPage -= 1).open(player);
    }

    public void nextPage(Player player) {
        menus.get(currentPage += 1).open(player);
    }

    private int getMaxPage(List<Button> buttons) {

        int pageSize = buttons.get(0).slots.length;
        int buttonSize = buttons.size();

        int maxPage = buttonSize / pageSize;

        if (buttonSize % pageSize != 0) {
            maxPage += 1;
        }

        return maxPage;
    }

    protected int getMaxPage(int row) {
        if (row <= 6) {
            return 1;
        } else {
            return row % 5 == 0 ? (row / 5) : (row / 5 + 1);
        }
    }

    @Override
    public Iterator<Menu> iterator() {
        return menus.iterator();
    }
}
