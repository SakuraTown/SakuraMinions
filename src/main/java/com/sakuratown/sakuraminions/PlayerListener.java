package com.sakuratown.sakuraminions;

import com.sakuratown.sakuraminions.minions.Minion;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {

    public void onPlayerInteractEvent(PlayerInteractEvent event) {

        System.out.println("AAA");
        ItemStack item = event.getItem();
        if (item == null) return;

        if (item.getType() == Material.EGG) {

            Minion minion = new Minion("miner", 10, 9);
            minion.openInventory(event.getPlayer());
        }
    }
}
