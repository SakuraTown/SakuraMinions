package com.sakuratown.minions.command;

import com.sakuratown.library.command.SimpleCommand;
import com.sakuratown.minions.minion.Minion;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
        Minion minion = new Minion("Miner", 9, 10);
        minion.openManagerMenu((Player) sender);
        return true;
    }
}