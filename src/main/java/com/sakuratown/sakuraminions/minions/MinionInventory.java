package com.sakuratown.sakuraminions.minions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Map;

public class MinionInventory implements InventoryHolder {

    private ArrayList<MenuButton> menuButtons;
    private final ArrayList<Inventory> inventoryList;
    private final String type;
    private final int row;
    private final int page;
    private int nowPage = 1;


    public MinionInventory(String type, int row) {
        this.row = row;
        this.type = type;
        page = (row / 6) + 1; //容器理论页数（加菜单）
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

    private void initInventory() {

        Inventory[] initInventory = new Inventory[page];
        for (int i = 0; i < page; i++) {
            if (i == (page - 1)) {
                initInventory[i] = Bukkit.createInventory(this, (row % 6 + page) * 9, type + ":" + (i + 1));
            } else {
                initInventory[i] = Bukkit.createInventory(this, 54, type + ":" + (i + 1));
            }
            inventoryList.add(initInventory[i]);
        }
        menuButtons = MenuButton.initMenuButton();
        addMenu(inventoryList, menuButtons);
    }

    public int getPage() {
        return page;
    }

    public int getNowPage() {
        return nowPage;
    }

    public void setNowPage(int page) {
        nowPage = page;
    }

    public void showInventoryGUI(int page, Player player) {
        Inventory inventory = inventoryList.get(page - 1);
        setNowPage(page);
        player.openInventory(inventory);
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

    public boolean addInventory(ArrayList<ItemStack> itemList) { //往背包塞物品
        if (itemList.isEmpty()) {
            return false;
        }
        int n = 0;
        Map<Integer, ItemStack> tempItemMap;
        ItemStack tempItemStack;
        if (itemList.size() > (row * 9)) {
            return false;
        }
        for (Inventory inv : inventoryList) {
            for (int i = n; i < itemList.size(); i++) {
                if (getFreeSpace(inv, itemList.get(i)) > 0) {
                    tempItemMap = inv.addItem(itemList.get(i));
                    if (!tempItemMap.isEmpty()) {
                        tempItemStack = tempItemMap.get(0);
                        itemList.set(i, tempItemStack);
                        n = i - 1;
                        break;
                    }
                } else {
                    n = i;
                    break;
                }
            }
        }
        return true;
    }

    public ArrayList<Inventory> getInventoryList() {
        return inventoryList;
    }

    @Override
    public Inventory getInventory() {// 只返回第一个
        return inventoryList.get(0);
    }

}
