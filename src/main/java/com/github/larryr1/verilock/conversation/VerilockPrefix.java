package com.github.larryr1.verilock.conversation;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;
import org.jetbrains.annotations.NotNull;

public class VerilockPrefix implements ConversationPrefix {
    @Override
    @NotNull
    public String getPrefix(@NotNull ConversationContext conversationContext) {
        return new StringBuilder()
            .append(ChatColor.GOLD)
            .append("<")
            .append(ChatColor.ITALIC)
            .append(ChatColor.GREEN)
            .append("Verilock")
            .append(ChatColor.RESET)
            .append(ChatColor.GOLD)
            .append("> ")
            .append(ChatColor.RESET).toString();
    }
}
