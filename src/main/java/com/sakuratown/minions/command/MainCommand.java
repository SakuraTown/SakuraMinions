package com.sakuratown.minions.command;

import com.sakuratown.library.command.SimpleCommand;
import com.sakuratown.library.menu.Button;
import com.sakuratown.library.menu.InventoryMenu;
import com.sakuratown.minions.minion.Minion;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommand extends SimpleCommand implements CommandExecutor {

    Minion minion = new Minion("Miner", 15, 64);
    
    public MainCommand() {
        registerSubCommands();
    }

    @Override
    public void registerSubCommands() {
        registerSubCommand(new GiveCommand("give"));
        registerSubCommand(new ReloadCommand("reload"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sendHelpMessage(sender, args);
        Player player = (Player) sender;

        minion.openStorageMenu(player);

        return true;
    }
}