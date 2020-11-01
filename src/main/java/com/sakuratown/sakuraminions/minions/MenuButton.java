package com.sakuratown.sakuraminions.minions;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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

    public ItemStack getItemStack() {
        return itemStack;
    }

    public String getType() {
        return type;
    }

    public int getSlot() {
        return slot;
    }

    public static ArrayList<MenuButton> initMenuButton() {
        ArrayList<MenuButton> menuButtons = new ArrayList<>();
        createButton(menuButtons, "LastPage");
        createButton(menuButtons, "PlaceHolder");
        createButton(menuButtons, "NextPage");

        return menuButtons;
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

    public ItemStack setMenuButton(String type) { //type 为LastPage、NextPage和PlaceHolder之一。
        ItemStack button;
        ConfigurationSection menuSection = Config.getMenuSection();
        if (type != null) {
            String materialName = menuSection.getString(type + ".Material");
            if (materialName != null) {
                Material buttonMaterial = Material.getMaterial(materialName);
                if (buttonMaterial != null) {
                    button = new ItemStack(buttonMaterial);
                    ItemMeta buttonMeta = button.getItemMeta();
                    buttonMeta.setDisplayName(menuSection.getString(type + ".Name"));
                    button.setItemMeta(buttonMeta);
                    return button;
                } else {
                    return new ItemStack(Material.STONE);
                }
            }
        }
        return new ItemStack(Material.STONE);
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

}
