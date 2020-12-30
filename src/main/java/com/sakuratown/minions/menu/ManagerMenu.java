package com.sakuratown.minions.menu;

import com.sakuratown.library.menu.Button;
import com.sakuratown.library.menu.Menu;
import com.sakuratown.library.utils.Config;
import com.sakuratown.minions.minion.Minion;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

public class ManagerMenu extends Menu {

    private final Minion minion;

    public ManagerMenu(Minion minion) {
        this.minion = minion;
        Config.getConfig("menu").setMenu("管理菜单", this);
    }

    @Override
    public void setButtonAction(Button button) {

        ConfigurationSection config = minion.getConfig();

        switch (button.action) {
            case "ChangeName":

                button.clickEvent = event -> {
                    minion.setName("设置名字");
                };
                break;

            case "UpgradeStorage":

                button.clickEvent = event -> {
                    HumanEntity whoClicked = event.getWhoClicked();

                    if (whoClicked instanceof Player) {
                        minion.upgradeStorage((Player) whoClicked, config.getInt("Efficiency.UpgradeEfficiency"));

                    }
                };
                break;

            case "UpgradeEfficiency":

                button.clickEvent = event -> {
                    HumanEntity whoClicked = event.getWhoClicked();

                    if (whoClicked instanceof Player) {
                        minion.upgradeEfficiency((Player) whoClicked, config.getInt("Efficiency.UpgradeEfficiency"));

                    }
                };
                break;

            case "RemoveMinion":

                button.clickEvent = event -> {
                    minion.remove();
                };
                break;

            case "OpenStorageMenu":

                button.clickEvent = event -> {
                    minion.openStorageMenu((Player) event.getWhoClicked());
                    setTitle("setDefaultButtonAction");
                    event.getWhoClicked().openInventory(inventory);
                };
                break;
        }
    }

}
