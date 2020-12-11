package com.sakuratown.minions.command;

import com.sakuratown.library.command.SimpleSubCommand;
import org.bukkit.command.CommandSender;

public class InventoryCommand extends SimpleSubCommand {

    InventoryCommand(String command) {
        super(command);

        setUsage("inventory");
        setDescription("打开工人背包");
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {

    }
}

