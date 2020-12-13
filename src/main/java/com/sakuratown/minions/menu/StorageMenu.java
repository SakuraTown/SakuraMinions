package com.sakuratown.minions.menu;

import com.sakuratown.library.menu.Button;
import com.sakuratown.library.menu.PageableMenu;
import com.sakuratown.library.utils.Config;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class StorageMenu extends PageableMenu {

    public StorageMenu(int storage) {
        super(storage);
        Config.getConfig("menu").setPageableMenu("工人仓库", this);
        setLock(false);
    }

    public void addItem(List<ItemStack> collectItems) {

    }
}
