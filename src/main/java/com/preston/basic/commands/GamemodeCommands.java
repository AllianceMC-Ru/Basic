package com.preston.basic.commands;

import com.preston.basic.utils.Messages;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class GamemodeCommands implements CommandExecutor, TabCompleter {

    private final Messages messages;

    public GamemodeCommands(Messages messages) {
        this.messages = messages;
    }

    @Override
    public boolean onCommand(
        CommandSender sender,
        Command command,
        String label,
        String[] args
    ) {
        if (args.length == 0) {
            sender.sendMessage(messages.get("messages.gm.usage"));
            return true;
        }

        GameMode targetMode = parseGameMode(args[0]);
        if (targetMode == null) {
            sender.sendMessage(messages.get("messages.gm.invalid-mode"));
            return true;
        }

        // /gm <mode> <player>
        if (args.length >= 2) {
            if (!sender.hasPermission("easyessentials.gm.other")) {
                sender.sendMessage(
                    messages.get("messages.gm.no-permission-other")
                );
                return true;
            }

            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(
                    messages.get(
                        "messages.gm.player-not-found",
                        "player",
                        args[1]
                    )
                );
                return true;
            }

            String formattedMode = formatGameMode(targetMode);

            target.setGameMode(targetMode);
            target.sendMessage(
                messages.get(
                    "messages.gm.other-changed-target",
                    "gamemode",
                    formattedMode,
                    "sender",
                    sender.getName()
                )
            );
            sender.sendMessage(
                messages.get(
                    "messages.gm.other-changed-sender",
                    "player",
                    target.getName(),
                    "gamemode",
                    formattedMode
                )
            );
            return true;
        }

        // /gm <mode> (self)
        if (!(sender instanceof Player)) {
            sender.sendMessage(messages.get("messages.gm.console-no-player"));
            return true;
        }

        if (!sender.hasPermission("easyessentials.gm")) {
            sender.sendMessage(messages.get("messages.gm.no-permission"));
            return true;
        }

        Player player = (Player) sender;
        player.setGameMode(targetMode);
        player.sendMessage(
            messages.get(
                "messages.gm.self-changed",
                "gamemode",
                formatGameMode(targetMode)
            )
        );
        return true;
    }

    @Override
    public List<String> onTabComplete(
        CommandSender sender,
        Command command,
        String alias,
        String[] args
    ) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            List<String> modes = Arrays.asList(
                "0",
                "1",
                "2",
                "3",
                "survival",
                "creative",
                "adventure",
                "spectator"
            );
            String input = args[0].toLowerCase();
            for (String mode : modes) {
                if (mode.startsWith(input)) {
                    completions.add(mode);
                }
            }
        } else if (
            args.length == 2 && sender.hasPermission("easyessentials.gm.other")
        ) {
            String input = args[1].toLowerCase();
            for (Player online : Bukkit.getOnlinePlayers()) {
                if (online.getName().toLowerCase().startsWith(input)) {
                    completions.add(online.getName());
                }
            }
        }

        return completions;
    }

    private GameMode parseGameMode(String input) {
        switch (input.toLowerCase()) {
            case "0":
            case "survival":
            case "s":
                return GameMode.SURVIVAL;
            case "1":
            case "creative":
            case "c":
                return GameMode.CREATIVE;
            case "2":
            case "adventure":
            case "a":
                return GameMode.ADVENTURE;
            case "3":
            case "spectator":
            case "sp":
                return GameMode.SPECTATOR;
            default:
                return null;
        }
    }

    private String formatGameMode(GameMode mode) {
        switch (mode) {
            case SURVIVAL:
                return "Survival (0)";
            case CREATIVE:
                return "Creative (1)";
            case ADVENTURE:
                return "Adventure (2)";
            case SPECTATOR:
                return "Spectator (3)";
            default:
                return mode.name();
        }
    }
}
