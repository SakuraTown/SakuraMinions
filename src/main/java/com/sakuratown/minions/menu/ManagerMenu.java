package com.sakuratown.minions.menu;

import com.sakuratown.library.menu.Button;
import com.sakuratown.library.menu.Menu;
import com.sakuratown.library.utils.Config;

public class ManagerMenu extends Menu {

    public ManagerMenu() {
        Config.getConfig("menu").setMenu("管理菜单", this);
    }

    @Override
    public void setButtonAction(Button button) {
        if (button.action.equals("ChangeName")) {
            button.clickEvent = event -> {
                System.out.println("改名测试");
            };
        }
    }
}
