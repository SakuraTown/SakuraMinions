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
            inventory = Bukkit.createInventory(this, 54, type + ":" + page);
            inventoryList.add(inventory);
            addMenuButton(i);
        }

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

    public static boolean isButton(ItemStack itemStack) {
        if (itemStack == null) return false;
        ItemMeta itemMeta = itemStack.getItemMeta();
        ArrayList<String> buttonNames = Config.getMenuConfig().getButtonNames();
        return buttonNames.contains(itemMeta.getDisplayName());
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


    public void addMenuButton(int page) {//todo: 修复bug
        ItemStack lockArea = Config.getMenuConfig().getLockArea();
        if (page <=1 && maxPage ==1) {
            Inventory fistInv = inventoryList.get(0);
            for (int n = row * 9; n < 54; n++){
                fistInv.setItem(n, lockArea);}
            return;
        }
        MenuButton menuButton = Config.getMenuConfig();
        int styleControl = Config.getMenuStyle().equals("Bottom") ? 45 : 0;
        if (page == 1 && maxPage >1) {
            Inventory fistInv = inventoryList.get(0);
            menuButton.getFistMenu().forEach((slot, item) -> {
                fistInv.setItem(slot + styleControl, item);
            });
        }
        if (page == maxPage) {
            Inventory endInv = inventoryList.get(maxPage - 1);
            menuButton.getEndMenu().forEach((slot, item) -> {
                endInv.setItem(slot + styleControl, item);
            });
            int endRow = row % 5;
            if (endRow == 0) {
                return;
            }//最后一页没有多余空间
            if (styleControl == 45) {//44表示是底部菜单
                for (int n = endRow * 9; n < 45; n++) {
                    endInv.setItem(n, lockArea);
                }
            } else {//顶部菜单
                for (int n = (endRow + 1) * 9; n < 54; n++) {
                    endInv.setItem(n, lockArea);
                }
            }
        }
        Inventory inv = inventoryList.get(page - 1);
        menuButton.getMidMenu().forEach((slot, item) -> {
            inv.setItem(slot + styleControl, item);
        });

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
        Map<Integer, ItemStack> tempItemMap = new HashMap<>() {{
            put(0, item);
        }};
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
        for (int i = 1; i < maxPage; i++) {
            addMenuButton(i);
        }

    }

    public ArrayList<Inventory> getInventoryList() {
        return inventoryList;
    }

    @Override
    public Inventory getInventory() {// 只返回第一个
        return inventoryList.get(0);
    }

}
