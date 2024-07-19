package org.mortisdevelopment.mortisallinone.generator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mortisdevelopment.mortisallinone.MortisAllinOne;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WorldTeleporter implements CommandExecutor, TabCompleter {

    private final MortisAllinOne plugin;
    private String prefix;

    public WorldTeleporter(MortisAllinOne plugin) {
        this.plugin = plugin;
        prefix = plugin.getConfig().getString("prefix");
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if (commandSender instanceof Player) {

            Player player = (Player) commandSender;

            if (player.hasPermission("mortis.aio.worldtp")){

                if (args.length == 0) {

                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&cYou need to provide the world name!"));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&6Examples:"));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&a/maotp world"));


                } else if (args.length == 1) {

                    World worldName = Bukkit.getWorld(args[0]);

                    if (worldName != null){

                        Location location = worldName.getSpawnLocation();
                        player.teleport(location);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&aSuccessfully teleported to world " + String.valueOf(args[0]) + "."));

                    }else if (worldName == null){

                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&cCan not find world with name " + String.valueOf(args[0]) + "."));

                    }

                }

            }

        } else {

            plugin.getLogger().info("This command can only be ran by a player!");

        }


        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (args.length == 1){

            return Bukkit.getWorlds().stream()
                    .map(World::getName)
                    .collect(Collectors.toList());

        }

        return new ArrayList<>();
    }
}