package com.github.larryr1.verilock.commands.executors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface SubCommand {
    void onCommand(CommandSender sender, Command command, String[] args);
    String getPermission();
}