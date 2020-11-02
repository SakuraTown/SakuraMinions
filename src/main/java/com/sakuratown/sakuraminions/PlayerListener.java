package com.sakuratown.sakuraminions;

import com.sakuratown.sakuraminions.minions.Minion;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class PlayerListener implements Listener {
    Minion minion;

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {

        ItemStack item = event.getItem();
        if (item == null) return;

        if (item.getType() == Material.EGG) {
            minion = new Minion("Miner", 7, 50);
            String randomMaterial = minion.getRandomMaterial();

            System.out.println(randomMaterial);
        }
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getPlayer().getTargetBlock(3).getType() == Material.STONE) {
            minion.showGuI(1, event.getPlayer());
        }
        if (item.getType() == Material.SAND) {
            minion.upgradeSize(2);
        }
        if (item.getType() == Material.OAK_LOG) {
            ItemStack itemStack4 = new ItemStack(Material.EGG, 16);
            ArrayList<ItemStack> itemStacks = new ArrayList<>();

            itemStacks.add(itemStack4);
            minion.addItem(itemStacks);
        }
    }

}
