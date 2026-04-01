package com.preston.basic;

import com.preston.basic.commands.CommandRegistry;
import com.preston.basic.listeners.FlyListener;
import com.preston.basic.utils.Messages;
import org.bukkit.plugin.java.JavaPlugin;

public class Basic extends JavaPlugin {

    private static Basic instance;
    private Messages messages;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        messages = new Messages(this);

        new CommandRegistry(this, messages).registerAll();

        getServer()
            .getPluginManager()
            .registerEvents(new FlyListener(this), this);

        getLogger().info("EasyEssentials has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("EasyEssentials has been disabled!");
        instance = null;
    }

    public static Basic getInstance() {
        return instance;
    }

    public Messages getMessages() {
        return messages;
    }
}
