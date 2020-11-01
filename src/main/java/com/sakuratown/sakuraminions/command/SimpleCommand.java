package com.sakuratown.sakuraminions.command;

import com.sakuratown.sakuraminions.Main;
import com.sakuratown.sakuraminions.utils.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class SimpleCommand {

    Set<SimpleSubCommand> subCommands = new HashSet<>();

    void registerSubCommand(SimpleSubCommand subCommand) {
        subCommands.add(subCommand);
    }

    public abstract void registerSubCommands();

    void sendHelpMessage(CommandSender sender) {

        Main plugin = Main.getInstance();

        List<String> helpMessage = new ArrayList<>();

        helpMessage.add("&6&m+------------------+&9&l " + plugin.getName() + " &6&m+------------------+");
        helpMessage.add("");

        for (SimpleSubCommand subCommand : subCommands) {
            if (sender.hasPermission(subCommand.permission) || sender.isOp()) {
                helpMessage.add("&d/minions"  + " " + subCommand.usage + "&e " + subCommand.description);
            }
        }
        helpMessage.add("");

        Message.send(sender, helpMessage);
    }
}
