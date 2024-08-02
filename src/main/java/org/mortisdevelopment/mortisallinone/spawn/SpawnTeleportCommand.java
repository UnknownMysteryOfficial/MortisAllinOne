package org.mortisdevelopment.mortisallinone.spawn;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.mortisdevelopment.mortisallinone.MortisAllinOne;

import java.io.File;

public class SpawnTeleportCommand implements CommandExecutor {

    private final MortisAllinOne plugin;
    private String prefix;
    private String spawnTeleportMessage;

    public SpawnTeleportCommand(MortisAllinOne plugin) {
        this.plugin = plugin;
        prefix = plugin.getConfig().getString("prefix");
        spawnTeleportMessage = plugin.getConfig().getString("spawn-teleport-message");
    }

    private File getFile(String name) {
        File file = new File(plugin.getDataFolder(), name);
        if (!file.exists()) {
            plugin.saveResource(name, true);
        }
        return file;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        File file = getFile("config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        Location location = config.getLocation("spawn");

        if (commandSender instanceof Player){
            Player player = (Player) commandSender;

            if (player.hasPermission("mortis.aio.spawn")){

                if (location != null){
                    player.teleport(location);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + spawnTeleportMessage));
                } else if (location == null) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&cNo spawnpoint found! Please use /setspawn to set the spawnpoint location."));
                }

            }else{

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&cYou don't have the permission to run this command!"));

            }


        }else{
            plugin.getLogger().info("This command can only be ran by a player!");
        }

        return true;
    }


}
