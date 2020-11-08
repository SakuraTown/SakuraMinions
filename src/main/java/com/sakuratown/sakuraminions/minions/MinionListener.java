package com.sakuratown.sakuraminions.minions;

import com.sakuratown.sakuralibrary.utils.Message;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;

import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MinionListener implements Listener {
    HashMap<String, Minion> minionList = new HashMap<>();
    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        ItemStack minionItem = event.getItem();
        if (minionItem == null) {
            return;
        }
        if (!isMinionItem(minionItem)) {
            return;
        }
        Block block = event.getClickedBlock();
        if (block == null || block.isEmpty() || block.isLiquid()) {
            return;
        }
        Block upBlock = block.getRelative(BlockFace.UP);
        if (upBlock.isEmpty()) {
            Location loc = upBlock.getLocation().add(0.5, 0, 0.5);
            summonMinion(event.getPlayer(),loc,minionItem);
        } else {
            event.getPlayer().sendMessage(Message.toColor("&c没有足够的空间放置！"));
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerArmorStandManipulateEvent(PlayerArmorStandManipulateEvent event) {
        if (isMinionItem(event.getArmorStandItem())) {
            event.setCancelled(true);
        }
        if (isMinionItem(event.getPlayerItem())) {
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onPlayerInteractAtEntityEvent(PlayerInteractAtEntityEvent event){
        if(event.isCancelled()){return;}
        if(event.getRightClicked().getType() != EntityType.ARMOR_STAND){
            return;
        }
        String UUID = event.getRightClicked().getUniqueId().toString();
        if(minionList.containsKey(UUID)){
            minionList.get(UUID).showGuI(event.getPlayer());
        }
    }

    private static boolean isMinionItem(ItemStack item) {
        if (item.getType() != Material.PLAYER_HEAD) {
            return false;
        }
        List<String> lore = item.getLore();
        if (lore == null) {
            return false;
        }
        return lore.get(lore.size() - 1).contains("SakuraMinions");
    }

    private void summonMinion(Player player,Location loc,ItemStack minionItem) {
        String name = minionItem.getItemMeta().getDisplayName();
        String minionType = null;
        for (String type : Config.getMinionSection().getKeys(false)) {
            if (name.contains(type)) {
                minionType = type;
                break;
            }
        }
        if(minionType == null){return;}
        ArmorStand armorStand = (ArmorStand) player.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        armorStand.setSmall(true);
        armorStand.setArms(true);
        armorStand.setBasePlate(false);
        armorStand.setCustomName(ChatColor.GOLD + player.getName() + "§a的" + ChatColor.RED +minionType);
        armorStand.setCustomNameVisible(true);
        armorStand.setItem(EquipmentSlot.HEAD, minionItem);
        Vector direction = getVector(armorStand).subtract(getVector(player)).normalize();
        double x = direction.getX();
        double y = direction.getY();
        double z = direction.getZ();
        Location changed = armorStand.getLocation().clone();
        changed.setYaw(180 - toDegree(Math.atan2(x, z)));
        changed.setPitch(90 - toDegree(Math.acos(y)));
        armorStand.teleport(changed);
        summonMinion(armorStand.getUniqueId().toString(),minionItem,minionType);
    }
    private void summonMinion(String UUID,ItemStack minionItem,String type){
        List<String> loreConfig = Config.getMinionItemSection().getStringList("Lore");
        List<String> minionItemLore = minionItem.getLore();
        int size = 9,amount = 5;
        Pattern getNum =Pattern.compile("[^0-9]");
        for(String line : loreConfig){
            int n = loreConfig.indexOf(line);
            if(line.contains("%Size%")){
                String sizeLine = minionItemLore.get(n);
                Matcher matcher = getNum.matcher(sizeLine);
                size = Integer.parseInt(matcher.replaceAll(""));
            }
            if(line.contains("%Amount%")){
                String amountLine = minionItemLore.get(n);
                Matcher matcher = getNum.matcher(amountLine);
                amount = Integer.parseInt(matcher.replaceAll(""));
            }
        }
        Bukkit.broadcastMessage(UUID);
        Bukkit.broadcastMessage(String.valueOf(size));
        Bukkit.broadcastMessage(String.valueOf(amount));
        Minion minion = new Minion(type,size/9,amount);
        minionList.put(UUID,minion);
    }
    private static float toDegree(double angle) {
        return (float) Math.toDegrees(angle);
    }
    private static Vector getVector(Entity entity) {
        if (entity instanceof Player)
            return ((Player) entity).getEyeLocation().toVector();
        else
            return entity.getLocation().toVector();
    }
}
