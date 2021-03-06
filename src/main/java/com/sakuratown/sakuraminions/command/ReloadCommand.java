package com.sakuratown.sakuraminions.command;

import com.sakuratown.sakuralibrary.command.SimpleSubCommand;
import com.sakuratown.sakuraminions.minions.Config;
import com.sakuratown.sakuralibrary.utils.Message;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends SimpleSubCommand {

    ReloadCommand(String command) {
        super(command);

        setUsage("reload");
        setDescription("重载配置文件");
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        Config.reloadConfig();
        Message.send(sender, "&9&l樱花娘 &6&l>> &a配置文件重载成功");
    }
}

