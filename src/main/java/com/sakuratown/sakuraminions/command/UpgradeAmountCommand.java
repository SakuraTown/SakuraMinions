package com.sakuratown.sakuraminions.command;

import org.bukkit.command.CommandSender;

public class UpgradeAmountCommand extends SimpleSubCommand {

    UpgradeAmountCommand(String command) {
        super(command);

        setUsage("upgrade amount");
        setDescription("升级工人效率");
    }

    @Override
    void onCommand(CommandSender sender, String[] args) {

    }
}
