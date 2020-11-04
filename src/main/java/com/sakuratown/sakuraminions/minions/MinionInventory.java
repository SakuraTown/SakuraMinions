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

    private ArrayList<MenuButton> menuButtons;
    private final ArrayList<Inventory> inventoryList;
    private Inventory currentInventory;
    private String type;
    private int row;
    private int maxPage;
    private int nowPage = 1;

    public MinionInventory(String type, int row) {
        this.row = row;
        this.type = type;
        setMaxPage();
        inventoryList = new ArrayList<>();
        initInventory();
    }

    public static int getFreeSpace(Inventory inventory, ItemStack itemStack) {

        int amount = 0;

        for (ItemStack invStack : inventory.getStorageContents()) {
            if (invStack == null) {
                amount += itemStack.getMaxStackSize();
            } else if (invStack.isSimilar(itemStack)) {
                amount += invStack.getMaxStackSize() - invStack.getAmount();
            }
        }

        return amount;
    }

    public void addRow(int row) {
        if (row <= 0) {
            return;
        }
        this.row += row;

        setMaxPage();
        addInventory(row);
    }

    private void setMaxPage() {
        if (this.row <= 6) {
            maxPage = 1;
        } else {
            maxPage = this.row % 5 == 0 ? (this.row / 5) : (this.row / 5 + 1);
        }
    }

    private void addInventory(int extra) { //成功返回true
        int oldRow = row - extra;
        int oldPage;
        int endInvSurplus;//最后一页的剩余(容器)排数
        if (oldRow <= 6) {
            oldPage = 1;
            endInvSurplus = 6 - oldRow;
        } else {
            if (oldRow % 5 == 0) {
                oldPage = oldRow / 5;
                endInvSurplus = 0;
            } else {
                oldPage = oldRow / 5 + 1;
                endInvSurplus = 5 - oldRow % 5;
            }
        }
        if (extra <= endInvSurplus) {
            Inventory invTemp = inventoryList.get(inventoryList.size() - 1);
            clearMenuButton(invTemp);
            ItemStack[] itemStacks = invTemp.getContents();
            Inventory inv = Bukkit.createInventory(this, invTemp.getSize() + extra * 9, type + ":" + oldPage);
            inv.setContents(itemStacks);
            inventoryList.set(inventoryList.size() - 1, inv);
            addMenuButton();
            currentInventory = inventoryList.get(0);
            return;
        } else {
            Inventory invTemp = inventoryList.get(inventoryList.size() - 1);
            clearMenuButton(invTemp);
            ItemStack[] itemStacks = invTemp.getContents();
            Inventory inv = Bukkit.createInventory(this, 54, type + ":" + oldPage);
            inv.setContents(itemStacks);
            inventoryList.set(inventoryList.size() - 1, inv);
            int extraRow = extra - endInvSurplus; //填完现有的最后一页还多的行数(不含菜单)
            int extraEndRow = (extra - endInvSurplus) % 5 == 0 ? 6 : (extra - endInvSurplus) % 5 + 1; //最后一页的行数(包括菜单1个)
            int extraPage = extraRow % 5 == 0 ? extraRow / 5 : extraRow / 5 + 1; //多增加的页数
            Inventory[] addInventories = new Inventory[extraPage];
            for (int i = 1; i < extraPage; i++) {
                addInventories[i - 1] = Bukkit.createInventory(this, 54, type + ":" + (oldPage + i));
                inventoryList.add(addInventories[i - 1]);
            }
            if (oldPage == 1 && extraPage == 1) {
                addInventories[0] = Bukkit.createInventory(this, (extraEndRow + 1) * 9, type + ":" + 2);
                inventoryList.add(addInventories[0]);
            } else {
                addInventories[extraPage - 1] = Bukkit.createInventory(this, extraEndRow * 9, type + ":" + (extraPage + oldPage));
                inventoryList.add(addInventories[extraPage - 1]);
            }
        }
        addMenuButton();
        currentInventory = inventoryList.get(0);
    }

    private void initInventory() {
        Inventory[] initInventory = new Inventory[maxPage];
        for (int i = 0; i < maxPage; i++) {
            if (i == (maxPage - 1)) {
                if (row >= 6) {
                    initInventory[i] = Bukkit.createInventory(this, (row % 6 + maxPage) * 9, type + ":" + (i + 1));
                } else {
                    initInventory[i] = Bukkit.createInventory(this, (row % 6) * 9, type + ":" + (i + 1));
                }
            } else {
                initInventory[i] = Bukkit.createInventory(this, 54, type + ":" + (i + 1));
            }
            inventoryList.add(initInventory[i]);
        }
        menuButtons = MenuButton.initMenuButton();
        addMenuButton();
        currentInventory = inventoryList.get(0);
    }

    public int getMaxPage() {
        return maxPage;
    }

    public int getNowPage() {
        return nowPage;
    }

    public void setNowPage(int page) {
        nowPage = page;
    }

    public void showInventoryGUI(int page, Player player) {
        Inventory inventory = inventoryList.get(page - 1);
        currentInventory = inventory;
        setNowPage(page);
        player.openInventory(inventory);
    }

    public Inventory getCurrentInventory() {
        return currentInventory;
    }

    public void setCurrentInventory(Inventory inv) {
        currentInventory = inv;
    }

    public void clearMenuButton(Inventory inv) {
        ItemStack[] itemStacks = inv.getContents();
        for (int i = 0; i < itemStacks.length; i++) {
            if (itemStacks[i] != null) {
                ItemMeta itemMeta = itemStacks[i].getItemMeta();
                String lName = Config.getMenuSection().getString("LastPage.Name");
                if (itemMeta.getDisplayName().equals(lName)) {
                    itemStacks[i] = null;
                }
                String nName = Config.getMenuSection().getString("NextPage.Name");
                if (itemMeta.getDisplayName().equals(nName)) {
                    itemStacks[i] = null;
                }
                String pName = Config.getMenuSection().getString("PlaceHolder.Name");
                if (itemMeta.getDisplayName().equals(pName)) {
                    itemStacks[i] = null;
                }
            }
        }
        inv.setContents(itemStacks);
    }

    public void addMenuButton() { //有问题
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
        Map<Integer, ItemStack> tempItemMap;
        ItemStack tempItemStack = null;
        if (itemList.size() > (row * 9)) {
            return false;
        }
        for (ItemStack item : itemList) { //填充物品 从第一个物品开始
            for (Inventory inv : inventoryList) {
                if (tempItemStack != null) {  //此处是在上一页物品填充有剩余的情况下尝试填充到新页
                    if (getFreeSpace(inv, tempItemStack) > 0) {//检查剩余空间
                        tempItemMap = inv.addItem(tempItemStack); //填充
                        if (!tempItemMap.isEmpty()) { //如果有剩余退出循环，把剩余填充到下一页
                            tempItemStack = tempItemMap.get(0);
                            break;
                        }
                        break;//没有剩余退出循环，避免填充到下一页
                    }
                }
                if (getFreeSpace(inv, item) > 0) { //当前物品在这个容器有空间
                    tempItemMap = inv.addItem(item); //填充
                    if (!tempItemMap.isEmpty()) { //如果有剩余 ，储存到暂存等待第二页的尝试
                        tempItemStack = tempItemMap.get(0);
                        break;
                    }
                    break;//没有剩余退出循环，避免填充到下一页
                }
            }
        }
        return true;
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

    public HashMap<String, Integer> getItemList() {//统计所有的物品到Hashmap todo:待测试
        ArrayList<Inventory> inventories = new ArrayList<>(inventoryList);
        HashMap<String, Integer> itemList = new HashMap<>();
        for (Inventory inventory : inventories) {
            clearMenuButton(inventory);
            if (inventory.isEmpty()) {
                continue;
            }
            ItemStack[] items = inventory.getContents();
            for (ItemStack item : items) {
                if (item == null) {//过滤null
                    continue;
                }
                String itemName = item.getType().name();
                if (!itemList.containsKey(itemName)) {
                    itemList.put(itemName, item.getAmount());
                } else {
                    itemList.put(itemName, itemList.get(itemName) + item.getAmount());
                }

            }

        }
        return itemList;
    }

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
