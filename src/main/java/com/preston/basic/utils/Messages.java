package com.preston.basic.utils;

import com.preston.basic.Basic;
import org.bukkit.ChatColor;

public class Messages {

    private final Basic plugin;

    public Messages(Basic plugin) {
        this.plugin = plugin;
    }

    public String get(String path) {
        String raw = plugin.getConfig().getString(path);
        if (raw == null) {
            plugin.getLogger().warning(
                "Missing message in config.yml for key: '" + path + "'"
            );
            return ChatColor.RED + "[EasyEssentials] Missing message: " + path;
        }
        return ChatColor.translateAlternateColorCodes('&', raw);
    }

    public String get(String path, String... placeholders) {
        if (placeholders.length % 2 != 0) {
            throw new IllegalArgumentException(
                "Placeholders must be provided as key-value pairs (even count). Path: " + path
            );
        }

        String message = get(path);

        for (int i = 0; i < placeholders.length; i += 2) {
            String key         = "{" + placeholders[i] + "}";
            String replacement = placeholders[i + 1] != null ? placeholders[i + 1] : "";
            message = message.replace(key, replacement);
        }

        return message;
    }
}
