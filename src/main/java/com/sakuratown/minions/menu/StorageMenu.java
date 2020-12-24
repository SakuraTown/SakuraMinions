package com.sakuratown.minions.menu;

import com.sakuratown.library.menu.Button;
import com.sakuratown.library.menu.PageableMenu;
import com.sakuratown.library.utils.Config;
import com.sakuratown.minions.minion.Minion;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class StorageMenu extends PageableMenu {

    public StorageMenu(int storage, Minion minion) {
        isLock = false;
        Config.getConfig("menu").setPageMenu("工人仓库", this, storage);
        closeEvent = event -> minion.collectItem();
    }

    // 返回背包是否已满
    public boolean addItem(Map<Material, Integer> collectItems) {

        for (Map.Entry<Material, Integer> entry : collectItems.entrySet()) {

            ItemStack itemStack = new ItemStack(entry.getKey(), entry.getValue());

            for (Inventory inventory : pages) {
                HashMap<Integer, ItemStack> failedItems = inventory.addItem(itemStack);
                if (failedItems.isEmpty()) continue;

                boolean isFull = pages.get(pages.size() - 1).equals(inventory);
                if (isFull) return true;
            }
        }
        return false;
    }

    // 排序物品
    public void sortItem() {
        HashMap<Material, Integer> allItemCount = getAllItemCount();
        clearAllItems();
        addItem(allItemCount);
    }

    // 获取所有界面的物品
    public HashMap<Material, Integer> getAllItemCount() {

        HashMap<Material, Integer> itemCount = new HashMap<>();

        for (Inventory inventory : pages) {
            HashMap<Material, Integer> menuItemCount = getMenuItemCount(inventory);
            menuItemCount.forEach((k, v) -> itemCount.merge(k, v, Integer::sum));
        }

        return itemCount;
    }

    // 获取 inv 中的除按钮以外的物品并计算数量总和
    private HashMap<Material, Integer> getMenuItemCount(Inventory inventory) {

        HashMap<Material, Integer> itemCount = new HashMap<>();

        ItemStack[] contents = inventory.getContents();

        for (int i = 0; i < contents.length; i++) {
            ItemStack itemStack = contents[i];

            if (itemStack == null || isButton(i)) {
                continue;
            }

            itemCount.merge(itemStack.getType(), itemStack.getAmount(), Integer::sum);
        }

        return itemCount;
    }

    // 清理除按钮以外的所有物品
    public void clearAllItems() {

        for (Inventory inventory : pages) {
            ItemStack[] itemStacks = inventory.getContents();

            for (int i = 0; i < itemStacks.length; i++) {
                if (!isButton(i)) {
                    itemStacks[i] = null;
                }
            }
            inventory.setContents(itemStacks);
        }
    }

    private void takeOutItem() {
        //TODO 把当前界面物品取到玩家背包
    }

    @Override
    public void setButtonAction(Button button) {

        switch (button.action) {

            case "SortItem":

                button.clickEvent = event -> {
                    sortItem();
                };

                break;

            case "TakeOutItem":

                button.clickEvent = event -> {
                    takeOutItem();
                };

                break;
        }
    }
}
