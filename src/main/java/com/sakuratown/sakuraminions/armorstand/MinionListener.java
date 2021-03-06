package com.sakuratown.sakuraminions.armorstand;

import com.sakuratown.sakuralibrary.utils.Message;
import com.sakuratown.sakuraminions.Main;
import com.sakuratown.sakuraminions.minions.Config;
import com.sakuratown.sakuraminions.minions.Minion;
import com.sakuratown.sakuraminions.minions.MinionManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MinionListener implements Listener {

    // 工人放置监听
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        ItemStack minionItem = event.getItem();

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK || !isMinionItem(minionItem)) {
            return;
        }

        Block block = event.getClickedBlock();

        if (block == null || block.isEmpty() || block.isLiquid()) {
            return;
        }

        Block upBlock = block.getRelative(BlockFace.UP);

        if (upBlock.isEmpty()) {
            Location loc = upBlock.getLocation().add(0.5, 0, 0.5);
            int amount = minionItem.getAmount();
            summonMinion(event.getPlayer(), loc, minionItem);
            minionItem.setAmount(amount - 1);
        } else {
            event.getPlayer().sendMessage(Message.toColor("&c没有足够的空间！"));
        }
        event.setCancelled(true);
    }

    //禁止拿头放头
    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerArmorStandManipulateEvent(PlayerArmorStandManipulateEvent event) {
        if (isMinionItem(event.getArmorStandItem())) {
            event.setCancelled(true);
            return;
        }
        if (isMinionItem(event.getPlayerItem())) {
            event.setCancelled(true);
        }
    }

    // 打开工人背包（盔甲架实体）需要潜行+右键
    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerInteractAtEntityEvent(PlayerInteractAtEntityEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getRightClicked().getType() != EntityType.ARMOR_STAND) {
            return;
        }
        Player player = event.getPlayer();
        if (!player.isSneaking()) {
            return;
        }
        String uuid = event.getRightClicked().getUniqueId().toString();
        event.setCancelled(true);
        MinionManager minionManager = MinionManager.getInstance();
        if (minionManager.isMinion(uuid)) {
            minionManager.getMinion(uuid).showGuI(player);
        }
    }

    @EventHandler
    public void onEntityDeathEvent(EntityDeathEvent event) { //删除工人实体并从列表删除
        if (event.isCancelled()) {
            return;
        }
        LivingEntity entity = event.getEntity();
        if (entity.getType() != EntityType.ARMOR_STAND) {
            return;
        }
        MinionManager minionManager = MinionManager.getInstance();
        if (!minionManager.isMinion(entity.getUniqueId().toString())) {
            return;
        }
        ArmorStand armorStand = (ArmorStand) entity;
        List<ItemStack> drops = event.getDrops();
        if (drops.isEmpty()) {
            ItemStack head = new ItemStack(armorStand.getItem(EquipmentSlot.HEAD));
            Location location = armorStand.getLocation();
            armorStand.getWorld().dropItem(location, head);
        } else {
            event.getDrops().remove(0);
        }
        String uuid = entity.getUniqueId().toString();
        minionManager.getMinion(uuid).dropItems(entity.getLocation());
        minionManager.removeMinion(uuid);
    }

    // 检测是否是工人（物品形式）
    private boolean isMinionItem(ItemStack item) {
        if (item == null || item.getType() != Material.PLAYER_HEAD) {
            return false;
        }
        List<String> lore = item.getLore();
        if (lore == null) {
            return false;
        }
        return lore.get(lore.size() - 1).contains("SakuraMinions");
    }

    // 生成工人盔甲架
    private void summonMinion(Player player, Location loc, ItemStack minionItem) {
        String name = minionItem.getItemMeta().getDisplayName();
        String minionType = null;
        for (String type : Config.getMinionSection().getKeys(false)) {
            if (name.contains(type)) {
                minionType = type;
                break;
            }
        }
        if (minionType == null) {
            return;
        }
        ArmorStand armorStand = (ArmorStand) player.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        armorStand.setSmall(true);
        armorStand.setArms(true);
        armorStand.setBasePlate(false);
        armorStand.setCustomName(ChatColor.GOLD + player.getName() + "§a的" + ChatColor.RED + minionType);
        armorStand.setCustomNameVisible(true);
        minionItem.setAmount(1);
        armorStand.setItem(EquipmentSlot.HEAD, minionItem);
        changeDirectionTo(armorStand, player);
        buildMinion(armorStand.getUniqueId().toString(), minionItem, minionType);
    }

    // 将盔甲架方向改为面向玩家
    private void changeDirectionTo(ArmorStand armorStand, Player player) {
        Vector direction = getVector(armorStand).subtract(getVector(player)).normalize();
        double x = direction.getX();
        double y = direction.getY();
        double z = direction.getZ();
        Location changed = armorStand.getLocation().clone();
        changed.setYaw(180 - toDegree(Math.atan2(x, z)));
        changed.setPitch(90 - toDegree(Math.acos(y)));
        armorStand.teleport(changed);
    }

    // 创建工人类并加入列表
    private void buildMinion(String uuid, ItemStack minionItem, String type) {
        List<String> loreConfig = Config.getMinionItemSection().getStringList("Lore");
        List<String> minionItemLore = minionItem.getLore();
        int size = 9, amount = 5;//随便初始化一个值，IDEA老提醒没有初始化，很烦
        Pattern getNum = Pattern.compile("[^0-9]");//正则表达式匹配数字
        for (int n = 0; n < loreConfig.size(); n++) {//获取参数
            String line = loreConfig.get(n);
            if (line.contains("%Size%")) {
                Matcher matcher = getNum.matcher(minionItemLore.get(n));
                size = Integer.parseInt(matcher.replaceAll(""));
            } else if (line.contains("%Amount%")) {
                Matcher matcher = getNum.matcher(minionItemLore.get(n));
                amount = Integer.parseInt(matcher.replaceAll(""));
            }
        }
        Minion minion = new Minion(type, size / 9, amount);
        MinionManager.getInstance().addMinion(uuid, minion);
    }

    // 改变工人方向依赖
    private float toDegree(double angle) {
        return (float) Math.toDegrees(angle);
    }

    // 改变工人方向依赖
    private Vector getVector(Entity entity) {
        if (entity instanceof Player)
            return ((Player) entity).getEyeLocation().toVector();
        else
            return entity.getLocation().toVector();
    }
}
