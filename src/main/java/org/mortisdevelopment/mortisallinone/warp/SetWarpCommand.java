package org.mortisdevelopment.mortisallinone.warp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mortisdevelopment.mortisallinone.MortisAllinOne;

import java.io.IOException;

public class SetWarpCommand implements CommandExecutor {

    private final MortisAllinOne plugin;
    private String prefix;



    public SetWarpCommand(MortisAllinOne plugin) {
        this.plugin = plugin;
        prefix = plugin.getConfig().getString("prefix");
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if (commandSender instanceof Player){

            Player player = (Player) commandSender;

            if (player.hasPermission("mortis.aio.setwarp")){

                if (args.length == 0){

                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&cPlease provide a valid name for warp!"));

                } else if (args.length == 1) {


                    String warpName = args[0];

                    if (!plugin.getConfig().contains(warpName)){
                        World warp = Bukkit.getServer().getWorld(args[0]);

                        Location location = player.getLocation();

                        int x = location.getBlockX();
                        int y = location.getBlockY();
                        int z = location.blockZ();
                        float pitch = location.getPitch();
                        float yaw = location.getYaw();

                        Location warpLocation = new Location(warp, x, y, z, pitch, yaw);

                        plugin.getConfig().set(warpName, location);
                        plugin.saveConfig();

                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&aSuccessfully set warp " + warpName + " at your location!"));
                    } else if (plugin.getConfig().contains(warpName)) {

                        World warp = Bukkit.getServer().getWorld(args[0]);

                        Location location = player.getLocation();

                        int x = location.getBlockX();
                        int y = location.getBlockY();
                        int z = location.blockZ();
                        float pitch = location.getPitch();
                        float yaw = location.getYaw();

                        Location warpLocation = new Location(warp, x, y, z, yaw, pitch);

                        plugin.getConfig().set(warpName, location);
                        plugin.saveConfig();

                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&aSuccessfully updated warp " + warpName + " location!"));

                    }

                }

            }

        }else{
            plugin.getLogger().info("This command can only be ran by a player!");
        }

        return true;
    }
}
