package com.sakuratown.minions.command;

import com.sakuratown.library.command.SimpleSubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveCommand extends SimpleSubCommand {

    GiveCommand(String command) {
        super(command);

        setUsage("give [玩家] <类型> <背包大小> <效率>");
        setDescription("给予玩家一个工人");
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            return;
        }
        Player player = (Player) sender;
    }
}
