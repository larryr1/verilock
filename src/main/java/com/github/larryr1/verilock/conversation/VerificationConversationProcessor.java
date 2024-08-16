package com.github.larryr1.verilock.conversation;

import com.github.larryr1.verilock.Verilock;
import com.github.larryr1.verilock.data.PlayerIdentity;
import com.github.larryr1.verilock.data.VerificationPlayer;
import com.github.larryr1.verilock.manager.PlayerTimeoutManager;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.CompletableFuture;

public class VerificationConversationProcessor {
    public static void ProcessConversationResult(ConversationContext context) {
        Player player = (Player) context.getForWhom();
        player.sendMessage(ChatColor.YELLOW + "Searching for student record...");

        String studentId = (String) context.getSessionData("studentId");
        String studentBirthday = (String) context.getSessionData("studentBirthday");
        PlayerIdentity identity = Verilock.getInstance().verificationDatabase.VerifyIdentity(studentId, studentBirthday);

        if (identity == null) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1, 0);
            player.sendMessage(ChatColor.RED + "The information you entered is incorrect. Please try running " + ChatColor.GRAY + "/verify" + ChatColor.RED + " again.");
            player.sendMessage(ChatColor.GRAY + "If you are a new student there is a chance your record is not in the database. Please email\nrowe.larry@somersetcollegeprep.org to have it fixed.");
            return;
        }

        // Update the player's verification status
        VerificationPlayer vp = Verilock.getInstance().verificationDatabase.GetPlayerByIdentityId(identity.getIdNumber());
        if (vp != null) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1, 0);
            player.sendMessage(ChatColor.RED + "Another player has already verified with this identity.");
            player.sendMessage(ChatColor.GRAY + "You can only verify one account at a time. To unverify your other account, run /unverify on that account. If you haven't verified any other accounts, someone else has claimed your identity. This is a security issue. Email \nrowe.larry@somersetcollegeprep.org to help us fix it.");
            player.sendMessage("\nYou will be automatically disconnected in 1 minute.");
            PlayerTimeoutManager.cancelPlayerTimeout(player);
            PlayerTimeoutManager.addPlayerTimeout(player, 1200); // 1 minute
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

        // Send the player a greeting
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        String fixedName = identity.getFirstName();
        fixedName = fixedName.substring(0, 1).toUpperCase() + fixedName.substring(1).toLowerCase();
        player.sendMessage("\n" + ChatColor.GREEN + "Welcome to PlaySCPA, " + fixedName + '!');
        player.sendMessage(ChatColor.GOLD + "You will be teleported to spawn in 10 seconds.\n" + ChatColor.RED + "Please read and abide by all server rules or real-life consequences may follow.");

        // Teleport the player in 10 seconds
        new BukkitRunnable() {
            @Override
            public void run() {
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                Verilock.getInstance().GetTeleportManager().TeleportPlayerToSpawn(player);
            }
        }.runTaskLater(Verilock.getInstance(), 200);
    }
}
