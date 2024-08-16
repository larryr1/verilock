package com.github.larryr1.verilock.events;

import com.github.larryr1.verilock.Verilock;
import com.github.larryr1.verilock.conversation.Messages;
import com.github.larryr1.verilock.data.PlayerIdentity;
import com.github.larryr1.verilock.data.VerificationPlayer;
import com.github.larryr1.verilock.manager.PlayerTimeoutManager;
import com.github.larryr1.verilock.manager.TeleportManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.sql.SQLException;

public class PlayerSpawnLocationHandler implements Listener {

    @EventHandler
    public void onPlayerSpawnLocation(PlayerSpawnLocationEvent event) {

    }
}
