package com.preston.basic.commands;

import com.preston.basic.Basic;
import com.preston.basic.utils.Messages;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class BasicCommand implements CommandExecutor, TabCompleter {

    private final Basic plugin;
    private final Messages messages;

    public BasicCommand(Basic plugin, Messages messages) {
        this.plugin = plugin;
        this.messages = messages;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(messages.get("messages.basic.usage"));
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("basic.reload")) {
                sender.sendMessage(messages.get("messages.basic.no-permission"));
                return true;
            }

            plugin.reloadConfig();
            sender.sendMessage(messages.get("messages.basic.reload-success"));
            return true;
        }

        sender.sendMessage(messages.get("messages.basic.usage"));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<String>();

        if (args.length == 1) {
            if (sender.hasPermission("basic.reload")) {
                String input = args[0].toLowerCase();
                if ("reload".startsWith(input)) {
                    completions.add("reload");
                }
            }
        }

        return completions;
    }
}
