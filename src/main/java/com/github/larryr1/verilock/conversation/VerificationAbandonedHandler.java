package com.github.larryr1.verilock.conversation;

import com.github.larryr1.verilock.Verilock;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationAbandonedListener;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.entity.Player;

public class VerificationAbandonedHandler implements ConversationAbandonedListener {

    private boolean invalidated = false;

    @Override
    public void conversationAbandoned(ConversationAbandonedEvent event) {

        // Bukkit is calling this twice for some reason. I'm not sure why. I probably screwed up somewhere. The convo can only be abandoned once so I figured this is the best way to handle it.
        if (invalidated) {
            Verilock.getInstance().logger.warning("An invalidated abandon handler was called.");
            return;
        }

        if (event.gracefulExit()) {
            invalidated = true;
            ConversationContext context = event.getContext();
            if (context.getSessionData("studentId") != null && context.getSessionData("studentBirthday") != null) {
                VerificationConversationProcessor.ProcessConversationResult(event.getContext());
            } else {
                Verilock.getInstance().logger.warning("Finished conversation was missing one of the required keys. Report this error if it persists.");
            }
        } else {
            ((Player) event.getContext().getSessionData("playerReference")).sendMessage(new VerilockPrefix().getPrefix(event.getContext()) + "The verification was canceled (you probably waited too long). To try again, type " + ChatColor.GOLD + "/verify");
        }
    }
}
