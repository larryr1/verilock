package com.github.larryr1.verilock.conversation;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * Contains static methods used for sending the welcome and exemption messages to players.
 */
public class Messages {
    /**
     * Send the welcome message to the specified player.
     * @param player The player to send the message to.
     */
    public static void SendWelcomeMessage(Player player) {
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        player.sendMessage(new StringBuilder()
            .append(ChatColor.GOLD).append("Welcome to PlaySCPA.\n\n")
            .append(ChatColor.AQUA).append("Students must verify with their 562 number and birthday.\n")
            .append("Type ").append(ChatColor.YELLOW).append("/verify").append(ChatColor.AQUA).append(" to start the verification processs.\n\n")
            .append(ChatColor.DARK_GRAY).append("If you are a teacher or alumni, type ").append(ChatColor.GRAY).append("/verify exempt").append(ChatColor.GRAY).append(" for special instructions.\n")
            .append(ChatColor.WHITE).append("You have 2 minutes to complete verification.").toString());
    }

    /**
     * Send the exemption message to the specified player.
     * @param player The player to send the message to.
     */
    public static void SendExemptionMessage(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1, 0);
        player.sendMessage(new StringBuilder()
            .append(ChatColor.AQUA).append("If you are a teacher or alumni, you are exempt from verification as you do not have a 562#.\n")
            .append("Send an email to ").append(ChatColor.AQUA).append("lrowe@charterschoolit.com").append(ChatColor.AQUA).append(" to request access.\n")
            .append(ChatColor.GRAY).append("You will be automatically disconnected in 1 minute.").toString());
    }
}
