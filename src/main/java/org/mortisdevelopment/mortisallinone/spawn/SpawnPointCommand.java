package org.mortisdevelopment.mortisallinone.spawn;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mortisdevelopment.mortisallinone.MortisAllinOne;

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

                plugin.getConfig().set("spawn", location);
                plugin.saveConfig();
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + spawnCommandMessage));
            }else{

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&cYou don't have the permission to run this command!"));

            }

        }else{
            plugin.getLogger().info("This command can only be ran by a player!");
        }

        return true;
    }
}
