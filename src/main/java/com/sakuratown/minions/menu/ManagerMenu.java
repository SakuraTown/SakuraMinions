package com.sakuratown.minions.menu;

import com.sakuratown.library.menu.Button;
import com.sakuratown.library.menu.Menu;
import com.sakuratown.library.utils.Config;
import com.sakuratown.minions.minion.Minion;
import org.bukkit.entity.Player;

//TODO 为啥不是继承 Menu 类?
public class ManagerMenu {

    Menu menu;

    public ManagerMenu() {

        Config config = Config.getConfig("menu");
        menu = config.getMenu("管理菜单");

        setButtonAction();
    }

    public void changeName(Minion minion) {

    }

    private void setButtonAction() {
        for (Button button : menu.buttonMap.values()) {

            if (button.action == null) continue;

            if (button.action.equals("ChangeName")) {
                button.clickEvent = event -> {

                };
            } else if (button.action.equals("UpgradeStorage")) {

            }
        }
    }

    public void open(Player player) {
        player.openInventory(menu.getInventory());
    }
}
