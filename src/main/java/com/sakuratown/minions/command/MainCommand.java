package com.sakuratown.minions.command;

import com.sakuratown.library.command.SimpleCommand;
import com.sakuratown.library.utils.Config;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MainCommand extends SimpleCommand implements CommandExecutor {

    public MainCommand() {
        registerSubCommands();
    }

    @Override
    public void registerSubCommands() {
        registerSubCommand(new GiveCommand("give"));
        registerSubCommand(new ReloadCommand("reload"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sendHelpMessage(sender, args);
//        Minion minion = new Minion("Miner", 100, 64);
//        minion.openManagerMenu((Player) sender);
        Config config = Config.getConfig("menu");

        Config buttons = config.getConfigurationSection("管理菜单").getConfigurationSection("Buttons");

        //TODO 循环调用问题处理, 结果为 管理菜单.buttons.蓝色玻璃板.品红色玻璃板.淡蓝色玻璃板... 应为管理菜单.buttons.蓝色玻璃板
        // 原因在于我的是返回 config 对象, 而应该返回新的才对, 要不设置两个方法, 一个返回 ConfigurationSection 一个返回 config
        for (String key : buttons.getKeys(false)) {
            config.getConfigurationSection(key);
        }

        return true;
    }
}