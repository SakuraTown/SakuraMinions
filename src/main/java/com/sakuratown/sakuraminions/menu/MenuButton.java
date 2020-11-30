package com.sakuratown.sakuraminions.menu;

import com.sakuratown.sakuralibrary.utils.ItemBuilder;
import com.sakuratown.sakuraminions.Main;
import com.sakuratown.sakuraminions.minions.Config;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;


public class MenuButton {
    private final int MaxMenuLength = 9;
    private final ConfigurationSection menuSection;
    private final HashMap<String, Button> buttonList;
    private final ItemStack[] menuList;
    private ItemStack lockArea;
    private ArrayList<String> buttonNames;
    private HashMap<Integer, ItemStack> fistMenu;
    private HashMap<Integer, ItemStack> midMenu;
    private HashMap<Integer, ItemStack> endMenu;

    public MenuButton() {
        buttonList = new HashMap<>();
        menuList = new ItemStack[MaxMenuLength];
        menuSection = Config.getMenuSection();
        initMenuButton();
        initMenu();
        initFistMenu();
        initMidMenu();
        initEndMenu();
    }

    private static int[] initSlot(String slotConfig) {
        if (slotConfig == null) {
            return null;
        }
        int[] slots;
        if (slotConfig.contains(",")) {

            String[] split = slotConfig.split(",");
            slots = new int[split.length];

            for (int i = 0; i < split.length; i++) {
                slots[i] = Integer.parseInt(split[i]);
            }
        } else {
            slots = new int[1];
            slots[0] = Integer.parseInt(slotConfig);
        }
        return slots;
    }

    private void initMenuButton() {
        for (String buttonType : menuSection.getKeys(false)) {
            if (buttonType.equals("Style")) {
                continue;
            }
            String materialName = menuSection.getString(buttonType.concat(".Material"));
            String buttonName = menuSection.getString(buttonType.concat(".Name"));
            String buttonSlots = menuSection.getString(buttonType.concat(".Slots"));//可能null

            Material buttonMaterial;

            if (materialName == null || Material.getMaterial(materialName) == null) {
                Main.getInstance().getLogger().info(buttonType + "材质名错误，现已替换成石头，请检查配置文件！");
                buttonMaterial = Material.STONE;
            } else {
                buttonMaterial = Material.getMaterial(materialName);
            }

            ItemBuilder tempButton = new ItemBuilder(buttonMaterial, 1);
            tempButton.name(buttonName);
            Button button = new Button(tempButton.build(), buttonSlots);
            buttonList.put(buttonType, button);
        }
        lockArea = buttonList.get("LockArea").itemStack;
        buttonNames = new ArrayList<>();
        buttonList.forEach((k, v) -> buttonNames.add(v.itemStack.getItemMeta().getDisplayName()));
    }

    private void initMenu() {
        buttonList.forEach((key, value) -> {
            if (value.slots == null) {
                return;
            }
            int length = value.slots.length;
            for (int n = 0; n < length; n++) {
                menuList[value.slots[n] - 1] = value.itemStack;
            }
        });
    }

    private void initFistMenu() {
        fistMenu = new HashMap<>();
        for (int n = 0; n < MaxMenuLength; n++) {
            if (menuList[n].isSimilar(buttonList.get("LastPage").itemStack)) {
                fistMenu.put(n, buttonList.get("PlaceHolder").itemStack);
                continue;
            }
            fistMenu.put(n, menuList[n]);
        }
    }

    private void initMidMenu() {
        midMenu = new HashMap<>();
        for (int n = 0; n < MaxMenuLength; n++) {
            midMenu.put(n, menuList[n]);
        }
    }

    private void initEndMenu() {
        endMenu = new HashMap<>();
        for (int n = 0; n < MaxMenuLength; n++) {
            if (menuList[n].isSimilar(buttonList.get("NextPage").itemStack)) {
                endMenu.put(n, buttonList.get("PlaceHolder").itemStack);
                continue;
            }
            endMenu.put(n, menuList[n]);
        }
    }

    public HashMap<Integer, ItemStack> getFistMenu() {
        return fistMenu;
    }

    public HashMap<Integer, ItemStack> getMidMenu() {
        return midMenu;
    }

    public HashMap<Integer, ItemStack> getEndMenu() {
        return endMenu;
    }

    public ItemStack getLockArea() {
        return lockArea;
    }

    public HashMap<String, Button> getButtonList() {
        return buttonList;
    }

    public ArrayList<String> getButtonNames() {
        return buttonNames;
    }

    private static class Button {
        protected ItemStack itemStack;
        protected int[] slots;

        public Button(ItemStack itemStack, String slots) {
            this.itemStack = itemStack;
            this.slots = initSlot(slots);
        }
    }
}

