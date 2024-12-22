package org.mortisdevelopment.mortisallinone.homes;

import org.bukkit.Bukkit;
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
import java.util.UUID;

public class HomeTeleportCommand implements CommandExecutor {

    private final MortisAllinOne plugin;
    private String prefix;

    public HomeTeleportCommand(MortisAllinOne plugin) {
        this.plugin = plugin;
        prefix = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(prefix + ChatColor.RED + "Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("mortis.aio.home")) {
            player.sendMessage(prefix + ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(prefix + ChatColor.RED + "Usage: /home <home_name>");
            return true;
        }

        String homeName = args[0];
        UUID playerUUID = player.getUniqueId();

        File file = getFile("homes.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        String homePath = playerUUID.toString() + "." + homeName;
        if (!config.contains(homePath)) {
            player.sendMessage(prefix + ChatColor.RED + "No home found with the name " + ChatColor.WHITE + homeName + ChatColor.RED + ".");
            return true;
        }

        String worldName = config.getString(homePath + ".world");
        double x = config.getDouble(homePath + ".x");
        double y = config.getDouble(homePath + ".y");
        double z = config.getDouble(homePath + ".z");
        float yaw = (float) config.getDouble(homePath + ".yaw");
        float pitch = (float) config.getDouble(homePath + ".pitch");

        Location location = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);

        if (location.getWorld() == null) {
            player.sendMessage(prefix + ChatColor.RED + "The world " + ChatColor.WHITE + worldName + ChatColor.RED + " does not exist or is not loaded.");
            return true;
        }

        player.teleport(location);
        player.sendMessage(prefix + ChatColor.GREEN + "Teleported to home " + ChatColor.WHITE + homeName + ChatColor.GREEN + ".");

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
