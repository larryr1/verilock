package com.github.larryr1.verilock.commands.executors.verilock.database.lookup;

import com.github.larryr1.verilock.Verilock;
import com.github.larryr1.verilock.commands.executors.SubCommand;
import com.github.larryr1.verilock.data.PlayerIdentity;
import com.github.larryr1.verilock.data.VerificationPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class LookupIdentitySubCommand implements SubCommand {
    @Override
    public void onCommand(CommandSender commandSender, Command command, String[] args) {
        if (args.length < 3) {
            commandSender.sendMessage(ChatColor.RED + "Incorrect command syntax. You must specify the ID number of an identity to lookup.");
            return;
        }

        try {
            PlayerIdentity id = Verilock.getInstance().verificationDatabase.GetPlayerIdentity(args[2]);

            if (id == null) {
                commandSender.sendMessage(ChatColor.RED + "There are no identities with the specified ID number.");
                return;
            }

            LookupMessages.sendIdentityInformation(id, commandSender);

            VerificationPlayer vp = Verilock.getInstance().verificationDatabase.GetPlayerByIdentityId(id.getIdNumber());

            if (vp == null) {
                commandSender.sendMessage("\n\n" + ChatColor.AQUA + "There are no players linked to this identity.\n");
                return;
            }

            commandSender.sendMessage("\n\n" + ChatColor.GOLD + "Additionally, the following player is linked to this identity.");
            LookupMessages.sendVerificationPlayerInformation(vp, vp.getUuid(), commandSender);

        } catch (Exception e) {
            commandSender.sendMessage(ChatColor.RED + "There was an error retrieving database information. The error has been logged to the server console.");
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getPermission() {
        return "verilock.database";
    }
}
