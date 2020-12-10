package com.sakuratown.sakuraminions.command;

import com.sakuratown.library.command.SimpleSubCommand;
import org.bukkit.command.CommandSender;

public class UpgradeCommand extends SimpleSubCommand {

    UpgradeCommand(String command) {
        super(command);

        setUsage("upgrade efficiency|storage");
        setDescription("升级工人效率");
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (args[1].equals("efficiency")) {

        } else if (args[1].equals("Storage")) {

        }
    }
}
