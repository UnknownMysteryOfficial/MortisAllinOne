package org.mortisdevelopment.mortisallinone.spawn;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.mortisdevelopment.mortisallinone.MortisAllinOne;

import java.io.File;
import java.io.IOException;

public class SpawnPointCommand implements CommandExecutor {

    private final MortisAllinOne plugin;
    private String prefix;
    private String spawnCommandMessage;

    public SpawnPointCommand(MortisAllinOne plugin) {
        this.plugin = plugin;
        prefix = plugin.getConfig().getString("prefix");
        spawnCommandMessage = plugin.getConfig().getString("spawnpoint-message");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (sender instanceof Player ){

            Player player = (Player) sender;

            if (player.hasPermission("mortis.aio.setspawn")){

                Location location = player.getLocation();

                File file = getFile("config.yml");

                FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                config.set("spawn", location);
                try {
                    config.save(file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + spawnCommandMessage));

            }else{

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&cYou don't have the permission to run this command!"));

            }

        }else{
            plugin.getLogger().info("This command can only be ran by a player!");
        }

        return true;
    }

    private File getFile(String name) {
        File file = new File(plugin.getDataFolder(), name);
        if (!file.exists()) {
            plugin.saveResource(name, true);
        }
        return file;
    }

}
