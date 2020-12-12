package com.sakuratown.minions.menu;

import com.sakuratown.library.menu.Button;
import com.sakuratown.library.menu.Menu;
import com.sakuratown.library.utils.Config;
import com.sakuratown.minions.minion.Minion;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTListCompound;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ManagerMenu extends Menu {

    private final Minion minion;

    public ManagerMenu(Minion minion) {
        this.minion = minion;
        Config.getConfig("menu").setMenu("管理菜单", this);
    }

    @Override
    public void setButtonAction(Button button) {

        ConfigurationSection config = minion.getConfig();

        String action = button.action;

        switch (action) {
            case "ChangeName":

                button.clickEvent = event -> {
                    minion.setName("设置名字");
                };
                break;

            case "UpgradeStorage":

                button.clickEvent = event -> {
                    minion.upgradeStorage(config.getInt("Storage.UpgradeAmount"));
                };
                break;

            case "UpgradeEfficiency":

                button.clickEvent = event -> {
                    minion.upgradeEfficiency(config.getInt("Storage.UpgradeEfficiency"));
                };
                break;

            case "RemoveMinion":

                button.clickEvent = event -> {
                    minion.remove();
                };
                break;

            case "OpenStorageMenu":

                button.clickEvent = event -> {
                    minion.openStorageMenu(event.getWhoClicked().getKiller());
                };
                break;

            case "Close":

                //TODO 这里的头颅只有放在地上才显示皮肤, 不知道为什么
                NBTItem nbtItem = new NBTItem(button.itemStack);

                NBTCompound skull = nbtItem.addCompound("SkullOwner");
                skull.setString("Id", String.valueOf(UUID.randomUUID()));

                NBTListCompound texture = skull.addCompound("Properties").getCompoundList("textures").addCompound();
                texture.setString("Value", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTk5YWFmMjQ1NmE2MTIyZGU4ZjZiNjI2ODNmMmJjMmVlZDlhYmI4MWZkNWJlYTFiNGMyM2E1ODE1NmI2NjkifX19");
                button.itemStack = nbtItem.getItem();

                button.clickEvent = event -> {
                    event.getWhoClicked().getInventory().addItem(button.itemStack);
                    event.getWhoClicked().closeInventory();
                };

                break;
        }
    }

}
