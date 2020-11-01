package com.sakuratown.sakuraminions.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MainCommand extends SimpleCommand implements CommandExecutor {

    public MainCommand() {
        registerSubCommands();
    }

    @Override
    public void registerSubCommands() {
        registerSubCommand(new SelectCommand("select"));
        registerSubCommand(new InventoryCommand("inventory"));
        registerSubCommand(new UpgradeAmountCommand("upgrade amount"));
        registerSubCommand(new UpgradeAmountCommand("upgrade inventory"));
        registerSubCommand(new GiveCommand("give"));
        registerSubCommand(new ReloadCommand("reload"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {

            sendHelpMessage(sender);

        } else {

            boolean match = false;

            for (SimpleSubCommand subCommand : subCommands) {

                if (subCommand.command.equalsIgnoreCase(args[0])) {

                    match = true;
                    subCommand.onCommand(sender, args);

                    break;
                }
            }

            if (!match) sendHelpMessage(sender);
        }

        return true;
    }
}