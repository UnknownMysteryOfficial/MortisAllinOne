package org.mortisdevelopment.mortisallinone;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.mortisdevelopment.mortisallinone.generator.WorldGenerateCommand;
import org.mortisdevelopment.mortisallinone.generator.WorldImportCommand;
import org.mortisdevelopment.mortisallinone.generator.WorldTeleporter;
import org.mortisdevelopment.mortisallinone.generator.WorldUnloaderCommand;
import org.mortisdevelopment.mortisallinone.motd.MotdListener;
import org.mortisdevelopment.mortisallinone.pluginhandler.PluginHandlingCommand;
import org.mortisdevelopment.mortisallinone.rtp.RandomTeleportCommand;
import org.mortisdevelopment.mortisallinone.spawn.SpawnPointCommand;
import org.mortisdevelopment.mortisallinone.spawn.SpawnTeleportCommand;
import org.mortisdevelopment.mortisallinone.spawn.SpawnTeleportListener;
import org.mortisdevelopment.mortisallinone.spawner.SpawnerGiveCommand;
import org.mortisdevelopment.mortisallinone.warp.SetWarpCommand;
import org.mortisdevelopment.mortisallinone.warp.WarpTeleportCommand;

import java.io.File;
import java.util.List;

public final class MortisAllinOne extends JavaPlugin {

    @Override
    public void onEnable() {
        loadWorlds();
        saveDefaultConfig();

        getLogger().info("Mortis All-in-One was successfully loaded!");
        getLogger().info("Running version 1.3.");
        getCommand("setspawn").setExecutor(new SpawnPointCommand(this));
        getCommand("spawn").setExecutor(new SpawnTeleportCommand(this));
        getServer().getPluginManager().registerEvents(new SpawnTeleportListener(this), this);
        getCommand("rtp").setExecutor(new RandomTeleportCommand(this));
        getCommand("setwarp").setExecutor(new SetWarpCommand(this));
        getCommand("warp").setExecutor(new WarpTeleportCommand(this));
        getServer().getPluginManager().registerEvents(new MotdListener(this), this);
        getCommand("mao").setExecutor(new WorldGenerateCommand(this));
        getCommand("maotp").setExecutor(new WorldTeleporter(this));
        getCommand("maoimport").setExecutor(new WorldImportCommand(this));
        getCommand("maounload").setExecutor(new WorldUnloaderCommand(this));
        getCommand("spawner").setExecutor(new SpawnerGiveCommand(this));
        getCommand("maoplugin").setExecutor(new PluginHandlingCommand(this));

    }

    private File getFile(String name) {
        File file = new File(getDataFolder(), name);
        if (!file.exists()) {
            saveResource(name, true);
        }
        return file;
    }

    private void loadWorlds() {
        File file = getFile("worlds.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        List<String> worlds = config.getStringList("worlds");
        for (String worldName : worlds) {
            if (Bukkit.getWorld(worldName) == null) {
                World world = new WorldCreator(worldName).createWorld();
                getLogger().info("Loading world: " + world.getName());
            }
        }
        getLogger().info("Mortis All-in-One successfully finished loading worlds!");
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}