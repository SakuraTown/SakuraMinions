package com.sakuratown.library.menu;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class PageableMenu extends Menu implements Iterable<Menu>, InventoryHolder {

    private final int maxPage;
    //TODO 翻页菜单也是一种菜单, 你不能打开一个 list 里的菜单, 而是你通过计算获取下一页的物品
    public List<Inventory> menus = new ArrayList<>();
    private int currentPage = 1;
//    public Inventory inventory;

    // 根据按钮数量设置最大页数
    PageableMenu(List<Button> buttons) {
        int pageSize = buttons.get(0).slots.length;
        int buttonSize = buttons.size();

        int maxPage = buttonSize / pageSize;

        if (buttonSize % pageSize != 0) {
            maxPage += 1;
        }

        this.maxPage = maxPage;
    }

    // 根据行数来判断最大页数
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

    public void open(Player player) {
        menus.get(0).open(player);
    }

    public void previousPage(Player player) {
        if (currentPage == 1) return;

        int index = --currentPage;
        player.openInventory(inventory);
        menus.get(index - 1).open(player);
    }

    public void nextPage(Player player) {
        if (currentPage == maxPage) return;
        menus.get(currentPage++).open(player);
    }

    // 设置按钮的默认行为
    public void setDefaultAction(Button button, int page) {

        switch (button.action) {

            case "NextPage":

                button.clickEvent = event -> {
                    HumanEntity whoClicked = event.getWhoClicked();
                    if (whoClicked instanceof Player) {
                        nextPage((Player) whoClicked);
                    }
                };

                if (page == maxPage) {
                    button.itemStack = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
                }
                break;

            case "PreviousPage":

                button.clickEvent = event -> {
                    HumanEntity whoClicked = event.getWhoClicked();
                    if (whoClicked instanceof Player) {
                        previousPage((Player) event.getWhoClicked());
                    }
                };

                if (page == 1) {
                    button.itemStack = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
                }

                break;

            case "Close":

                button.clickEvent = event -> event.getWhoClicked().closeInventory();
                break;
        }

        setButtonAction(button);
    }

    public abstract void setButtonAction(Button button);

    @Override
    public Iterator<Menu> iterator() {
        return menus.iterator();
    }

    @Override
    public Inventory getInventory() {
        return super.getInventory();
    }
}