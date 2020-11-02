package com.sakuratown.sakuraminions.command;

import org.bukkit.command.CommandSender;

public class UpgradeInventoryCommand extends SimpleSubCommand {

    UpgradeInventoryCommand(String command) {
        super(command);

        setUsage("upgrade inventory");
        setDescription("升级工人背包");
    }

    @Override
    void onCommand(CommandSender sender, String[] args) {

    }
}
