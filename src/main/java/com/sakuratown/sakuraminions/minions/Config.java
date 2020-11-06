package com.sakuratown.sakuraminions.minions;

import com.sakuratown.sakuraminions.Main;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;



public class Config {

    private static FileConfiguration config;
    private static MenuButton menuConfig;
    //工人背包配置
    private static String menuStyle;
    private static ConfigurationSection menuSection;

    public static String getMenuStyle() {
        return menuStyle;
    }

    public static ConfigurationSection getMinionSection(String type) {
        return config.getConfigurationSection("Minions." + type);
    }

    public static ConfigurationSection getMenuSection() {
        return menuSection;
    }
    public static MenuButton getMenuConfig(){return menuConfig;}
    public static void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(new File(Main.getInstance().getDataFolder(), "config.yml"));
        menuLoad();
        menuConfig = new MenuButton();
    }

    private static void menuLoad() {
        menuSection = config.getConfigurationSection("Menu");
        menuStyle = menuSection.getString("Style");
    }
}
