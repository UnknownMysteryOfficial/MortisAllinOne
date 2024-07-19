package org.mortisdevelopment.mortisallinone.spawner;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mortisdevelopment.mortisallinone.MortisAllinOne;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SpawnerGiveCommand implements CommandExecutor, TabCompleter {

    private final MortisAllinOne plugin;
    private String prefix;

    public SpawnerGiveCommand(MortisAllinOne plugin) {
        this.plugin = plugin;
        prefix = plugin.getConfig().getString("prefix");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (commandSender instanceof Player){

            Player player = (Player) commandSender;

            if (player.hasPermission("mortis.aio.spawner")){

                if (args.length == 0){

                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&cYou need to specify the entity type."));

                }else if (args.length == 1){

                    EntityType entityType;
                    try {
                        entityType = EntityType.valueOf(args[0].toUpperCase());
                    } catch (IllegalArgumentException e) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&cInvalid entity type!"));
                        return true;
                    }

                    if (!entityType.isAlive()) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&cYou can't create a spawner for this entity type!"));
                        return true;
                    }

                    ItemStack spawner = new ItemStack(Material.SPAWNER);
                    BlockStateMeta meta = (BlockStateMeta) spawner.getItemMeta();

                    CreatureSpawner spawner1 = (CreatureSpawner) meta.getBlockState();
                    spawner1.setSpawnedType(entityType);
                    meta.setBlockState(spawner1);
                    spawner.setItemMeta(meta);

                    player.getInventory().addItem(spawner);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&aYou have been given a " + entityType.name() + " spawner!"));

                }

            }

        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (args.length == 1){

            return Arrays.stream(EntityType.values())
                    .map(EntityType::name)
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());

        }

        return new ArrayList<>();
    }
}
