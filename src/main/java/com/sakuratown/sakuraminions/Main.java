package com.sakuratown.sakuraminions;

import com.plotsquared.core.api.PlotAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main plugin;

    @Override
    public void onEnable() {

        plugin = this;

        String[] message = {
                "§a樱花工人插件§e v" + getDescription().getVersion() + " §a已启用",
                "§a插件制作作者:§e EnTIv §aQQ群:§e 600731934"
        };
        getServer().getConsoleSender().sendMessage(message);
    }

    @Override
    public void onDisable() {
        String[] message = {
                "§a樱花工人插件§e v" + getDescription().getVersion() + "§a已卸载",
                "§a插件制作作者:§e EnTIv §aQQ群:§e 600731934"
        };
        getServer().getConsoleSender().sendMessage(message);
    }

}
