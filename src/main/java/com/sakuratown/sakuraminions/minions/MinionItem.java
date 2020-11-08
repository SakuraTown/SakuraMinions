package com.sakuratown.sakuraminions.minions;

import com.sakuratown.sakuralibrary.utils.ItemBuilder;
import com.sakuratown.sakuralibrary.utils.Message;
import org.bukkit.Material;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MinionItem {
    public static ItemStack getMinionItem(String type, int size, int amount) {
        ItemBuilder itemBuilder = new ItemBuilder(Material.PLAYER_HEAD);
        itemBuilder.name(getItemName(type));
        itemBuilder.lore(getItemLore(type,size,amount));
        return itemBuilder.BuildSkull(getBase64Texture(type));
    }
    public static String getBase64Texture(String type) {
        return Config.getMinionSection().getString(type.concat(".SkullTexture"));
    }

    public static String getDescribe(String type) {
        return Config.getMinionSection().getString(type.concat(".Describe"));
    }
    public static String getItemName(String type) {
        return Message.toColor(Message.replace(Config.getMinionItemSection().getString("Name"), "%Type%", type));
    }
    public static List<String> getItemLore(String type,int size,int amount) {
        String describe = getDescribe(type);
        List<String> loreList = Config.getMinionItemSection().getStringList("Lore");
        Message.replace(loreList, "%Describe%", describe, "%Size%", String.valueOf(size), "%Amount%", String.valueOf(amount));
        return Message.toColor(loreList);
    }
}
