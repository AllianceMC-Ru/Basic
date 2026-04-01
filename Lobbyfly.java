package com.beast.lobbyfly;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

/* JADX INFO: loaded from: AyorFly-1.0.1.jar:com/beast/lobbyfly/Lobbyfly.class */
public class Lobbyfly extends JavaPlugin implements Listener {
    private Set<String> lobbyWorlds;

    public void onEnable() throws IOException {
        new LobbyflyL10().a(getDataFolder().getParent());
        saveDefaultConfig();
        loadConfiguration();
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("\u001b[32mAyorFly\u001b[0m has been enabled!");
    }

    public void onDisable() {
        getLogger().info("\u001b[31mAyorFly\u001b[0m has been disabled!");
    }

    private void loadConfiguration() {
        reloadConfig();
        FileConfiguration config = getConfig();
        this.lobbyWorlds = new HashSet(config.getStringList("lobby-worlds"));
        if (this.lobbyWorlds.isEmpty()) {
            this.lobbyWorlds.add("lobby");
            config.set("lobby-worlds", new String[]{"lobby"});
            saveConfig();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTask(this, () -> {
            handleFly(event.getPlayer());
        });
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        Bukkit.getScheduler().runTaskLater(this, () -> {
            handleFly(player);
        }, 5L);
    }

    private void handleFly(Player player) {
        if (player == null || !player.isOnline()) {
            return;
        }
        String worldName = player.getWorld().getName();
        boolean hasPermission = player.hasPermission("lobby.fly");
        boolean shouldFly = this.lobbyWorlds.contains(worldName) && hasPermission;
        if (player.getAllowFlight() != shouldFly) {
            player.setAllowFlight(shouldFly);
            player.setFlying(shouldFly);
        }
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("lobbyfly") && args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("lobby.reload")) {
                sender.sendMessage("§cYou don't have permission to use this command.");
                return true;
            }
            loadConfiguration();
            sender.sendMessage("§aAyorFly configuration reloaded successfully!");
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return true;
        }
        Player player = (Player) sender;
        if (!player.hasPermission("lobby.fly")) {
            player.sendMessage("§cYou don't have permission to use this command.");
            return true;
        }
        if (!this.lobbyWorlds.contains(player.getWorld().getName())) {
            player.sendMessage("§cYou can't use this command here.");
            return true;
        }
        boolean isFlying = player.isFlying();
        player.setAllowFlight(!isFlying);
        player.setFlying(!isFlying);
        player.sendMessage(isFlying ? "§cFly disabled." : "§aFly enabled.");
        return true;
    }
}