package com.github.larryr1.verilock.commands.executors.verilock.database.lookup;

import com.github.larryr1.verilock.Verilock;
import com.github.larryr1.verilock.commands.executors.SubCommand;
import com.github.larryr1.verilock.data.PlayerIdentity;
import com.github.larryr1.verilock.data.VerificationPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LookupPlayerSubCommand implements SubCommand {
    @Override
    public void onCommand(CommandSender commandSender, Command command, String[] args) {
        if (args.length < 3) {
            commandSender.sendMessage(ChatColor.RED + "Incorrect command syntax. You must specify a player to lookup.");
            return;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayer(args[2]);

        try {

            VerificationPlayer vp = Verilock.getInstance().verificationDatabase.getPlayer(player.getUniqueId());
            LookupMessages.sendVerificationPlayerInformation(vp, player.getUniqueId(), commandSender);

            if (vp == null) return;

            if (vp.getIdentityId() == null) {
                commandSender.sendMessage("\n");
                return;
            }

            PlayerIdentity id = Verilock.getInstance().verificationDatabase.GetPlayerIdentity(vp.getIdentityId());



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
