package com.sakuratown.sakuraminions;

import com.sakuratown.sakuraminions.minions.Minion;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {

        ItemStack item = event.getItem();
        if (item == null) return;

        if (item.getType() == Material.EGG) {

            Minion minion = new Minion("miner", 18, 50);
            minion.showGuI(4,event.getPlayer());
            //minion.openInventory(event.getPlayer());
        }
    }
}
