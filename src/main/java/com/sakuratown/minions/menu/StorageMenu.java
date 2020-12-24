package com.sakuratown.minions.menu;

import com.sakuratown.library.menu.Button;
import com.sakuratown.library.menu.PageableMenu;
import com.sakuratown.library.utils.Config;
import org.bukkit.inventory.InventoryHolder;

public class StorageMenu extends PageableMenu {

    public StorageMenu(int storage) {
        Config.getConfig("menu").setPageMenu("工人仓库", this, storage);
        openEvent = event -> System.out.println("开启菜单事件");
        closeEvent = event -> System.out.println("关闭菜单事件");
    }

//    public void addItem(Map<Material, Integer> collectItems) {
//        collectItems.forEach((k, v) -> {
//
//            ItemStack itemStack = new ItemStack(k, v);
//
//
//
//            for (Menu menu : menus) {
//                HashMap<Integer, ItemStack> failedItems = inventory.addItem(itemStack);
//                if (failedItems.isEmpty()) return;
//            }
//        });
//    }
//
//    public void sortItem() {
//        HashMap<Material, Integer> allItemCount = getAllItemCount();
//        clearAllItems();
//        addItem(allItemCount);
//    }
//
//    public HashMap<Material, Integer> getAllItemCount() {
//
//        HashMap<Material, Integer> itemCount = new HashMap<>();
//
//        for (Menu menu : menus) {
//            HashMap<Material, Integer> menuItemCount = getMenuItemCount(menu);
//            menuItemCount.forEach((k, v) -> {
//                itemCount.merge(k, v, Integer::sum);
//            });
//        }
//
//        return itemCount;
//    }
//
//    private HashMap<Material, Integer> getMenuItemCount(Menu menu) {
//
//        HashMap<Material, Integer> itemCount = new HashMap<>();
//
//        Inventory inventory = menu.getInventory();
//        ItemStack[] contents = inventory.getContents();
//
//        for (int i = 0; i < contents.length; i++) {
//            ItemStack itemStack = contents[i];
//
//            if (itemStack == null || menu.isButton(i)) {
//                continue;
//            }
//
//            itemCount.merge(itemStack.getType(), itemStack.getAmount(), Integer::sum);
//        }
//
//        return itemCount;
//    }
//
//    public void clearAllItems() {
//
//        for (Menu menu : menus) {
//            ItemStack[] itemStacks = menu.getInventory().getContents();
//
//            for (int i = 0; i < itemStacks.length; i++) {
//                if (!menu.isButton(i)) {
//                    itemStacks[i] = null;
//                }
//            }
//            menu.getInventory().setContents(itemStacks);
//        }
//    }
//
//    private void takeOutItem() {
//        //TODO 把当前界面物品取到玩家背包
//    }
    @Override
    public void setButtonAction(Button button) {

        switch (button.action) {

            case "SortItem":

                button.clickEvent = event -> {
//                    sortItem();
                };

                break;

            case "TakeOutItem":
                //TODO
                break;
        }
    }

//
//    @Override
//    public Inventory getInventory() {
//        return menus.get(0).getInventory();
//    }
}
