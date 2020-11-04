package com.sakuratown.sakuraminions.command;

import com.sakuratown.sakuralibrary.command.SimpleSubCommand;
import org.bukkit.command.CommandSender;

public class UpgradeAmountCommand extends SimpleSubCommand {

    UpgradeAmountCommand(String command) {
        super(command);

        setUsage("upgrade amount");
        setDescription("升级工人效率");
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {

    }
}
