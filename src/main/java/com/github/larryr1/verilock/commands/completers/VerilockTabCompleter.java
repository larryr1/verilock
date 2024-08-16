package com.github.larryr1.verilock.commands.completers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class VerilockTabCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        // Root Command
        if (args.length == 1) return Arrays.asList("identities", "database", "reload");
        if (args.length == 2 && args[0].equalsIgnoreCase("identities")) return Arrays.asList("import");
        if (args.length == 2 && args[0].equalsIgnoreCase("database")) return Arrays.asList("lookup_player", "lookup_identity", "link_identity", "unlink_identity");
        return null;
    }
}
