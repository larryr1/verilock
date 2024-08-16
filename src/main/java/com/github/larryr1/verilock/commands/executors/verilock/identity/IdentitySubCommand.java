package com.github.larryr1.verilock.commands.executors.verilock.identity;

import com.github.larryr1.verilock.Verilock;
import com.github.larryr1.verilock.commands.executors.SubCommand;
import com.github.larryr1.verilock.data.importing.IdentityImportResult;
import com.github.larryr1.verilock.data.importing.IdentityImporter;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class IdentitySubCommand implements SubCommand {
    @Override
    public void onCommand(CommandSender commandSender, Command command, String[] args) {

        // Check for arguments
        if (args.length < 2) {
            commandSender.sendMessage(ChatColor.RED + "Incorrect usage.");
            commandSender.sendMessage(ChatColor.RED + "Look at the tab completions for valid options.");
            return;
        }

        // Import identities from file
        if (args[1].equalsIgnoreCase("import")) {
            commandSender.sendMessage(ChatColor.YELLOW + "Starting identity import...");


            try {
                IdentityImportResult result = IdentityImporter.readData();

                commandSender.sendMessage(ChatColor.GREEN + "Done! " + result.getIdentitiesAdded() + " identities imported in " + result.getMsDuration() + "ms.");
                if (result.getIdentitiesSkipped() > 0) {
                    commandSender.sendMessage(ChatColor.GRAY + "" + result.getIdentitiesSkipped() + " identities skipped because they already exist in the database.");
                }

            } catch (Exception e) {
                commandSender.sendMessage(ChatColor.RED + "There was an error importing player identities. The error has been logged to the server console.");
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public String getPermission() {
        return "verilock.identity";
    }
}
