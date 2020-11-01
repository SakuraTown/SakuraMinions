package com.sakuratown.sakuraminions.minions;
import com.sakuratown.sakuraminions.Main;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;


public class Config {

    private static FileConfiguration config;

    //工人背包配置
    private static String menuStyle;
    private static ConfigurationSection menuSection;

    public Config(){
        reload();
    }

    public static String getMenuStyle() {
        return menuStyle;
    }

    public static ConfigurationSection getMenuSection() {
        return menuSection;
    }

    public static void reload(){
        config = YamlConfiguration.loadConfiguration(new File(Main.getInstance().getDataFolder(), "config.yml"));
        menuLoad();
    }

    private static void menuLoad(){
        menuSection = config.getConfigurationSection("Menu");
        menuStyle = menuSection.getString("Style");
    }


}
