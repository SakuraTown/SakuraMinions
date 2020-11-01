package com.sakuratown.sakuraminions.command;

import com.sakuratown.sakuraminions.Main;
import com.sakuratown.sakuraminions.minions.Config;
import com.sakuratown.sakuraminions.utils.Message;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends SimpleSubCommand {

    ReloadCommand(String command) {
        super(command);

        setUsage("reload");
        setDescription("重载配置文件");
    }

    @Override
    void onCommand(CommandSender sender, String[] args) {
        Config.reloadConfig();
        Message.send(sender,"&9&l樱花娘 &6&l>> &a配置文件重载成功");
    }
}

