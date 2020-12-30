package com.sakuratown.minions;

import com.sakuratown.library.menu.MenuListener;
import com.sakuratown.library.utils.Config;
import com.sakuratown.library.utils.Message;
import com.sakuratown.minions.command.MainCommand;
import com.sakuratown.minions.listener.PlayerListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin {

    private static Main plugin;
    public static Main getInstance() {
        return plugin;
    }
    private static Economy eco = null;

    @Override
    public void onEnable() {

        plugin = this;
        setupEconomy();

        String[] message = {
                "§a樱花工人插件§e v" + getDescription().getVersion() + " §a已启用",
                "§a插件制作作者:§e EnTIv §aQQ群:§e 600731934"
        };

        Message.sendConsole(message);
        saveDefaultConfig();
        PluginCommand command = Bukkit.getPluginCommand("SakuraMinions");

        if (command != null) {
            MainCommand executor = new MainCommand();

            command.setExecutor(executor);
            command.setTabCompleter(executor);
        }

        setupConfig();
        getServer().getPluginManager().registerEvents(new MenuListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    }

    @Override
    public void onDisable() {
        String[] message = {
                "§a樱花工人插件§e v" + getDescription().getVersion() + " §a已卸载",
                "§a插件制作作者:§e EnTIv §aQQ群:§e 600731934"
        };
        getServer().getConsoleSender().sendMessage(message);
    }

    private void setupConfig() {
        saveDefaultConfig();

        Config.saveDefaultConfig("skin");
        Config.saveDefaultConfig("minions");
        Config.saveDefaultConfig("menu");
    }

    private void setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") != null) {
            RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp == null) return;
            eco = rsp.getProvider();
        } else {
            String message = "&c未检测到&e Vault &c插件, 樱花工人插件即将卸载";
            Message.sendConsole(message);
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    public static Economy getEconomy() {
        return eco;
    }
}
