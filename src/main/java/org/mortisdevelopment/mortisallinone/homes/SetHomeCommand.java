package org.mortisdevelopment.mortisallinone.homes;

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
import java.io.IOException;
import java.util.UUID;

public class SetHomeCommand implements CommandExecutor {

    private final MortisAllinOne plugin;
    private String prefix;

    public SetHomeCommand(MortisAllinOne plugin) {
        this.plugin = plugin;
        prefix = plugin.getConfig().getString("prefix");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',  prefix + "&cOnly players can use this command."));
            return true;
        }

        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();

        if (args.length != 1) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&cUsage: /sethome <home_name>"));
            return true;
        }

        String homeName = args[0];
        if (!homeName.matches("^[a-zA-Z0-9_-]{1,16}$")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&cInvalid home name. Use alphanumeric characters, underscores, or dashes (max 16 characters)."));
            return true;
        }

        int maxHomes = getMaxHomes(player);

        File file = getFile("homes.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        String playerPath = playerUUID.toString();
        int currentHomes = config.getConfigurationSection(playerPath) == null
                ? 0
                : config.getConfigurationSection(playerPath).getKeys(false).size();

        if (currentHomes >= maxHomes) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&cYou have reached the maximum number of homes (" + ChatColor.WHITE + maxHomes + "&c)."));
            return true;
        }

        Location location = player.getLocation();
        String homePath = playerPath + "." + homeName;

        config.set(homePath + ".world", location.getWorld().getName());
        config.set(homePath + ".x", location.getX());
        config.set(homePath + ".y", location.getY());
        config.set(homePath + ".z", location.getZ());
        config.set(homePath + ".yaw", location.getYaw());
        config.set(homePath + ".pitch", location.getPitch());

        try {
            config.save(file);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&aHome " + ChatColor.WHITE + homeName + "&ahas been set to your location!"));
        } catch (IOException e) {
            player.sendMessage(ChatColor.RED + "An error occurred while saving your home.");
            e.printStackTrace();
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

    private int getMaxHomes(Player player) {
        int maxHomes = 0;
        for (int i = 1; i <= 100; i++) {
            if (player.hasPermission("mao.aio.sethome." + i)) {
                maxHomes = i;
            }
        }
        return maxHomes;
    }
}