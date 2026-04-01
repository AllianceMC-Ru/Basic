package com.preston.basic.commands;

import com.preston.basic.Basic;
import com.preston.basic.utils.Messages;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor, TabCompleter {

    private final Messages messages;

    public FlyCommand(Messages messages) {
        this.messages = messages;
    }

    @Override
    public boolean onCommand(
        CommandSender sender,
        Command command,
        String label,
        String[] args
    ) {
        // /fly <player> — toggle fly for another player
        if (args.length >= 1) {
            if (!sender.hasPermission("basic.fly.other")) {
                sender.sendMessage(
                    messages.get("messages.fly.no-permission-other")
                );
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(
                    messages.get(
                        "messages.fly.player-not-found",
                        "player",
                        args[0]
                    )
                );
                return true;
            }

            boolean enabling = !target.getAllowFlight();
            target.setAllowFlight(enabling);
            target.setFlying(enabling);

            String state = enabling
                ? messages.get("messages.fly.state-enabled")
                : messages.get("messages.fly.state-disabled");

            sender.sendMessage(
                messages.get(
                    "messages.fly.other-toggled-sender",
                    "player",
                    target.getName(),
                    "state",
                    state
                )
            );
            target.sendMessage(
                messages.get(
                    "messages.fly.other-toggled-target",
                    "sender",
                    sender.getName(),
                    "state",
                    state
                )
            );
            return true;
        }

        // /fly — toggle fly for yourself
        if (!(sender instanceof Player)) {
            sender.sendMessage(messages.get("messages.fly.console-no-player"));
            return true;
        }

        Player player = (Player) sender;

        if (player.hasPermission("basic.fly")) {
            // Full fly — any world
            toggleFly(player);
            return true;
        }

        if (player.hasPermission("basic.fly.worlds")) {
            // World-restricted fly
            Set<String> allowedWorlds = new HashSet<String>();
            for (String w : Basic.getInstance()
                .getConfig()
                .getStringList("fly.allowed-worlds")) {
                allowedWorlds.add(w.toLowerCase());
            }

            if (
                !allowedWorlds.contains(
                    player.getWorld().getName().toLowerCase()
                )
            ) {
                player.sendMessage(
                    messages.get("messages.fly.world-not-allowed")
                );
                return true;
            }

            toggleFly(player);
            return true;
        }

        player.sendMessage(messages.get("messages.fly.no-permission"));
        return true;
    }

    private void toggleFly(Player player) {
        boolean enabling = !player.getAllowFlight();
        player.setAllowFlight(enabling);
        player.setFlying(enabling);

        String state = enabling
            ? messages.get("messages.fly.state-enabled")
            : messages.get("messages.fly.state-disabled");
        player.sendMessage(
            messages.get("messages.fly.self-toggled", "state", state)
        );
    }

    @Override
    public List<String> onTabComplete(
        CommandSender sender,
        Command command,
        String alias,
        String[] args
    ) {
        List<String> completions = new ArrayList<String>();

        if (args.length == 1 && sender.hasPermission("basic.fly.other")) {
            String input = args[0].toLowerCase();
            for (Player online : Bukkit.getOnlinePlayers()) {
                if (online.getName().toLowerCase().startsWith(input)) {
                    completions.add(online.getName());
                }
            }
        }

        return completions;
    }
}
