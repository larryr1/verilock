package com.github.larryr1.verilock.manager;

import com.github.larryr1.verilock.Verilock;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

public class PlayerTimeoutManager {

    private static final Map<UUID, Integer> playerTimeouts = new HashMap<>();

    /**
     * Creates a Runnable to kick the specified player after timeInTicks ticks.
     * @param player The player to kick.
     * @param timeInTicks The number of ticks to pass before kicking.
     */
    public static void addPlayerTimeout(Player player, Integer timeInTicks) {

        Integer taskId = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Verilock.getInstance(), () -> {
            playerTimeouts.remove(player.getUniqueId());
            player.kickPlayer(ChatColor.YELLOW + "You have been disconnected for idling while not verified.\nPlease rejoin and try again.");

        }, timeInTicks);

        playerTimeouts.put(player.getUniqueId(), taskId);
    }

    /**
     * Cancel the player timeout.
     * @param player The player to cancel the timeout for.
     */
    public static void cancelPlayerTimeout(Player player) {
        if (playerTimeouts.containsKey(player.getUniqueId())) {
            int taskId = playerTimeouts.get(player.getUniqueId());
            Bukkit.getServer().getScheduler().cancelTask(taskId);
            playerTimeouts.remove(player.getUniqueId());
        }

    }
}
