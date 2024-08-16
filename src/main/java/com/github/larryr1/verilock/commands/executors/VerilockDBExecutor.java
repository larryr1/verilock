package com.github.larryr1.verilock.commands.executors;

import com.github.larryr1.verilock.Verilock;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

public class VerilockDBExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length < 1) {
            commandSender.sendMessage(ChatColor.RED + "Incorrect command syntax.");
            return true;
        }

        if (args[0].equalsIgnoreCase("runsql")) {

            // Check for query
            if (args.length < 2) {
                commandSender.sendMessage(ChatColor.RED + "Incorrect command syntax. No SQL query specified.");
                return true;
            }

            StringBuilder queryBuilder = new StringBuilder();

            for (int i = 1; i < args.length; i++) {
                queryBuilder.append(" " + args[i]);
            }

            String query = queryBuilder.toString();

            commandSender.sendMessage(ChatColor.YELLOW + "Executing query: " + query);


            try (ResultSet result = Verilock.getInstance().verificationDatabase.ExecuteQuery(query)) {

                if (result.getRow() < 1) {
                    commandSender.sendMessage(ChatColor.YELLOW + "No rows were returned for that query.");
                    return true;
                }

                ResultSetMetaData meta = result.getMetaData();
                int columnCount = meta.getColumnCount();

                ArrayList<String> columnLabels = new ArrayList<>();

                StringBuilder columnHeaderBuilder = new StringBuilder();

                for (int i = 0; i < columnCount; i++) {
                    String label = meta.getColumnLabel(i + 1);
                    columnLabels.add(label);
                    columnHeaderBuilder.append(", " + label);
                }

                Verilock.getInstance().logger.info(ChatColor.GOLD + "Column Labels:");
                Verilock.getInstance().logger.info(columnHeaderBuilder.toString());

                while (result.next()) {

                    StringBuilder rowBuilder = new StringBuilder("Row:");

                    for (int i = 0; i < columnLabels.size(); i++) {
                        rowBuilder.append(", " + result.getObject(i));
                    }

                    Verilock.getInstance().logger.info(rowBuilder.toString());
                }

                commandSender.sendMessage(ChatColor.GREEN + "Query executed successfully. Results logged to server console.");

            } catch (SQLException e) {
                commandSender.sendMessage(ChatColor.RED + "SQLException while executing query.\n" + ChatColor.RED + e);
                e.printStackTrace();
            }
        }

        return true;
    }
}
