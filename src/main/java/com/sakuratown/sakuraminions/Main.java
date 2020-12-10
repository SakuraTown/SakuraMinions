package com.sakuratown.sakuraminions;

import com.sakuratown.library.utils.Message;
import com.sakuratown.sakuraminions.command.MainCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin {

    private static Main plugin;
    public static Main getInstance() {
        return plugin;
    }

    @Override
    public void onEnable() {

        plugin = this;
        String[] message = {
                "§a樱花工人插件§e v" + getDescription().getVersion() + " §a已启用",
                "§a插件制作作者:§e EnTIv §aQQ群:§e 600731934"
        };

        Message.sendConsole(message);
        saveDefaultConfig();
        PluginCommand command = Bukkit.getPluginCommand("SakuraMinions");

        if (command != null) {
            command.setExecutor(new MainCommand());
            command.setTabCompleter(new MainCommand());
        }
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
