package com.preston.essentials;

import com.preston.essentials.commands.CommandRegistry;
import com.preston.essentials.utils.Messages;
import org.bukkit.plugin.java.JavaPlugin;

public class Essentials extends JavaPlugin {

    private static Essentials instance;
    private Messages messages;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        messages = new Messages(this);

        new CommandRegistry(this, messages).registerAll();

        getLogger().info("EasyEssentials has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("EasyEssentials has been disabled!");
        instance = null;
    }

    public static Essentials getInstance() {
        return instance;
    }

    public Messages getMessages() {
        return messages;
    }
}
