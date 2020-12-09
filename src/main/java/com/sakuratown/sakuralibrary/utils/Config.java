package com.sakuratown.sakuralibrary.utils;

import com.sakuratown.sakuraminions.Main;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {

    public static ConfigurationSection getConfigurationSection(String path) {
        FileConfiguration config = Main.getInstance().getConfig();
        ConfigurationSection configurationSection = config.getConfigurationSection(path);

        if (configurationSection == null) {
            throw new NullPointerException("配置文件有误, 请检查配置文件");
        }

        return configurationSection;
    }
}
