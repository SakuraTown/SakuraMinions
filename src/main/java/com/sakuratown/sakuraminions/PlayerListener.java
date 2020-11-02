package com.sakuratown.sakuraminions;

import com.sakuratown.sakuraminions.minions.Minion;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {
    Minion minion;
    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {

        ItemStack item = event.getItem();
        if (item == null) return;

        if (item.getType() == Material.EGG) {
            minion = new Minion("miner", 1, 50);
        }
        if (item.getType() == Material.STONE) {
            minion.showGuI(1,event.getPlayer());
        }
        if (item.getType() == Material.SAND){
            minion.upgradeSize(2);
        }
    }

}
