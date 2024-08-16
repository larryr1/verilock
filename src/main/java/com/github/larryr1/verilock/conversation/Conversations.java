package com.github.larryr1.verilock.conversation;

import com.github.larryr1.verilock.Verilock;
import com.github.larryr1.verilock.conversation.prompts.IDNumberPrompt;
import org.bukkit.conversations.ConversationFactory;

public class Conversations {

    /**
     * Creates a new verification conversation.
     * @return The conversation.
     */
    public static ConversationFactory GetVerificationConversationFactory() {
        return Verilock.getInstance().conversationFactory.withFirstPrompt(new IDNumberPrompt())
            .withLocalEcho(true)
            .withTimeout(120)
            .withPrefix(new VerilockPrefix())
            .addConversationAbandonedListener(new VerificationAbandonedHandler());
    }
}
