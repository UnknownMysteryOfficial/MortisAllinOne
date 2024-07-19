package org.mortisdevelopment.mortisallinone.rtp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mortisdevelopment.mortisallinone.MortisAllinOne;


import java.util.Random;

public class RandomTeleportCommand implements CommandExecutor {

    private final MortisAllinOne plugin;
    private String prefix;
    private String rtpSuccessful;

    public RandomTeleportCommand(MortisAllinOne plugin) {
        this.plugin = plugin;
        prefix = plugin.getConfig().getString("prefix");
        rtpSuccessful = plugin.getConfig().getString("rtp-successful");
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {


        if (commandSender instanceof Player){

            Player player = (Player) commandSender;

            if (player.hasPermission("mortis.aio.rtp")){

                if (args.length == 0){

                    Random random = new Random();

                    int x = random.nextInt(100000);
                    int y = 150;
                    int z = random.nextInt(100000);

                    World world = player.getWorld();

                    if (world != null){
                        Location randomLocation = new Location(world, x, y, z);

                        y = randomLocation.getWorld().getHighestBlockYAt(randomLocation);
                        randomLocation.setY(y);

                        player.teleport(randomLocation);
                        String message = ChatColor.translateAlternateColorCodes('&', prefix + rtpSuccessful);
                        message = message.replace("{world}", player.getWorld().getName());
                        message = message.replace("{x}", String.valueOf(x));
                        message = message.replace("{y}", String.valueOf(y));
                        message = message.replace("{z}", String.valueOf(z));
                        player.sendMessage(message);
                    } else if (world == null) {
                        player.sendMessage(prefix + ChatColor.RED + "The world seems to be corrupted or damaged, cancelling teleport.");
                    }

                } else if (args.length == 1){

                    Random newRandom = new Random();
                    int x = newRandom.nextInt(1000);
                    int y = 150;
                    int z = newRandom.nextInt(1000);

                    World world = Bukkit.getServer().getWorld(args[0]);

                    if (world != null){

                        Location location = new Location(world, x, y, z);

                        y= location.getWorld().getHighestBlockYAt(location);
                        location.setY(y);

                        player.teleport(location);
                        String message = ChatColor.translateAlternateColorCodes('&', prefix + rtpSuccessful);
                        message = message.replace("{world}", player.getWorld().getName());
                        message = message.replace("{x}", String.valueOf(x));
                        message = message.replace("{y}", String.valueOf(y));
                        message = message.replace("{z}", String.valueOf(z));
                        player.sendMessage(message);

                    } else if (world == null) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&cUnable to find a world named" + world));
                    }


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