package com.sakuratown.sakuralibrary.command;

import com.sakuratown.sakuralibrary.utils.Message;
import com.sakuratown.sakuraminions.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class SimpleCommand implements TabCompleter {

    Set<SimpleSubCommand> subCommands = new HashSet<>();

    public void registerSubCommand(SimpleSubCommand subCommand) {
        subCommands.add(subCommand);
    }

    public abstract void registerSubCommands();

    public void sendHelpMessage(CommandSender sender, String[] args) {

        if (args.length == 0) {

            Message.send(sender, getHelpMessage(sender));

        } else {

            for (SimpleSubCommand subCommand : subCommands) {

                if (subCommand.command.equalsIgnoreCase(args[0])) {

                    subCommand.onCommand(sender, args);

                    return;
                }
            }

             Message.send(sender, getHelpMessage(sender));
        }
    }

    private List<String> getHelpMessage(CommandSender sender) {

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

        return helpMessage;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> tabComplete = new ArrayList<>();
            for (SimpleSubCommand subCommand : subCommands) {
                tabComplete.add(subCommand.command);
            }
            tabComplete.removeIf(s -> !s.startsWith(args[0]));
            return tabComplete;
        }
        return null;
    }
}
