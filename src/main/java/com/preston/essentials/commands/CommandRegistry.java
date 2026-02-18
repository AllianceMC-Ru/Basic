package com.preston.essentials.commands;

import com.preston.essentials.Essentials;
import com.preston.essentials.utils.Messages;
import org.bukkit.command.PluginCommand;

public class CommandRegistry {

    private final Essentials plugin;
    private final Messages messages;

    public CommandRegistry(Essentials plugin, Messages messages) {
        this.plugin = plugin;
        this.messages = messages;
    }

    public void registerAll() {
        registerGamemodeCommands();
    }

    private void registerGamemodeCommands() {
        GamemodeCommands gamemodeCommands = new GamemodeCommands(messages);
        register("gm", gamemodeCommands);
    }

    private void register(String commandName, Object handler) {
        PluginCommand command = plugin.getCommand(commandName);
        if (command == null) {
            plugin.getLogger().warning(
                "Command '/" + commandName + "' is not defined in plugin.yml — skipping registration."
            );
            return;
        }

        if (handler instanceof org.bukkit.command.CommandExecutor) {
            command.setExecutor((org.bukkit.command.CommandExecutor) handler);
        }

        if (handler instanceof org.bukkit.command.TabCompleter) {
            command.setTabCompleter((org.bukkit.command.TabCompleter) handler);
        }
    }
}
