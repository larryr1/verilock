package com.github.larryr1.verilock.events;


import com.github.larryr1.verilock.Verilock;
import com.github.larryr1.verilock.conversation.Messages;
import com.github.larryr1.verilock.data.PlayerIdentity;
import com.github.larryr1.verilock.data.VerificationPlayer;
import com.github.larryr1.verilock.manager.PlayerTimeoutManager;
import com.github.larryr1.verilock.manager.TeleportManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.sql.SQLException;

public class PlayerJoinHandler implements Listener {

    private String verificationWorldName = null;


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        VerificationPlayer vp;

        try {
            vp = Verilock.getInstance().verificationDatabase.GetOrCreatePlayer(player.getUniqueId());
        } catch (SQLException e) {
            player.kickPlayer("A database error occurred while finding your verification status. Please rejoin the server.");
            throw new RuntimeException(e);
        }

        if (vp.getIdentityId() != null) {
            // Check if player is still in verification world
            if (player.getWorld().getName().equals(Verilock.getInstance().getConfig().getString("verificationWorldName"))) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(Verilock.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        Verilock.getInstance().GetTeleportManager().TeleportPlayerToSpawn(player);
                    }
                }, 10L);


            }

            PlayerIdentity identity = Verilock.getInstance().verificationDatabase.GetPlayerIdentity(vp.getIdentityId());
            String fixedName = identity.getFirstName();
            fixedName = fixedName.substring(0, 1).toUpperCase() + fixedName.substring(1).toLowerCase();

            player.sendMessage(ChatColor.GREEN + "Welcome back, " + fixedName + "!");

        } else {
            Verilock.getInstance().getLogger().info("Moving " + player.getName() + " to verification world.");
            Bukkit.getScheduler().scheduleSyncDelayedTask(Verilock.getInstance(), new Runnable() {
                @Override
                public void run() {
                    Verilock.getInstance().GetTeleportManager().TeleportPlayerToVerificationWorld(player);
                }
            }, 10L);
            Messages.SendWelcomeMessage(player);
            PlayerTimeoutManager.addPlayerTimeout(player, 2400);
        }
    }
}
