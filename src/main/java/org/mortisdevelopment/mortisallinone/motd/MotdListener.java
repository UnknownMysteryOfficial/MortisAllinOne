package org.mortisdevelopment.mortisallinone.motd;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.mortisdevelopment.mortisallinone.MortisAllinOne;

public class MotdListener implements Listener {

    private final MortisAllinOne plugin;
    private boolean motdEnabled;
    private String motd;

    public MotdListener(MortisAllinOne plugin) {
        this.plugin = plugin;
        motdEnabled = plugin.getConfig().getBoolean("motd-enabled");
        motd = ChatColor.translateAlternateColorCodes('&', String.valueOf(plugin.getConfig().getString("motd.1")) + String.valueOf(plugin.getConfig().getString("motd.2")));
    }

    @EventHandler
    public void onPlayerPing(ServerListPingEvent event){
        if (motdEnabled){
            event.setMotd(motd);
        }
    }

}
