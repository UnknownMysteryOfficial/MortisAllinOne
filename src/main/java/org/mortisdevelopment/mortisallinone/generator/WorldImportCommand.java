package org.mortisdevelopment.mortisallinone.generator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
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
import java.util.List;

public class WorldImportCommand implements CommandExecutor, TabCompleter {

    private final MortisAllinOne plugin;
    private String prefix;

    public WorldImportCommand(MortisAllinOne plugin) {
        this.plugin = plugin;
        prefix = plugin.getConfig().getString("prefix");
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if (commandSender instanceof Player){

            Player player = (Player) commandSender;

            if (player.hasPermission("mortis.aio.worldimport")){

                if (args.length == 0){

                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&cPlease provide a name for world to import!"));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&6Example:"));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&a/maoimport world"));

                } else if (args.length == 1) {

                    String worldName = args[0];
                    File worldFolder = new File(Bukkit.getWorldContainer(), worldName);

                    if (!worldFolder.exists() || !worldFolder.isDirectory()){

                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&cWorld " + worldName + " does not exist in the server directory."));

                    }

                    World world = Bukkit.getWorld(worldName);
                    if (world == null){
                        world = Bukkit.createWorld(new WorldCreator(worldName));

                        if (world == null){

                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&cWorld " + worldName + " could not be imported."));

                        }
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&aWorld " + worldName + " successfully imported!"));
                        File file = getFile("config.yml");
                        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                        List<String> worlds = config.getStringList("worlds");
                        if (!worlds.contains(worldName)){
                            worlds.add(worldName);
                            config.set("worlds", worlds);
                            try {
                                config.save(file);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }else{

                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&aWorld " + worldName + " is already imported."));

                    }

                }

            }else {

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "You don't have enough permission to run this command!"));

            }

        }else{

            if (args.length == 0){

                plugin.getLogger().info("Please provide a name for world to import!");
                plugin.getLogger().info("Example:");
                plugin.getLogger().info("/maoimport world");

            } else if (args.length == 1) {

                String worldName = args[0];
                File worldFolder = new File(Bukkit.getWorldContainer(), worldName);

                if (!worldFolder.exists() || !worldFolder.isDirectory()){

                    plugin.getLogger().info("World " + worldName + " does not exist in the server directory.");

                }

                World world = Bukkit.getWorld(worldName);
                if (world == null){
                    world = Bukkit.createWorld(new WorldCreator(worldName));

                    if (world == null){

                        plugin.getLogger().info("World " + worldName + " could not be imported.");

                    }
                    plugin.getLogger().info("World " + worldName + " successfully imported!");
                    File file = getFile("config.yml");
                    FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                    List<String> worlds = config.getStringList("worlds");
                    if (!worlds.contains(worldName)){
                        worlds.add(worldName);
                        config.set("worlds", worlds);
                        try {
                            config.save(file);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }else{

                    plugin.getLogger().info("World " + worldName + " is already imported.");

                }

            }

        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        return new ArrayList<>();
    }

    private File getFile(String name) {
        File file = new File(plugin.getDataFolder(), name);
        if (!file.exists()) {
            plugin.saveResource(name, true);
        }
        return file;
    }

}