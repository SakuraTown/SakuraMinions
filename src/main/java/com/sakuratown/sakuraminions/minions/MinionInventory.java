package com.sakuratown.sakuraminions.minions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MinionInventory implements InventoryHolder {

    private final ArrayList<Inventory> inventoryList = new ArrayList<>();
    private final String type;
    private ArrayList<MenuButton> menuButtons;
    private int row;
    private int maxPage;
    private HashMap<String, Integer> playerLookingPage;

    public MinionInventory(String type, int row) {
        this.row = row;
        this.type = type;
        playerLookingPage = new HashMap<>();
        maxPage = countMaxPage();
        initInventory();
    }

    public static int getFreeSpace(Inventory inventory, ItemStack itemStack) {

        int amount = 0;
        int maxStackSize = itemStack.getMaxStackSize();
        for (ItemStack invStack : inventory.getStorageContents()) {
            if (invStack == null) {
                amount += maxStackSize;
            } else if (invStack.isSimilar(itemStack)) {
                amount += (maxStackSize - invStack.getAmount());
            }
        }

        return amount;
    }

    public void addRow(int row) {
        if (row <= 0) {
            return;
        }

        this.row += row;

        int lastPage = inventoryList.size() - 1;
        ItemStack[] contents = getPageContents(lastPage);
        inventoryList.remove(lastPage);

        initInventory();

        inventoryList.get(lastPage).setContents(contents);
    }

    private void initInventory() {

        maxPage = countMaxPage();

        // 如果新增行数后, 只初始化后面的界面, 不会重新初始化前面的界面
        for (int i = inventoryList.size(); i < maxPage; i++) {

            int page = inventoryList.size() + 1;

            Inventory inventory;

            if (maxPage == 1) {

                inventory = Bukkit.createInventory(this, row * 9, type + ":" + 1);

            } else if (page == maxPage) {

                int lastPageBlankRow = row % 5 == 0 ? 5 : row % 5;
                int lastPageRow = lastPageBlankRow + 1;
                inventory = Bukkit.createInventory(this, lastPageRow * 9, type + ":" + page);

            } else {

                inventory = Bukkit.createInventory(this, 54, type + ":" + page);

            }

            inventoryList.add(inventory);
        }

        menuButtons = MenuButton.initMenuButton();
        addMenuButton();
    }

    private int countMaxPage() {
        if (row <= 6) {
            return 1;
        } else {
            return row % 5 == 0 ? (row / 5) : (row / 5 + 1);
        }
    }

    private ItemStack[] getPageContents(int page) {
        Inventory inventory = inventoryList.get(page);
        ItemStack[] contents = inventory.getContents();
        for (int i = 0; i < contents.length; i++) {
            ItemStack itemStack = contents[i];
            if (isButton(itemStack)) {
                contents[i] = null;
            }
        }
        return contents;
    }

    private boolean isButton(ItemStack itemStack) {

        if (itemStack == null) return false;

        ItemMeta itemMeta = itemStack.getItemMeta();
        String[] buttons = {"LastPage", "NextPage", "PlaceHolder"};

        for (String button : buttons) {
            String name = Config.getMenuSection().getString(button + ".Name");
            if (itemMeta.getDisplayName().equals(name)) return true;

        }
        return false;
    }

    public int getPlayerPage(Player player, int num) { //获取玩家页数  num ：0为当前/上一次的页，正数+ 负数-
        String playerName = player.getName();
        if (!playerLookingPage.containsKey(playerName)) {//没看过的话设置第一页
            playerLookingPage.put(playerName, 1);
        }
        playerLookingPage.merge(playerName, num, Integer::sum);
        return playerLookingPage.get(playerName);
    }


    public void showInventoryGUI(int page, Player player) {
        Inventory inventory = inventoryList.get(page - 1);
        player.openInventory(inventory);
    }

    public void clearMenuButton(Inventory inv) {
        ItemStack[] itemStacks = inv.getContents();
        for (int i = 0; i < itemStacks.length; i++) {
            if (isButton(itemStacks[i])) {
                itemStacks[i] = null;
            }
        }
        inv.setContents(itemStacks);
    }

    //TODO 这里应该接受一个页数参数, 来给指定页数添加按钮
    public void addMenuButton() { //有问题 有什么问题??
        if (maxPage == 1) {
            return;
        }
        ArrayList<MenuButton> menuButtonsFirst = new ArrayList<>(menuButtons);
        ArrayList<MenuButton> menuButtonsEnd = new ArrayList<>(menuButtons);
        String style = Config.getMenuStyle();
        final int x = style.equals("Bottom") ? 45 : 0;//底部或顶部 此值决定了按钮的位置（slot）
        Inventory[] inventories = new Inventory[maxPage];
        for (int i = 1; i < maxPage; i++) {
            inventories[i - 1] = inventoryList.get(i - 1);
            if (i == 1) {
                for (int n = 0; n < menuButtonsFirst.size(); n++) {
                    MenuButton button = menuButtonsFirst.get(n);
                    if (button.getType().equals("LastPage")) {
                        menuButtonsFirst.set(n, new MenuButton("PlaceHolder", button.getSlot()));
                    }
                }
                for (MenuButton button : menuButtonsFirst) {
                    inventories[i - 1].setItem(x + button.getSlot() - 1, button.getItemStack());
                }
            } else {
                for (MenuButton button : menuButtons) {
                    inventories[i - 1].setItem(x + button.getSlot() - 1, button.getItemStack());
                }
            }
            inventoryList.set(i - 1, inventories[i - 1]);
        }

        inventories[maxPage - 1] = inventoryList.get(maxPage - 1);
        for (int n = 0; n < menuButtonsEnd.size(); n++) {
            MenuButton button = menuButtonsEnd.get(n);
            if (button.getType().equals("NextPage")) {
                menuButtonsEnd.set(n, new MenuButton("PlaceHolder", button.getSlot()));
            }
        }
        for (MenuButton button : menuButtonsEnd) {
            if (x == 45) {
                inventories[maxPage - 1].setItem(inventories[maxPage - 1].getSize() - 10 + button.getSlot(), button.getItemStack());
            } else {
                inventories[maxPage - 1].setItem(button.getSlot() - 1, button.getItemStack());
            }
        }
        inventoryList.set(maxPage - 1, inventories[maxPage - 1]);

    }

    public boolean addItem(ArrayList<ItemStack> itemList) { //往背包塞物品
        if (itemList.isEmpty()) {
            return false;
        }
        if (itemList.size() > (row * 9)) {
            return false;
        }
        for (ItemStack item : itemList) { //填充物品 从第一个物品开始
            addItem(item);
        }
        return true;
    }

    public boolean addItem(ItemStack item) {
        Map<Integer, ItemStack> tempItemMap;
        ItemStack tempItemStack = null;
        for (Inventory inv : inventoryList) {
            if (tempItemStack != null) {
                tempItemMap = inv.addItem(tempItemStack);
                if (tempItemMap.isEmpty()) {
                    return true;
                }
                tempItemStack = tempItemMap.get(0);
                continue;
            }
            tempItemMap = inv.addItem(item);
            if (tempItemMap.isEmpty()) {
                return true;
            }
            tempItemStack = tempItemMap.get(0);
        }
        return false;
    }

    public void sortItems() {
        ArrayList<Inventory> inventories = new ArrayList<>(inventoryList);
        ArrayList<ItemStack> itemStackList = getAllItems(inventories);
        if (itemStackList.isEmpty()) {
            addMenuButton();
            return;
        }
        ArrayList<ItemStack> tempItemStackList = new ArrayList<>();
        Iterator<ItemStack> iter = itemStackList.iterator();
        while (iter.hasNext()) {
            ItemStack tempItem1 = iter.next();
            tempItemStackList.add(tempItem1);
            iter.remove();
            while (iter.hasNext()) {
                ItemStack tempItem2 = iter.next();
                if (tempItem2.isSimilar(tempItem1)) {
                    tempItemStackList.add(tempItem2);
                    iter.remove();
                }
            }
            iter = itemStackList.iterator();
        }
        clearItems();
        addItem(tempItemStackList);
    }

//    public HashMap<String, Integer> getItemList() {//统计所有的物品到Hashmap todo:待测试
//        ArrayList<Inventory> inventories = new ArrayList<>(inventoryList);
//        HashMap<String, Integer> itemList = new HashMap<>();
//        for (Inventory inventory : inventories) {
//            clearMenuButton(inventory);
//            if (inventory.isEmpty()) {
//                continue;
//            }
//            ItemStack[] items = inventory.getContents();
//            for (ItemStack item : items) {
//                if (item == null) {//过滤null
//                    continue;
//                }
//                String itemName = item.getType().name();
//                if (!itemList.containsKey(itemName)) {
//                    itemList.put(itemName, item.getAmount());
//                } else {
//                    itemList.put(itemName, itemList.get(itemName) + item.getAmount());
//                }
//
//            }
//
//        }
//        return itemList;
//    }

    public ArrayList<ItemStack> getAllItems(ArrayList<Inventory> inventories) {
        ArrayList<ItemStack> itemStackList = new ArrayList<>();
        for (Inventory inventory : inventories) {
            clearMenuButton(inventory);
        }
        for (Inventory inventory : inventories) { //获取全部物品储存到itemStackList
            ItemStack[] items = inventory.getContents();
            for (ItemStack item : items) {
                if (item != null) { //过滤null
                    itemStackList.add(item);
                }
            }
        }
        return itemStackList;
    }

    public void clearItems() {
        for (Inventory inv : inventoryList) {
            inv.clear();
        }
        addMenuButton();
    }

    public ArrayList<Inventory> getInventoryList() {
        return inventoryList;
    }

    @Override
    public Inventory getInventory() {// 只返回第一个
        return inventoryList.get(0);
    }

}
