package com.sakuratown.library.utils;

import com.sakuratown.library.menu.Button;
import com.sakuratown.library.menu.Menu;
import com.sakuratown.library.menu.PageableMenu;
import com.sakuratown.minions.Main;
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

public class Config {

    private static final HashMap<String, Config> configs = new HashMap<>();
    private final YamlConfiguration yamlConfiguration;

    private Config(String fileName) {
        File file = new File(Main.getInstance().getDataFolder(), fileName + ".yml");
        yamlConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public static Config getConfig(String name) {
        if (configs.get(name) == null) {
            configs.put(name, new Config(name));
        }
        return configs.get(name);
    }

    public static void saveDefaultConfig(String fileName) {
        File file = new File(Main.getInstance().getDataFolder(), fileName + ".yml");

        //TODO 测试代码, 方便修改配置文件, 开服记得删了
        Main.getInstance().saveResource(fileName + ".yml", true);

        if (!file.exists()) {
            Main.getInstance().saveResource(fileName + ".yml", false);
        }
    }

    public static void reload() {

        for (String fileName : configs.keySet()) {
            Config config = new Config(fileName);
            configs.put(fileName, config);
        }

    }

    public void setMenu(String path, Menu menu) {

        ConfigurationSection config = getConfigurationSection(path);

        if (config == null) return;

        String title = config.getString("Title");
        int row = config.getInt("Row");

        menu.setTitle(title);
        menu.setRow(row);

        ConfigurationSection buttons = config.getConfigurationSection("Buttons");

        for (String name : buttons.getKeys(false)) {
            Button button = getButton(buttons.getCurrentPath().concat(".").concat(name));
            menu.setButton(button);
        }
    }

    public void setPageMenu(String path, PageableMenu menu, int storage) {
        ConfigurationSection config = getConfigurationSection(path);

        if (config == null) return;

        String title = config.getString("Title");
        int row = config.getInt("Row");

        menu.setTitle(title);
        menu.setRow(row);
        menu.setTotalRow(storage);
        menu.initPages();

        ConfigurationSection buttons = config.getConfigurationSection("Buttons");

        for (String name : buttons.getKeys(false)) {
            Button button = getButton(buttons.getCurrentPath().concat("." + name));
            menu.setButton(button);
        }
    }

    public Button getButton(String path) {

        ItemStack itemStack = getItemStack(path);
        ConfigurationSection config = getConfigurationSection(path);

        String slotConfig = config.getString("slots");

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

        String type = config.getString("type");

        Material material = Material.matchMaterial(type == null ? "STONE" : type);

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

        boolean isUnbreakable = config.getBoolean("unbreakable");
        if (isUnbreakable) itemBuilder.setUnbreakable();

        return new ItemBuilder(material, amount).name(name).lore(lore).build();
    }

    public ConfigurationSection getConfigurationSection(String path) {
        return yamlConfiguration.getConfigurationSection(path);
    }
}
