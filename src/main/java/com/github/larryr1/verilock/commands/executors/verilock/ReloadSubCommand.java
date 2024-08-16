package com.github.larryr1.verilock.commands.executors.verilock;

import com.github.larryr1.verilock.Verilock;
import com.github.larryr1.verilock.commands.executors.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ReloadSubCommand implements SubCommand {
    @Override
    public void onCommand(CommandSender sender, Command command, String[] args) {
        sender.sendMessage(ChatColor.YELLOW + "Reloading Verilock configuration...");
        Verilock.getInstance().TriggerReload();
        sender.sendMessage(ChatColor.GREEN + "Configuration reloaded!");
    }

    @Override
    public String getPermission() {
        return "verilock.reload";
    }
}
