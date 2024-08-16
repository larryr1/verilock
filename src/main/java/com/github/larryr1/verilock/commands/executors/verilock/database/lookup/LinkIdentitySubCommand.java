package com.github.larryr1.verilock.commands.executors.verilock.database.lookup;

import com.github.larryr1.verilock.Verilock;
import com.github.larryr1.verilock.commands.executors.SubCommand;
import com.github.larryr1.verilock.data.PlayerIdentity;
import com.github.larryr1.verilock.data.VerificationPlayer;
import com.github.larryr1.verilock.manager.PlayerTimeoutManager;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class LinkIdentitySubCommand implements SubCommand {
    @Override
    public void onCommand(CommandSender commandSender, Command command, String[] args) {
        if (args.length < 3) {
            commandSender.sendMessage(ChatColor.RED + "Incorrect command syntax. You must specify a player to link.");
            return;
        }

        if (args.length < 4) {
            commandSender.sendMessage(ChatColor.RED + "Incorrect command syntax. You must specify an identity to link.");
            return;
        }

        Player player = Bukkit.getPlayer(args[2]);
        if (player == null) {
            commandSender.sendMessage(ChatColor.RED + "Couldn't find player " + args[2]);
            return;
        }

        PlayerIdentity identity = Verilock.getInstance().verificationDatabase.GetPlayerIdentity(args[3]);
        if (identity == null) {
            commandSender.sendMessage(ChatColor.RED + "Couldn't find identity " + args[3]);
            return;
        }

        // Update the player's verification status
        VerificationPlayer vp = Verilock.getInstance().verificationDatabase.GetPlayerByIdentityId(identity.getIdNumber());
        if (vp != null) {
            commandSender.sendMessage(ChatColor.RED + "This identity is already linked to another player:");
            commandSender.sendMessage("- Player UUID " + vp.getUuid());
            Player playerUsing = Bukkit.getPlayer(vp.getUuid());
            if (playerUsing != null) {
                commandSender.sendMessage("- Player Username " + playerUsing.getName());
            }

            commandSender.sendMessage(ChatColor.RED + "Unlink that identity before linking it to another player.");

            return;
        }

        Verilock.getInstance().verificationDatabase.LinkPlayerIdentity(player.getUniqueId(), identity);

        // Set LP perm
        Node verifiedNode = Node.builder(Verilock.getInstance().getConfig().getString("permissions.verifiedPermission"))
                .value(true)
                .build();

        String path = "permissions.permissionByHouse." + identity.getHouse().toLowerCase();
        Node houseNode = Node.builder(Verilock.getInstance().getConfig().getString(path))
                .value(true)
                .build();

        path = "permissions.permissionByGrade.grade-" + identity.getGrade();
        Node gradeNode = Node.builder(Verilock.getInstance().getConfig().getString(path))
                .value(true)
                .build();

        // Load, modify, and save user
        UserManager userManager = Verilock.getInstance().GetPermissionProvider().getProvider().getUserManager();
        userManager.modifyUser(player.getUniqueId(), user -> {
            user.data().add(houseNode);
            user.data().add(gradeNode);
            user.data().add(verifiedNode);
        });

        // Cancel the player's verification timeout
        PlayerTimeoutManager.cancelPlayerTimeout(player);

        commandSender.sendMessage(ChatColor.GREEN + "Identity linked.");
    }

    @Override
    public String getPermission() {
        return "verilock.database";
    }
}
