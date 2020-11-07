package com.sakuratown.sakuraminions;

import com.sakuratown.sakuraminions.minions.Minion;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
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
            minion = new Minion("Miner", 6, 50);
        }
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getPlayer().getTargetBlock(3).getType() == Material.STONE) {
            minion.showGuI(event.getPlayer());
        }
        if (item.getType() == Material.SAND) {
            minion.upgradeSize(2);
        }
        if (item.getType() == Material.OAK_LOG) {
            ItemStack itemStack1 = new ItemStack(Material.OAK_LOG, 64);
            ItemStack itemStack2 = new ItemStack(Material.SAND, 64);
            ItemStack itemStack3 = new ItemStack(Material.STONE, 100);
            ItemStack itemStack4 = new ItemStack(Material.EGG, 16);
            ArrayList<ItemStack> itemStacks = new ArrayList<>();
            itemStacks.add(itemStack1);
            itemStacks.add(itemStack2);
            itemStacks.add(itemStack3);
            itemStacks.add(itemStack4);
            minion.addItem(itemStacks);
        }
        if (item.getType() == Material.NETHER_STAR) {
            minion.addRandomItem();
        }
        if (item.getType() == Material.COBBLESTONE) {
            ItemStack itemStack4 = new ItemStack(Material.DIAMOND_AXE, 1);
            Bukkit.broadcastMessage(String.valueOf(itemStack4.getMaxStackSize()));
        }

    }
    @EventHandler
    public void onPlayerArmorStandManipulateEvent(PlayerArmorStandManipulateEvent e){
            if(e.getArmorStandItem().getType() == Material.PLAYER_HEAD){
                minion.showGuI(e.getPlayer());
                e.setCancelled(true);
            }
    }
}
