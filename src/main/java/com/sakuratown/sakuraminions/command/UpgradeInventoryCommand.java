package com.sakuratown.sakuraminions.command;

import com.sakuratown.sakuralibrary.command.SimpleSubCommand;
import org.bukkit.command.CommandSender;

public class UpgradeInventoryCommand extends SimpleSubCommand {

    UpgradeInventoryCommand(String command) {
        super(command);

        setUsage("upgrade inventory");
        setDescription("升级工人背包");
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {

    }
}
