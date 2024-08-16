package com.github.larryr1.verilock.conversation.prompts;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Bukkit chat prompt for obtaining and validating a user's student ID number in the verification process.
 */
public class IDNumberPrompt extends ValidatingPrompt {

    @Override
    protected boolean isInputValid(@NotNull ConversationContext conversationContext, @NotNull String s) {
        // If the input is a 10-digit number (student ID) or starts with CUSTOM_ (used for special authorizations) then the input is valid.
        if ((s.matches("^[0-9]*$") && s.length() == 10) || s.startsWith("CUSTOM_")) return true;
        conversationContext.getForWhom().sendRawMessage(ChatColor.RED + "You did not enter a valid 562#. Please try again.");
        return false;
    }

    @Override
    protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull String s) {

        conversationContext.setSessionData("studentId", s);
        conversationContext.getForWhom().sendRawMessage(ChatColor.GREEN + "Accepted 562# as " + s + ".");

        return new BirthdayPrompt();
    }

    @Override
    public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
        return ChatColor.GOLD + "[1/2] Enter your 562#:";
    }
}
