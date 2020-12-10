package com.sakuratown.library.menu;

import com.sakuratown.library.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Consumer;

public class Button {

    private final ItemStack itemStack;
    public boolean isLock = true;
    public Consumer<InventoryClickEvent> clickEvent;
    public String action;
    int[] slots;
    private List<String> commands;

    public Button(ItemStack itemStack, int... slots) {
        this.itemStack = itemStack;
        this.slots = slots;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }

    void runCommand(CommandSender commandSender) {

        if (commands == null || commands.isEmpty()) return;

        for (String string : commands) {
            int separator = string.indexOf(" ");
            String type = string.substring(0, separator);
            String command = string.substring(separator + 1).replace("%player%", commandSender.getName());

            switch (type) {
                case "[CONSOLE]":
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                    break;

                case "[PLAYER]":

                    if (commandSender instanceof Player) {
                        Player player = (Player) commandSender;
                        player.chat(command);
                    }
                    break;

                case "[OP]":

                    if (commandSender instanceof Player) {
                        Player player = (Player) commandSender;

                        if (player.isOp()) {
                            player.chat(command);
                        } else {
                            player.setOp(true);
                            player.chat(command);
                            player.setOp(false);
                        }

                    }
                    break;

                default:
                    Message.send(Bukkit.getConsoleSender(), "&9&l樱花核心插件&6&l >> &c命令类型&b&l " + type + "&c 配置错误,请仔细检查配置");
            }
        }

    }

}
