package com.github.larryr1.verilock;

import com.github.larryr1.verilock.commands.completers.VerilockTabCompleter;
import com.github.larryr1.verilock.commands.executors.VerifyCommandExecutor;
import com.github.larryr1.verilock.commands.executors.verilock.VerilockCommandExecutor;
import com.github.larryr1.verilock.commands.executors.VerilockDBExecutor;
import com.github.larryr1.verilock.data.database.VerificationDatabase;
import com.github.larryr1.verilock.events.PlayerJoinHandler;
import com.github.larryr1.verilock.events.PlayerQuitHandler;
import com.github.larryr1.verilock.events.PlayerSpawnLocationHandler;
import com.github.larryr1.verilock.manager.TeleportManager;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.logging.Logger;

public final class Verilock extends JavaPlugin {

    private static Verilock instance;
    private TeleportManager teleportManager;
    private RegisteredServiceProvider<LuckPerms> permissionProvider;
    public ConversationFactory conversationFactory;
    public Logger logger;
    public VerificationDatabase verificationDatabase;

    @Override
    public void onEnable() {

        // Initialize things that need initializing
        instance = this;
        logger = this.getLogger();
        conversationFactory = new ConversationFactory(this);

        // Register events and commands with Bukkit
        registerEvents();
        registerCommands();

        // Create config
        saveDefaultConfig();

        // Database Setup
        try {
            verificationDatabase = new VerificationDatabase(getDataFolder().getAbsolutePath() + "/VerilockDatabase.db");
        } catch (SQLException e) {
            e.printStackTrace();
            logger.severe("Failed to connect to the database! " + e.getMessage());
            Bukkit.getPluginManager().disablePlugin(this);
        }

        permissionProvider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);

        // Teleport manager, initialize after worlds are loaded
        this.getServer().getScheduler().scheduleSyncDelayedTask(this, () -> teleportManager = new TeleportManager(), 1);

    }

    @Override
    public void onDisable() {

        // Close database connection
        try {
            verificationDatabase.CloseConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void registerCommands() {
        this.getCommand("verify").setExecutor(new VerifyCommandExecutor(this));
        this.getCommand("verilock").setExecutor(new VerilockCommandExecutor());
        this.getCommand("verilock").setTabCompleter(new VerilockTabCompleter());
        this.getCommand("verilockdb").setExecutor(new VerilockDBExecutor());
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new PlayerJoinHandler(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitHandler(), this);
        getServer().getPluginManager().registerEvents(new PlayerSpawnLocationHandler(), this);
    }

    public static Verilock getInstance() {
        return instance;
    }

    public TeleportManager GetTeleportManager() {
        return this.teleportManager;
    }

    public void TriggerReload() {
        reloadConfig();
        GetTeleportManager().RetrieveConfig();
    }

    public RegisteredServiceProvider<LuckPerms> GetPermissionProvider() {
        return permissionProvider;
    }
}
