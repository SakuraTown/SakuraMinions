package com.sakuratown.sakuraminions.minions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class MinionInventory implements InventoryHolder {

    private final ArrayList<Inventory> inventoryList = new ArrayList<>();
    private final String type;
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

        for (ItemStack invStack : inventory.getStorageContents()) {
            if (invStack == null) {
                amount += 64;
            } else if (invStack.isSimilar(itemStack)) {
                amount += 64 - invStack.getAmount();
            }
        }

        return amount;
    }

    public void addRow(int row) {
        if (row <= 0) {
            return;
        }

        this.row += row;

        int lastPage = inventoryList.size();

        ItemStack[] contents = getPageContents(lastPage);
        inventoryList.remove(lastPage - 1);

        initInventory();

        inventoryList.get(lastPage - 1).setContents(contents);
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
        Inventory inventory = inventoryList.get(page - 1);
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


    public void addMenuButton() {// todo:没解决，有bug，功能未完善
        if (maxPage == 1) {
            return;
        }
        int styleControl = MenuButton.type.equals("Top") ? -1 :44;
        for(int i =0; i< MenuButton.fistMenu.length;i++){ //第一页
            inventoryList.get(0).setItem(styleControl+i+1,MenuButton.fistMenu[i]);
        }
        for(int n = 1 ;n < maxPage;n++){//中间页
            for(int i =0; i< MenuButton.midMenu.length;i++){
                inventoryList.get(n).setItem(styleControl+i+1,MenuButton.midMenu[i]);
            }
        }
        for(int i = 0; i< MenuButton.lastMenu.length;i++){//最后一页
            inventoryList.get(inventoryList.size()-1).setItem(inventoryList.get(inventoryList.size()-1).getSize()-10+i,MenuButton.midMenu[i]);
        }
    }

    public void addItem(List<ItemStack> itemList) { //往背包塞物品
        if (itemList.isEmpty()) {
            return;
        }
        if (itemList.size() > (row * 9)) {
            return;
        }
        for (ItemStack item : itemList) { //填充物品 从第一个物品开始
            addItem(item);
        }
    }

    public void addItem(ItemStack item) {
        Map<Integer, ItemStack> tempItemMap = new HashMap<>() {{ put(0, item); }};
        for (Inventory inv : inventoryList) {
            tempItemMap = inv.addItem(tempItemMap.get(0));
            if (tempItemMap.isEmpty()) {
                return;
            }
        }
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

        //TODO 等待优化中
//        List<ItemStack> itemStackList = new ArrayList<>();
//        getItemCount().forEach((string, amount) -> {
//            Material type = Material.valueOf(string);
//            ItemStack itemStack = new ItemStack(type, amount);
//            itemStackList.add(itemStack);
//        });
//        System.out.println(itemStackList);
//        addItem(itemStackList);
    }

    public HashMap<String, Integer> getItemCount() {
        HashMap<String, Integer> itemCount = new HashMap<>();

        for (int i = 0; i < inventoryList.size(); i++) {
            ItemStack[] pageContents = getPageContents(i);
            ItemStack itemStack = pageContents[i];

            if (itemStack == null) continue;

            String name = itemStack.getType().name();
            int amount = itemStack.getAmount();

            itemCount.merge(name, amount, Integer::sum);

        }
        return itemCount;
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
