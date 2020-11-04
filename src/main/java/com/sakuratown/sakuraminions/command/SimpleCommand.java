package com.sakuratown.sakuraminions.command;

import com.sakuratown.sakuraminions.Main;
import com.sakuratown.sakuraminions.utils.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class SimpleCommand implements CommandExecutor, TabCompleter {

    Set<SimpleSubCommand> subCommands = new HashSet<>();

    void registerSubCommand(SimpleSubCommand subCommand) {
        subCommands.add(subCommand);
    }

    public abstract void registerSubCommands();

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

    void sendHelpMessage(CommandSender sender) {

        Main plugin = Main.getInstance();

        List<String> helpMessage = new ArrayList<>();

        helpMessage.add("&6&m+------------------+&9&l " + plugin.getName() + " &6&m+------------------+");
        helpMessage.add("");

        for (SimpleSubCommand subCommand : subCommands) {
            if (sender.hasPermission(subCommand.permission) || sender.isOp()) {
                helpMessage.add("&d/minions" + " " + subCommand.usage + "&e " + subCommand.description);
            }
        }
        helpMessage.add("");

        Message.send(sender, helpMessage);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> tabComplete = new ArrayList<>();
        for (SimpleSubCommand subCommand : subCommands) {
            tabComplete.add(subCommand.command);
        }
        return tabComplete;
    }
}
