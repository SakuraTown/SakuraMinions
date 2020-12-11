package com.sakuratown.minions.command;

import com.sakuratown.library.command.SimpleSubCommand;
import com.sakuratown.library.utils.Config;
import com.sakuratown.library.utils.Message;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends SimpleSubCommand {

    ReloadCommand(String command) {
        super(command);

        setUsage("reload");
        setDescription("重载配置文件");
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        Config.reload();
        Message.send(sender, "&9&l樱花娘 &6&l>> &a配置文件重载成功");
    }
}

