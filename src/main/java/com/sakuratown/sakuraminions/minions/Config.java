package com.sakuratown.sakuraminions.minions;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;


public class Config {
    private static Plugin plugin;
    private static FileConfiguration config;

    //工人背包配置
    private static String menuStyle;
    private static ConfigurationSection menuSection;

    public Config(Plugin _plugin){
        plugin = _plugin;
        reload();
    }
    public static void reload(){
        config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "config.yml"));
        menuLoad();
    }

    private static void menuLoad(){
        menuSection = config.getConfigurationSection("Menu");
        menuStyle = menuSection.getString("Style");
    }
    public static String getmenuStyle(){return menuStyle;}
    public static ConfigurationSection getMenuSection(){ return menuSection; }


}
