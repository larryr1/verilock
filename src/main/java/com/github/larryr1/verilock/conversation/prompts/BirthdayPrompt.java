package com.github.larryr1.verilock.conversation.prompts;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Bukkit chat prompt for obtaining and validating a user's birthday in the player verification flow.
 */
public class BirthdayPrompt extends ValidatingPrompt {

    private static DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("M/d/yyyy");

    @Override
    protected boolean isInputValid(@NotNull ConversationContext conversationContext, @NotNull String s) {

        // Validate input as date
        String formattedInput = formatBirthdayString(s);

        try {
            LocalDate birthdate = LocalDate.parse(formattedInput, dtFormatter);
            return true;
        } catch (DateTimeParseException e) {
            conversationContext.getForWhom().sendRawMessage(ChatColor.RED + formattedInput + " is not a valid birthday. Please try again.");
            return false;
        }
    }

    @Override
    protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull String s) {

        String formattedInput = formatBirthdayString(s);
        conversationContext.setSessionData("studentBirthday", formattedInput);
        conversationContext.getForWhom().sendRawMessage(ChatColor.GREEN + "Accepted birthday as " + formattedInput + ".");

        return END_OF_CONVERSATION;
    }

    @Override
    public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
        return ChatColor.GOLD + "[2/2] Enter your birthday in MM/DD/YYYY Format (ex. 1/1/2007):";
    }

    /**
     * Removes leading zeros from a date string in the form of MM/DD/YYYY.
     * @param input The date string to process.
     * @return The formatted date string.
     */
    private String formatBirthdayString(String input) {
        StringBuilder sb = new StringBuilder();
        String[] parts = input.split("/");

        // Check each part of the date
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];

            // If the part is a 2 digit date, chop off the first character if it is a zero.
            if (part.length() == 2) {
                part = part.startsWith("0") ? part.substring(1) : part;
                parts[i] = part;
            }

            sb.append(part);

            // Add a slash at the appropriate spots.
            if (i < parts.length - 1) {
                sb.append("/");
            }
        }

        return sb.toString();
    }
}
