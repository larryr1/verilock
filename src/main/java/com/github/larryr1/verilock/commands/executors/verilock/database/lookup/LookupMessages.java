package com.github.larryr1.verilock.commands.executors.verilock.database.lookup;

import com.github.larryr1.verilock.Verilock;
import com.github.larryr1.verilock.data.PlayerIdentity;
import com.github.larryr1.verilock.data.VerificationPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class LookupMessages {
    public static void sendVerificationPlayerInformation(VerificationPlayer vp, UUID uuid, CommandSender cs) {

        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);

        cs.sendMessage(ChatColor.GOLD + "Player Information");
        cs.sendMessage(ChatColor.AQUA + "Username: " + player.getName());
        cs.sendMessage(ChatColor.AQUA + "Online: " + (player.isOnline() ? "Yes" : "No" ));
        cs.sendMessage(ChatColor.AQUA + "UUID: " + player.getUniqueId());


        if (vp == null) {
            cs.sendMessage(ChatColor.GRAY + "This player does not have a Verilock record. They probably haven't joined before.");
        } else {
            cs.sendMessage(ChatColor.AQUA + "Identity ID: " + ((vp.getIdentityId() == null) ? "(NOT LINKED)" : vp.getIdentityId()));
        }

        if (!player.isOnline()) {
            cs.sendMessage(ChatColor.GRAY + "This player is not online and may not be the correct player. Compare UUIDs of the player you are trying to lookup to be sure.");
        }
    }

    public static void sendIdentityInformation(PlayerIdentity id, CommandSender cs) {
        cs.sendMessage("\n" + ChatColor.GOLD + "Linked Identity Information");
        cs.sendMessage(ChatColor.AQUA + "ID Number: " + id.getIdNumber());
        cs.sendMessage(ChatColor.AQUA + "First Name: " + ((id.getFirstName() == null) ? "(NO DATA)" : id.getFirstName()));
        cs.sendMessage(ChatColor.AQUA + "Last Name: " + ((id.getLastName() == null) ? "(NO DATA)" : id.getLastName()));
        cs.sendMessage(ChatColor.AQUA + "Birthday: " + ((id.getBirthday() == null) ? "(NO DATA)" : id.getBirthday()));
        cs.sendMessage(ChatColor.AQUA + "Age: " + ((id.getAge() == 0) ? "0 (NO DATA)" : id.getAge()));
        cs.sendMessage(ChatColor.AQUA + "Grade: " + ((id.getGrade() == 0) ? "0 (NO DATA)" : id.getGrade()));
        cs.sendMessage(ChatColor.AQUA + "House: " + ((id.getHouse() == null) ? "(UNSORTED)" : id.getHouse()));
        cs.sendMessage("\n");
    }
}
