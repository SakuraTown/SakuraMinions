package com.sakuratown.sakuraminions.command;

import com.sakuratown.library.command.SimpleCommand;
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
        registerSubCommand(new UpgradeCommand("upgrade"));
        registerSubCommand(new GiveCommand("give"));
        registerSubCommand(new ReloadCommand("reload"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sendHelpMessage(sender, args);
        return true;
    }
}