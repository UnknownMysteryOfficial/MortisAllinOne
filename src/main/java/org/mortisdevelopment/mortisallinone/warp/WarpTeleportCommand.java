package org.mortisdevelopment.mortisallinone.warp;

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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class WarpTeleportCommand implements CommandExecutor, TabCompleter {

    private final MortisAllinOne plugin;
    private String prefix;
    private String invalidName;
    private String noWarpFound;
    private String warpTeleport;

    public WarpTeleportCommand(MortisAllinOne plugin) {
        this.plugin = plugin;
        prefix = plugin.getConfig().getString("prefix");
        invalidName = plugin.getConfig().getString("invalid-name");
        noWarpFound = plugin.getConfig().getString("no-warp-found");
        warpTeleport = plugin.getConfig().getString("warp-teleport");
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if (commandSender instanceof Player){

            Player player = (Player) commandSender;

            if (player.hasPermission("mortis.aio.warptp")){

                if (args.length == 0){

                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + invalidName));

                } else if (args.length == 1) {

                    String warpName = args[0];
                    Location location = plugin.getConfig().getLocation(warpName);

                    if (location != null){

                        player.teleport(location);
                        String message = (ChatColor.translateAlternateColorCodes('&', prefix + warpTeleport));
                        message = message.replace("{warp}", warpName);
                        player.sendMessage(message);

                    } else if (location == null) {

                        String message = (ChatColor.translateAlternateColorCodes('&', prefix + noWarpFound));
                        message = message.replace("{warp}", warpName);
                        player.sendMessage(message);

                    }

                }

            }

        }else{
            plugin.getLogger().info("This command can only be ran by a player!");
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        return new ArrayList<>();
    }

}
