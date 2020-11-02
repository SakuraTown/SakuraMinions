package com.sakuratown.sakuraminions.minions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Map;

public class MinionInventory implements InventoryHolder {

    private ArrayList<MenuButton> menuButtons;
    private ArrayList<Inventory> inventoryList;
    private Inventory currentInventory;
    private String type;
    private int row;
    private int page;
    private int nowPage = 1;


    public MinionInventory(String type, int row) {
        this.row = row;
        this.type = type;
        if (row == 6) {
            page = 1;
        } else {
            page = (row / 6) + 1; //容器理论页数（加菜单）
        }
        if (row <= 6) {
            page = 1;
        } else if (row <= 10) {
            page = 2;
        } else {
            if (row % 5 == 0) {
                page = row / 5;
            } else {
                page = row / 5 + 1;
            }
        }
        inventoryList = new ArrayList<>();
        initInventory();
    }

    public boolean addRow(int row) {
        if (row <= 0) {
            return false;
        }
        this.row += row;
        if (this.row <= 6) {
            page = 1;
        } else {
            page = this.row % 5 == 0 ? (this.row / 5) : (this.row / 5 + 1);
        }
        return addInventory(row);
    }

    private boolean addInventory(int extra) { //成功返回true
        int oldRow = row - extra;
        int oldPage;
        int endInvSurplus;//最后一页的剩余(容器)排数

        if (oldRow <= 6) {
            oldPage = 1;
            endInvSurplus = 6 - oldRow;
        } else if (oldRow <= 10) {
            oldPage = 2;
            endInvSurplus = 6 - ((oldRow % 6) + 2);
        } else {
            if (oldRow % 5 == 0) {
                oldPage = oldRow / 5;
                endInvSurplus = 0;
            } else {
                oldPage = oldRow / 5 + 1;
                endInvSurplus = 6 - ((oldRow % 5) + 1);
            }
        }
        if (extra <= endInvSurplus) {
            Inventory invTemp = inventoryList.get(inventoryList.size() - 1);
            clearMenuButton(invTemp);
            ItemStack[] itemStacks = invTemp.getContents();
            Inventory inv = Bukkit.createInventory(this, invTemp.getSize() + extra * 9, type + ":" + oldPage);
            inv.setContents(itemStacks);
            inventoryList.set(inventoryList.size() - 1, inv);
            addMenu(inventoryList, menuButtons);
            currentInventory = inventoryList.get(0);
            return true;
        } else {
            Inventory invTemp = inventoryList.get(inventoryList.size() - 1);
            clearMenuButton(invTemp);
            ItemStack[] itemStacks = invTemp.getContents();
            Inventory inv = Bukkit.createInventory(this, 54, type + ":" + oldPage);
            inv.setContents(itemStacks);
            inventoryList.set(inventoryList.size() - 1, inv);
            int extraEndRow = extra - endInvSurplus;
            int extraPage;
            if (extraEndRow % 5 == 0) {
                extraPage = extraEndRow / 5;
            } else {
                extraPage = extraEndRow / 5 + 1;
            }
            Inventory[] addInventories = new Inventory[extraPage];
            for (int i = 1; i < extraPage; i++) {
                addInventories[i - 1] = Bukkit.createInventory(this, 54, type + ":" + (oldPage + i));
                inventoryList.add(addInventories[i - 1]);
            }
            if (oldPage == 1 && extraPage == 1) {
                addInventories[0] = Bukkit.createInventory(this, (extraEndRow % 5 + 2) * 9, type + ":" + extraPage);

            } else {
                addInventories[extraPage - 1] = Bukkit.createInventory(this, (extraEndRow % 5 + 1) * 9, type + ":" + extraPage);
            }
            inventoryList.add(addInventories[extraPage - 1]);
        }
        addMenu(inventoryList, menuButtons);
        currentInventory = inventoryList.get(0);
        return true;
    }

    private void initInventory() {

        Inventory[] initInventory = new Inventory[page];
        for (int i = 0; i < page; i++) {
            if (i == (page - 1)) {
                if (row >= 6) {
                    initInventory[i] = Bukkit.createInventory(this, (row % 6 + page) * 9, type + ":" + (i + 1));
                } else {
                    initInventory[i] = Bukkit.createInventory(this, (row % 6) * 9, type + ":" + (i + 1));
                }
            } else {
                initInventory[i] = Bukkit.createInventory(this, 54, type + ":" + (i + 1));
            }
            inventoryList.add(initInventory[i]);
        }
        menuButtons = MenuButton.initMenuButton();
        addMenu(inventoryList, menuButtons);
        currentInventory = inventoryList.get(0);
    }

    public int getPage() {
        return page;
    }

    public void setNowPage(int page) {
        nowPage = page;
    }

    public int getNowPage() {
        return nowPage;
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


    public void addMenu(ArrayList<Inventory> inventoryList, ArrayList<MenuButton> menuButtons) {
        int page = inventoryList.size();
        if (page == 1) {
            return;
        }
        ArrayList<MenuButton> menuButtonsFirst = new ArrayList<>(menuButtons);
        ArrayList<MenuButton> menuButtonsEnd = new ArrayList<>(menuButtons);

        String style = Config.getMenuStyle();
        final int x = style.equals("Bottom") ? 45 : 0;//底部或顶部 此值决定了按钮的位置（slot）
        Inventory[] inventories = new Inventory[page];
        for (int i = 1; i < page; i++) {
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
        inventories[page - 1] = inventoryList.get(page - 1);
        for (int n = 0; n < menuButtonsEnd.size(); n++) {
            MenuButton button = menuButtonsEnd.get(n);
            if (button.getType().equals("NextPage")) {
                menuButtonsEnd.set(n, new MenuButton("PlaceHolder", button.getSlot()));
            }
        }
        for (MenuButton button : menuButtonsEnd) {
            if (x == 45) {
                inventories[page - 1].setItem(inventories[page - 1].getSize() - 10 + button.getSlot(), button.getItemStack());
            } else {
                inventories[page - 1].setItem(button.getSlot() - 1, button.getItemStack());
            }
        }
        inventoryList.set(page - 1, inventories[page - 1]);

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

    public ArrayList<Inventory> getInventoryList() {
        return inventoryList;
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

    @Override
    public Inventory getInventory() {// 只返回第一个
        return inventoryList.get(0);
    }

}
