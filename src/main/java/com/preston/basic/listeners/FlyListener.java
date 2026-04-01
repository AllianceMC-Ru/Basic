package com.preston.basic.listeners;

import com.preston.basic.Basic;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class FlyListener implements Listener {

    private final Basic plugin;

    public FlyListener(Basic plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldChange(PlayerChangedWorldEvent event) {
        final Player player = event.getPlayer();
        Bukkit.getScheduler().runTaskLater(
            plugin,
            new Runnable() {
                @Override
                public void run() {
                    handleFlyCheck(player);
                }
            },
            5L
        );
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        Bukkit.getScheduler().runTaskLater(
            plugin,
            new Runnable() {
                @Override
                public void run() {
                    handleFlyCheck(player);
                }
            },
            1L
        );
    }

    private void handleFlyCheck(Player player) {
        if (player == null || !player.isOnline()) {
            return;
        }

        // Player with global fly permission — don't touch their fly state
        if (player.hasPermission("basic.fly")) {
            return;
        }

        // Player with world-restricted fly
        if (player.hasPermission("basic.fly.worlds")) {
            Set<String> allowedWorlds = new HashSet<String>();
            for (String w : plugin
                .getConfig()
                .getStringList("fly.allowed-worlds")) {
                allowedWorlds.add(w.toLowerCase());
            }
            String currentWorld = player.getWorld().getName().toLowerCase();

            if (!allowedWorlds.contains(currentWorld)) {
                // Not in an allowed world — disable fly
                if (player.isFlying() || player.getAllowFlight()) {
                    player.setFlying(false);
                    player.setAllowFlight(false);
                }
            }
            return;
        }

        // Player has no fly permission at all — make sure fly is off
        if (player.getAllowFlight()) {
            player.setFlying(false);
            player.setAllowFlight(false);
        }
    }
}
