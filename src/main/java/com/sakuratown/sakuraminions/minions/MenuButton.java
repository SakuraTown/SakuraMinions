package com.sakuratown.sakuraminions.minions;

import com.sakuratown.sakuraminions.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Consumer;

import java.util.ArrayList;

public class MenuButton {
    private final String type; //type 为LastPage、NextPage和PlaceHolder之一。
    private final ItemStack itemStack;
    private final int slot;
    public Consumer<InventoryClickEvent> clickEvent;

    public MenuButton(String type, int slot) {
        this.type = type;
        itemStack = setMenuButton(type);
        this.slot = slot;
    }

    public static ArrayList<MenuButton> initMenuButton() {
        ArrayList<MenuButton> menuButtons = new ArrayList<>();
        createButton(menuButtons, "LastPage");
        createButton(menuButtons, "PlaceHolder");
        createButton(menuButtons, "NextPage");

        return menuButtons;
    }

    private static int[] initSlot(String slotConfig) {
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

    public ItemStack setMenuButton(String type) { //type 为LastPage、NextPage和PlaceHolder之一。

        ConfigurationSection menuSection = Config.getMenuSection();
        String materialName = menuSection.getString(type + ".Material");

        if (materialName == null || Material.getMaterial(materialName) == null) {
            return new ItemStack(Material.STONE);
        }

        Material material = Material.getMaterial(materialName);

        if (material == null) {
            return new ItemStack(Material.STONE);
        }

        String displayName = menuSection.getString(type + ".Name");
        return new ItemBuilder(material).name(displayName).build();
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public String getType() {
        return type;
    }

    public int getSlot() {
        return slot;
    }

    private static void createButton(ArrayList<MenuButton> menuButton, String types) {
        String slots = Config.getMenuSection().getString(types.concat(".Slots"));
        if (slots != null) {
            int[] slot = initSlot(slots);
            for (int i : slot) {
                MenuButton button = new MenuButton(types, i);
                if (!menuButton.contains(button)) {
                    menuButton.add(button);
                }
            }
        }
    }
}

