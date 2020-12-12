package com.sakuratown.minions.menu;

import com.sakuratown.library.menu.Button;
import com.sakuratown.library.menu.PageableMenu;
import com.sakuratown.library.utils.Config;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class StorageMenu extends PageableMenu {

    public StorageMenu(String type, int storage) {

        // 初始化所有界面(需要最大页数)
        // 设置所有界面的按钮(需要在这里设置特殊按钮行为)
        // 添加其他东西

        Config.getConfig("menu").setPageableMenu("工人仓库", this, getMaxPage(storage));

        setLock(false);
    }

    public void addItem(List<ItemStack> collectItems) {

    }

    public void setButtonAction(Button button) {

    }


}
