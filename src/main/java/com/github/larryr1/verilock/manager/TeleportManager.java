package com.github.larryr1.verilock.manager;

import com.github.larryr1.verilock.Verilock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class TeleportManager {

    public static Location verificationLocation;
    public static Location spawnLocation;



    public TeleportManager() {
        RetrieveConfig();
    }

    public void RetrieveConfig() {
        FileConfiguration config = Verilock.getInstance().getConfig();

        String verificationWorldName = config.getString("verificationWorldName");
        double verificationX = config.getDouble("verificationX");
        double verificationY = config.getDouble("verificationY");
        double verificationZ = config.getDouble("verificationZ");
        String spawnWorldName = config.getString("spawnWorldName");

        // Check for missing values, and disable the plugin because these config options are critical.
        if (verificationWorldName == null) {
            Verilock.getInstance().getLogger().severe("Configuration option 'verificationWorldName' is missing.");
            Bukkit.getPluginManager().disablePlugin(Verilock.getInstance());
            return;

        } else if (spawnWorldName == null) {
            Verilock.getInstance().getLogger().severe("Configuration option 'spawnWorldName' is missing.");
            Bukkit.getPluginManager().disablePlugin(Verilock.getInstance());
            return;
        }

        verificationLocation = new Location(
            Bukkit.getWorld(verificationWorldName),
            verificationX,
            verificationY,
            verificationZ,
            0,
            0
        );

        Verilock.getInstance().getLogger().info("Using world '" + spawnWorldName + "' as the spawn world.");
        Verilock.getInstance().getLogger().info("Using world '" + verificationWorldName + "' as the verification world.");

        World spawnWorld = Bukkit.getWorld(spawnWorldName);
        if (spawnWorld == null) {
            Verilock.getInstance().getLogger().severe("Bukkit returned null when retrieving the world " + spawnWorldName + ".");
            Bukkit.getPluginManager().disablePlugin(Verilock.getInstance());
            return;
        }
        spawnLocation = spawnWorld.getSpawnLocation();
    }



    public void TeleportPlayerToVerificationWorld(Player player) {
        player.teleport(verificationLocation);
    }

    public void TeleportPlayerToSpawn(Player player) {
        player.teleport(spawnLocation);
    }
}
