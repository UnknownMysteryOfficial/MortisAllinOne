package org.mortisdevelopment.mortisallinone.generator;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class WorldManager {

    private final JavaPlugin plugin;

    public WorldManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    private File getFile(String name) {
        File file = new File(plugin.getDataFolder(), name);
        if (!file.exists()) {
            plugin.saveResource(name, true);
        }
        return file;
    }

    private void saveToConfig(String worldName){

        File file = getFile("worlds.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        List<String> worlds = config.getStringList("worlds");
        if (!worlds.contains(worldName)) {
            worlds.add(worldName);
            config.set("worlds", worlds);
            try {
                config.save(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void worldCreation(String worldName, World.Environment environment){

        WorldCreator worldCreator = new WorldCreator(worldName);
        worldCreator.environment(environment);
        World world = Bukkit.createWorld(worldCreator);

        if (world != null){

            Bukkit.getWorld(worldName);
            saveToConfig(worldName);

        }

    }

}
