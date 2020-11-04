package com.sakuratown.sakuraminions.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public class MainCommand extends SimpleCommand {

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
}