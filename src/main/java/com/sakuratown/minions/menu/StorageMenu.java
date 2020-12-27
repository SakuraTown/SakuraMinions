package com.sakuratown.minions.menu;

import com.sakuratown.library.menu.Button;
import com.sakuratown.library.menu.PageableMenu;
import com.sakuratown.library.utils.Config;
import com.sakuratown.minions.minion.Minion;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.Map;

public class StorageMenu extends PageableMenu {

    //TODO 添加红色玻璃板来控制仓库大小, 现在如果设置 7 行, 实际有 10 行大小,
    public StorageMenu(int storage, Minion minion) {
        isLock = false;
        new Config("menu").setPageMenu("工人仓库", this, storage);

        closeEvent = event -> {
            minion.collectItem();
            sortItem();
        };
    }

    public boolean addItem(Map<Material, Integer> collectItems) {

        for (Map.Entry<Material, Integer> entry : collectItems.entrySet()) {

            ItemStack itemStack = new ItemStack(entry.getKey(), entry.getValue());

            for (Inventory inventory : pages) {
                HashMap<Integer, ItemStack> failedItems = inventory.addItem(itemStack);
                if (failedItems.isEmpty()) break;

                //TODO 返回工人仓库是否已满, 但是现在无法等到已满才返回, 总有几个物品无法堆叠到 64 个就提示已满
                boolean isFull = pages.get(pages.size() - 1).equals(inventory);
                if (isFull) return true;
            }
        }
        return false;
    }

    public void sortItem() {
        HashMap<Material, Integer> allItemCount = getAllItemCount();
        clearAllItems();
        addItem(allItemCount);
    }

    public HashMap<Material, Integer> getAllItemCount() {

        HashMap<Material, Integer> itemCount = new HashMap<>();

        for (Inventory inventory : pages) {
            HashMap<Material, Integer> menuItemCount = getMenuItemCount(inventory);
            menuItemCount.forEach((k, v) -> itemCount.merge(k, v, Integer::sum));
        }

        return itemCount;
    }

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

    public void clearAllItems() {
        for (Inventory inventory : pages) {
            clearPageItems(inventory);
        }
    }

    // 清理页面上除了按钮以外的物品
    public void clearPageItems(Inventory inventory) {

        ItemStack[] itemStacks = inventory.getContents();

        for (int i = 0; i < itemStacks.length; i++) {
            if (!isButton(i)) {
                itemStacks[i] = null;
            }
        }
        inventory.setContents(itemStacks);
    }

    // 将 inv 中的物品取到 target 中
    private void takeOutItem(Inventory inventory, Inventory target) {
        HashMap<Material, Integer> itemCount = getMenuItemCount(inventory);
        clearPageItems(inventory);

        itemCount.forEach((k, v) -> {
            ItemStack itemStack = new ItemStack(k, v);
            HashMap<Integer, ItemStack> failedItems = target.addItem(itemStack);

            if (!failedItems.isEmpty()) {
                for (ItemStack value : failedItems.values()) {
                    inventory.addItem(value);
                }
            }
        });
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
                    Inventory inventory = event.getInventory();
                    PlayerInventory target = event.getWhoClicked().getInventory();
                    takeOutItem(inventory, target);
                };

                break;
        }
    }
}
