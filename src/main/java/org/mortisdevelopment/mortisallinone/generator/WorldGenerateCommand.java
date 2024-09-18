package org.mortisdevelopment.mortisallinone.generator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mortisdevelopment.mortisallinone.MortisAllinOne;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WorldGenerateCommand implements CommandExecutor, TabCompleter {

    private final MortisAllinOne plugin;
    private String prefix;

    public WorldGenerateCommand(MortisAllinOne plugin) {
        this.plugin = plugin;
        prefix = plugin.getConfig().getString("prefix");
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if (commandSender instanceof Player){

            Player player = (Player) commandSender;

            if (player.hasPermission("mortis.aio.worldgenerate")){
                if (args.length == 0){

                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&cYou need to provide the world name and environment type ."));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&6Examples:"));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&a/mao world Normal"));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&a/mao world_nether Nether"));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&a/mao world_end End"));

                } else if (args.length == 1) {

                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&cYou need to provide environment type."));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&6Examples:"));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&a/mao world Normal"));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&a/mao world_nether Nether"));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&a/mao world_end End"));

                }else if (args.length == 2){

                    String worldName = args[0];
                    World.Environment environment = World.Environment.valueOf(args[1].toUpperCase());

                    if (Bukkit.getWorld(worldName) != null){
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&cWorld with that name already exists!"));
                    }else{
                        plugin.getWorldManager().worldCreation(worldName, environment);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&aCreating world....."));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&aWorld successfully created!"));
                        if (Bukkit.getWorld(worldName) == null){
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&cThe plugin ran into a problem while creating world named " + worldName +"."));
                        }
                    }
                }
            }else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&cYou don't have enough permission to run this command!"));
            }
        }else {
            plugin.getLogger().info("This command can only be ran by a player!");
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 2){
            return Arrays.asList("normal", "nether", "the_end");
        }
        return new ArrayList<>();
    }

    private File getFile(String name) {
        File file = new File(plugin.getDataFolder(), name);
        if (!file.exists()) {
            plugin.saveResource(name, true);
        }
        return file;
    }

    private void saveToConfig(String worldName){
        File file = getFile("worlds.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        List<String> worlds = config.getStringList("worlds");
        if (!worlds.contains(worldName)) {
            worlds.add(worldName);
            config.set("worlds", worlds);
            try {
                config.save(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}