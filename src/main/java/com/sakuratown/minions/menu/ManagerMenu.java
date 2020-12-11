package com.sakuratown.minions.menu;

import com.sakuratown.library.menu.Button;
import com.sakuratown.library.menu.Menu;
import com.sakuratown.library.utils.Config;
import com.sakuratown.minions.minion.Minion;
import org.bukkit.entity.Player;

public class ManagerMenu{

    Minion minion;
    Menu menu;

    public ManagerMenu(Minion minion) {
        this.minion = minion;

        Config config = Config.getConfig("menu");
        menu = config.getMenu("管理菜单");

        setButtonAction();
    }

    private void setButtonAction() {
        for (Button button : menu.buttonMap.values()) {
            if (button.action == null) continue;

            if (button.action.equals("ChangeName")) {
                button.clickEvent = event -> {
                    minion.changeName();
                };
            } else if (button.action.equals("UpgradeStorage")) {

            }
        }
    }

    public void open(Player player) {

    }
}
