package org.mortisdevelopment.mortisallinone;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.mortisdevelopment.mortisallinone.generator.*;
import org.mortisdevelopment.mortisallinone.homes.HomeTeleportCommand;
import org.mortisdevelopment.mortisallinone.homes.SetHomeCommand;
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
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.bukkit.Bukkit.getLogger;

public final class MortisAllinOne extends JavaPlugin {

    private WorldManager worldManager;

    @Override
    public void onEnable() {

        this.worldManager = new WorldManager(this);
        loadWorlds();
        saveDefaultConfig();
        File file = getFile("homes.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        File file2 = getFile("warps.yml");
        FileConfiguration config2 = YamlConfiguration.loadConfiguration(file2);

        if (!getConfig().getBoolean("rtp-enable", true)) {
            unregisterCommand("rtp");
        }

        getLogger().info("Mortis All-in-One was successfully loaded!");
        getLogger().info("Running version 1.6");
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
        getCommand("sethome").setExecutor(new SetHomeCommand(this));
        getCommand("home").setExecutor(new HomeTeleportCommand(this));
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

    public WorldManager getWorldManager(){
        return worldManager;
    }


    private void unregisterCommand(String commandName) {
        try {
            PluginManager pluginManager = Bukkit.getPluginManager();

            Field commandMapField = pluginManager.getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            CommandMap commandMap = (CommandMap) commandMapField.get(pluginManager);

            Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
            knownCommandsField.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<String, Command> knownCommands = (Map<String, Command>) knownCommandsField.get(commandMap);

            if (knownCommands.containsKey(commandName)) {
                knownCommands.remove(commandName);
                getLogger().info("Successfully unregistered the /" + commandName + " command.");
            } else {
                getLogger().warning("The /" + commandName + " command was not registered.");
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            getLogger().severe("Failed to unregister the /" + commandName + " command: " + e.getMessage());
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}