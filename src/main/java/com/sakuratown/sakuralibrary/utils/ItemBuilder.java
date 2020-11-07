package com.sakuratown.sakuralibrary.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ItemBuilder {

    private final ItemStack itemStack;
    private ItemMeta itemMeta;

    public ItemBuilder(Material material, int amount) {
        itemStack = new ItemStack(material, amount);
        itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = new ItemStack(itemStack);
        itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder(Material material) {
        this(material, 1);
        itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder name(String displayName) {

        if (displayName == null) return this;

        itemMeta.setDisplayName(Message.toColor(displayName));
        itemStack.setItemMeta(itemMeta);

        return this;
    }

    public ItemBuilder lore(String... lore) {

        itemMeta.setLore(Arrays.asList(Message.toColor(lore)));
        itemStack.setItemMeta(itemMeta);

        return this;
    }

    public ItemBuilder lore(List<String> lore) {

        itemMeta.setLore(Message.toColor(lore));
        itemStack.setItemMeta(itemMeta);

        return this;
    }

    public ItemBuilder addLore(String... lore) {

        List<String> itemLore = itemMeta.getLore();
        if (itemLore == null) return this;

        itemLore.addAll(Arrays.asList(Message.toColor(lore)));

        itemMeta.setLore(itemLore);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder addLore(List<String> lore) {

        List<String> itemLore = itemMeta.getLore();
        if (itemLore == null) return this;

        itemLore.addAll(Message.toColor(lore));

        itemMeta.setLore(itemLore);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        itemStack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder addFlag(ItemFlag itemFlag) {
        itemMeta.addItemFlags(itemFlag);
        itemStack.setItemMeta(itemMeta);
        return this;
    }


    public ItemBuilder setUnbreakable() {

        itemMeta.setUnbreakable(true);

        itemStack.setItemMeta(itemMeta);

        return this;
    }

    public ItemStack build() {
        return itemStack;
    }
    public ItemStack BuildSkull(String url) {
        ItemStack head = itemStack;
        if(head.getType() != Material.PLAYER_HEAD){return head;}
        if (url.isEmpty()) return head;

        SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);

        profile.getProperties().put("textures", new Property("textures", url));

        try {
            Method mtd = skullMeta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
            mtd.setAccessible(true);
            mtd.invoke(skullMeta, profile);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
            ex.printStackTrace();
        }

        head.setItemMeta(skullMeta);
        return head;
    }
}