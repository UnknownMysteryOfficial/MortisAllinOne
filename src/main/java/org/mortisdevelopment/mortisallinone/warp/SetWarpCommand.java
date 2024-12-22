package org.mortisdevelopment.mortisallinone.warp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.mortisdevelopment.mortisallinone.MortisAllinOne;

import java.io.File;
import java.io.IOException;

public class SetWarpCommand implements CommandExecutor {

    private final MortisAllinOne plugin;
    private String prefix;



    public SetWarpCommand(MortisAllinOne plugin) {
        this.plugin = plugin;
        prefix = plugin.getConfig().getString("prefix");
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

        File file = getFile("warps.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        if (commandSender instanceof Player){

            Player player = (Player) commandSender;

            if (player.hasPermission("mortis.aio.setwarp")){

                if (args.length == 0){

                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&cPlease provide a valid name for warp!"));

                } else if (args.length == 1) {


                    String warpName = args[0];

                    if (!config.contains(warpName)){
                        World warp = Bukkit.getServer().getWorld(args[0]);

                        Location location = player.getLocation();

                        int x = location.getBlockX();
                        int y = location.getBlockY();
                        int z = location.blockZ();
                        float pitch = location.getPitch();
                        float yaw = location.getYaw();

                        Location warpLocation = new Location(warp, x, y, z, pitch, yaw);

                        config.set(warpName, location);
                        try {
                            config.save(file);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&aSuccessfully set warp " + warpName + " at your location!"));
                    } else if (config.contains(warpName)) {

                        World warp = Bukkit.getServer().getWorld(args[0]);

                        Location location = player.getLocation();

                        int x = location.getBlockX();
                        int y = location.getBlockY();
                        int z = location.blockZ();
                        float pitch = location.getPitch();
                        float yaw = location.getYaw();

                        Location warpLocation = new Location(warp, x, y, z, yaw, pitch);

                        config.set(warpName, location);
                        try {
                            config.save(file);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

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
