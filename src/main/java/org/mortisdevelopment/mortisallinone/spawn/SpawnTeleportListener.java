package org.mortisdevelopment.mortisallinone.spawn;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.mortisdevelopment.mortisallinone.MortisAllinOne;


public class SpawnTeleportListener implements Listener {

    private final MortisAllinOne plugin;
    private boolean teleportOnJoin;
    private boolean teleportOnDeath;
    private boolean teleportOnFirstJoin;

    public SpawnTeleportListener(MortisAllinOne plugin) {
        this.plugin = plugin;
        teleportOnJoin = plugin.getConfig().getBoolean("teleport-on-join", false);
        teleportOnDeath = plugin.getConfig().getBoolean("teleport-on-death", false);
        teleportOnFirstJoin = plugin.getConfig().getBoolean("teleport-on-first-join", true);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){

        Player player = event.getPlayer();
        Location location = plugin.getConfig().getLocation("spawn");

        if (teleportOnJoin){

            if (location != null){

                player.teleport(location);

            }

        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){

        Entity entity = event.getEntity();
        if (entity instanceof Player){

            Player player = event.getPlayer();

            Location location = plugin.getConfig().getLocation("spawn");

            if (teleportOnDeath){

                if (location != null){

                    player.setBedSpawnLocation(location);

                }

            }

        }

    }

    @EventHandler
    public void onPlayerFirstJoin(PlayerJoinEvent event){

        Player player = event.getPlayer();
        Location location = plugin.getConfig().getLocation("spawn");

        if (!player.hasPlayedBefore()){

            if (teleportOnFirstJoin){

                if (location != null){

                    player.teleport(location);

                }

            }

        }

    }

}