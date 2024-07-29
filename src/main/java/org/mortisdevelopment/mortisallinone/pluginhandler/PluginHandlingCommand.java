package org.mortisdevelopment.mortisallinone.pluginhandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mortisdevelopment.mortisallinone.MortisAllinOne;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PluginHandlingCommand implements TabExecutor {

    private final MortisAllinOne plugin;
    private String prefix;

    public PluginHandlingCommand(MortisAllinOne plugin) {
        this.plugin = plugin;
        prefix = plugin.getConfig().getString("prefix");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (commandSender instanceof Player){

            Player player = (Player) commandSender;

            if (player.hasPermission("mortis.aio.pluginhandler")){

                if (args.length == 0){

                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&cIncomplete command arguments!"));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&c/maoplugin <plugin name> disable|enable|reload"));
                }else if (args.length == 1){

                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&cIncomplete command arguments!"));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&c/maoplugin <plugin name> disable|enable|reload"));

                }else if (args.length == 2){

                    Plugin pluginName = Bukkit.getServer().getPluginManager().getPlugin(args[0]);
                    String argName = args[1];

                    if (pluginName != null){

                        if (argName.equalsIgnoreCase("disable")){

                            if (!pluginName.getName().equalsIgnoreCase("MortisAllinOne")){

                                Bukkit.getServer().getPluginManager().disablePlugin(pluginName);
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&aPlugin successfully disabled!"));

                            }else {

                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&cYou can not disable this plugin!"));

                            }

                        } else if (argName.equalsIgnoreCase("enable")) {

                            Bukkit.getServer().getPluginManager().enablePlugin(pluginName);
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&aPlugin successfully enabled!"));

                        } else if (argName.equalsIgnoreCase("reload")) {

                            Bukkit.getServer().getPluginManager().disablePlugin(pluginName);
                            Bukkit.getServer().getPluginManager().enablePlugin(pluginName);
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&aPlugin successfully reloaded!"));

                        }

                    }else {

                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&cNo plugin found with that name"));

                    }

                }

            }else {

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&cYou don't have enough permission to run this command!"));

            }

        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (args.length == 1){

            return Arrays.stream(Bukkit.getServer().getPluginManager().getPlugins())
                    .map(Plugin::getName)
                    .collect(Collectors.toList());

        }else if (args.length == 2){

            return Arrays.asList("enable", "disable", "reload");

        }

        return new ArrayList<>();
    }
}
