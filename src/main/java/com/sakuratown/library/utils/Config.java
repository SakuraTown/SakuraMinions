package com.sakuratown.library.utils;

import com.sakuratown.library.menu.Button;
import com.sakuratown.library.menu.Menu;
import com.sakuratown.sakuraminions.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Config {

    private static final HashMap<String, Config> configs = new HashMap<>();
    private final YamlConfiguration configuration;

    public Config(String fileName) {
        File file = new File(Main.getInstance().getDataFolder().getParent(), fileName + ".yml");
        configuration = YamlConfiguration.loadConfiguration(file);
    }

    public static Config getConfig(String name) {
        configs.putIfAbsent(name, new Config(name));
        return configs.get(name);
    }

    public void reload() {

        for (String fileName : configs.keySet()) {
            Config config = new Config(fileName);
            configs.put(fileName, config);
        }

    }

    public ConfigurationSection getConfigurationSection(String path) {

        ConfigurationSection configurationSection = configuration.getConfigurationSection(path);

        if (configurationSection == null) {
            throw new NullPointerException("配置文件有误, 请仔细检查配置文件");
        }

        return configurationSection;

    }

    public Menu getMenu(String path) {

        ConfigurationSection config = getConfigurationSection(path);

        String title = config.getString("Title");
        int row = config.getInt("Row");

        return new Menu(title, row);
    }

    public Button getButton(String path) {

        ItemStack itemStack = getItemStack(path);
        ConfigurationSection config = getConfigurationSection(path);

        String slotConfig = Objects.requireNonNullElse(config.getString("slots"), "1");

        int[] slots;

        if (slotConfig.contains(",")) {

            String[] split = slotConfig.split(",");
            slots = new int[split.length];

            for (int i = 0; i < split.length; i++) {
                slots[i] = Integer.parseInt(split[i]);
            }

        } else {

            slots = new int[1];
            slots[0] = Integer.parseInt(slotConfig);

        }

        Button button = new Button(itemStack, slots);
        List<String> commands = config.getStringList("commands");

        button.action = config.getString("action");
        button.setCommands(commands);

        return button;
    }

    public ItemStack getItemStack(String path) {

        ConfigurationSection config = getConfigurationSection(path);
        Material material = Material.matchMaterial(Objects.requireNonNullElse(config.getString("mats"), "STONE"));

        String name = config.getString("name");
        int amount = config.getInt("amount", 1);

        List<String> lore = config.getStringList("lore");
        List<String> flag = config.getStringList("flag");
        List<String> enchantment = config.getStringList("enchantment");

        ItemBuilder itemBuilder = new ItemBuilder(material, amount).name(name).lore(lore);

        for (String string : flag) {
            ItemFlag itemFlag = ItemFlag.valueOf(string);
            itemBuilder.addFlag(itemFlag);
        }

        for (String string : enchantment) {

            String[] split = string.split(":");
            NamespacedKey namespacedKey = NamespacedKey.minecraft(split[0]);
            Enchantment enchant = Enchantment.getByKey(namespacedKey);

            int level = Integer.parseInt(split[1]);

            itemBuilder.addEnchantment(enchant, level);

        }

        boolean isUnbreakable = config.getBoolean("unbreakable", false);
        if (isUnbreakable) itemBuilder.setUnbreakable();

        return new ItemBuilder(material, amount).name(name).lore(lore).build();
    }

}
