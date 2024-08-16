package com.github.larryr1.verilock.commands.executors;

import com.github.larryr1.verilock.Verilock;
import com.github.larryr1.verilock.conversation.Conversations;
import com.github.larryr1.verilock.conversation.Messages;
import com.github.larryr1.verilock.manager.PlayerTimeoutManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class VerifyCommandExecutor implements CommandExecutor {

    private final Verilock instance;
    public VerifyCommandExecutor(Verilock plugin) {
        instance = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "You must be a player to use this command.");
            return true;
        }

        // Cast to player
        Player player = (Player) commandSender;

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("exempt")) {
                Messages.SendExemptionMessage(player);
                PlayerTimeoutManager.cancelPlayerTimeout(player);
                PlayerTimeoutManager.addPlayerTimeout(player, 1200); // 1 minute
                return true;
            }

            player.sendMessage(ChatColor.RED + "Incorrect command usage.");
            return false;
        }

        instance.logger.info("Starting conversation.");
        Conversation conversation = Conversations.GetVerificationConversationFactory().buildConversation(player);
        conversation.getContext().setSessionData("playerReference", player);
        conversation.begin();

        return true;
    }
}
