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
import java.util.List;
import java.util.stream.Collectors;

public class WorldUnloaderCommand implements CommandExecutor, TabCompleter {

    private final MortisAllinOne plugin;
    private String prefix;

    public WorldUnloaderCommand(MortisAllinOne plugin) {
        this.plugin = plugin;
        prefix = plugin.getConfig().getString("prefix");
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if (commandSender instanceof Player){

           Player player = (Player) commandSender;

           if (player.hasPermission("mortis.aio.worldunloader")){

               if (args.length == 0){

                   player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&cSpecify a world name to unload."));

               } else if (args.length == 1) {

                   String worldName = args[0];
                   World world = Bukkit.getWorld(worldName);

                   player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&aSaving world before unloading"));

                   if (world != null){

                       Bukkit.unloadWorld(world, true);
                       player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&aWorld " + worldName + " successfully unloaded! You may now delete the world or use /maoimport " + worldName + " to import and load the world again."));

                       File file = getFile("worlds.yml");
                       FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                       List<String> worlds = config.getStringList("worlds");
                       if (worlds.contains(worldName)){
                           worlds.remove(worldName);
                           config.set("worlds", worlds);
                           try {
                               config.save(file);
                           } catch (IOException e) {
                               throw new RuntimeException(e);
                           }
                       }

                   } else {

                       player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&cWorld " + worldName + " not found, it may have already been unloaded or it doesn't exist."));

                   }

               }

           }


        }else {

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

    private File getFile(String name) {
        File file = new File(plugin.getDataFolder(), name);
        if (!file.exists()) {
            plugin.saveResource(name, true);
        }
        return file;
    }

}