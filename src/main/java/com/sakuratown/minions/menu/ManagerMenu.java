package com.sakuratown.minions.menu;

import com.sakuratown.library.menu.Button;
import com.sakuratown.library.menu.Menu;
import com.sakuratown.library.utils.Config;
import com.sakuratown.minions.minion.Minion;
import org.bukkit.entity.Player;

//TODO 为啥不是继承 Menu 类?
public class ManagerMenu extends Menu {

    public ManagerMenu() {
        Config.getConfig("menu").setMenu("管理菜单", this);
    }

    public void changeName(Minion minion) {

    }

    private void setButtonAction() {
        for (Button button : buttonMap.values()) {

            if (button.action == null) continue;

            if (button.action.equals("ChangeName")) {
                button.clickEvent = event -> {

                };
            } else if (button.action.equals("UpgradeStorage")) {

            }
        }
    }
}
