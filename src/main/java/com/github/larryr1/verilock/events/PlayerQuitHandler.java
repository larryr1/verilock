package com.github.larryr1.verilock.events;

import com.github.larryr1.verilock.manager.PlayerTimeoutManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitHandler implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        PlayerTimeoutManager.cancelPlayerTimeout(event.getPlayer());
    }
}
