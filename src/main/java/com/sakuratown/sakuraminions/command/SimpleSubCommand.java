package com.sakuratown.sakuraminions.command;

import com.sakuratown.sakuraminions.Main;
import org.bukkit.command.CommandSender;

public abstract class SimpleSubCommand {

    String command;

    String usage;
    String description;
    String permission;

    SimpleSubCommand(String subCommand) {
        this.command = subCommand;
        this.permission = Main.getInstance().getName() + "." + subCommand;
    }

    abstract void onCommand(CommandSender sender, String[] args);

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

}
