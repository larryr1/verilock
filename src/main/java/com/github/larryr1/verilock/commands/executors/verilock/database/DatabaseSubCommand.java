package com.github.larryr1.verilock.commands.executors.verilock.database;

import com.github.larryr1.verilock.commands.executors.SubCommand;
import com.github.larryr1.verilock.commands.executors.verilock.database.lookup.LinkIdentitySubCommand;
import com.github.larryr1.verilock.commands.executors.verilock.database.lookup.LookupIdentitySubCommand;
import com.github.larryr1.verilock.commands.executors.verilock.database.lookup.LookupPlayerSubCommand;
import com.github.larryr1.verilock.commands.executors.verilock.database.lookup.UnlinkIdentitySubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class DatabaseSubCommand implements SubCommand {

    private Map<String, SubCommand> subCommands = new HashMap<>();

    public DatabaseSubCommand() {
        subCommands.put("lookup_player", new LookupPlayerSubCommand());
        subCommands.put("lookup_identity", new LookupIdentitySubCommand());
        subCommands.put("unlink_identity", new UnlinkIdentitySubCommand());
        subCommands.put("link_identity", new LinkIdentitySubCommand());
    }

    @Override
    public void onCommand(CommandSender commandSender, Command command, String[] args) {
        if (args.length < 2) {
            commandSender.sendMessage(ChatColor.RED + "Incorrect command syntax. Specify a subcommand.");
            return;
        }

        if (!subCommands.containsKey(args[1].toLowerCase())) {
            commandSender.sendMessage(ChatColor.RED + "Incorrect command syntax. That subcommand doesn't exist.");
            return;
        }

        SubCommand sc = subCommands.get(args[1].toLowerCase());
        if (commandSender.hasPermission(sc.getPermission().toLowerCase()) || commandSender.isOp()) {
            sc.onCommand(commandSender, command, args);
        } else {
            commandSender.sendMessage(ChatColor.RED + "You don't have permission to use that command.");
            return;
        }
    }

    @Override
    public String getPermission() {
        return "verilock.database";
    }
}
