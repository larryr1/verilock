package com.github.larryr1.verilock.commands.executors.verilock;

import com.github.larryr1.verilock.commands.executors.SubCommand;
import com.github.larryr1.verilock.commands.executors.verilock.database.DatabaseSubCommand;
import com.github.larryr1.verilock.commands.executors.verilock.identity.IdentitySubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class VerilockCommandExecutor implements CommandExecutor {

    private Map<String, SubCommand> subCommands = new HashMap<>();

    public VerilockCommandExecutor() {
        subCommands.put("identities", new IdentitySubCommand());
        subCommands.put("database", new DatabaseSubCommand());
        subCommands.put("reload", new ReloadSubCommand());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (args.length < 1) {
            return true;
        }

        if (!subCommands.containsKey(args[0].toLowerCase())) {
            commandSender.sendMessage(ChatColor.RED + "Incorrect command syntax. That subcommand doesn't exist.");
            return true;
        }

        SubCommand sc = subCommands.get(args[0].toLowerCase());
        if (commandSender.hasPermission(sc.getPermission().toLowerCase()) || commandSender.isOp()) {
            sc.onCommand(commandSender, command, args);
        } else {
            commandSender.sendMessage(ChatColor.RED + "You don't have permission to use that command.");
            return true;
        }

        return true;
    }
}
