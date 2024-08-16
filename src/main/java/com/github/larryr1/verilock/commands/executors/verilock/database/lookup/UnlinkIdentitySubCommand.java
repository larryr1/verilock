package com.github.larryr1.verilock.commands.executors.verilock.database.lookup;

import com.github.larryr1.verilock.Verilock;
import com.github.larryr1.verilock.commands.executors.SubCommand;
import com.github.larryr1.verilock.data.PlayerIdentity;
import com.github.larryr1.verilock.data.VerificationPlayer;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnlinkIdentitySubCommand implements SubCommand {
    @Override
    public void onCommand(CommandSender commandSender, Command command, String[] args) {
        if (args.length < 3) {
            commandSender.sendMessage(ChatColor.RED + "Incorrect command syntax. You must specify a player to unlink.");
            return;
        }

        Player player = Bukkit.getPlayer(args[2]);
        if (player == null) {
            commandSender.sendMessage(ChatColor.RED + "Couldn't find player " + args[2]);
            return;
        }

        try {
            VerificationPlayer vp = Verilock.getInstance().verificationDatabase.GetOrCreatePlayer(player.getUniqueId());
            boolean result = Verilock.getInstance().verificationDatabase.UnlinkPlayerIdentity(player.getUniqueId());

            if (!result) {
                commandSender.sendMessage(ChatColor.RED + "Nothing changed. That player isn't linked to an identity.");
                return;
            }

            PlayerIdentity identity = Verilock.getInstance().verificationDatabase.GetPlayerIdentity(vp.getIdentityId());

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
                user.data().remove(houseNode);
                user.data().remove(gradeNode);
                user.data().remove(verifiedNode);
            });

            commandSender.sendMessage(ChatColor.GREEN + "Successfully unlinked that player's identity.");

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
