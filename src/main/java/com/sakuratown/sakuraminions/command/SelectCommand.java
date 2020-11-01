package com.sakuratown.sakuraminions.command;

import com.sakuratown.sakuraminions.Main;
import org.bukkit.command.CommandSender;

public class SelectCommand extends SimpleSubCommand {

    SelectCommand(String command) {
        super(command);

        setUsage("select");
        setDescription("选择你面前的工人");
    }

    @Override
    void onCommand(CommandSender sender, String[] args) {


    }
}

