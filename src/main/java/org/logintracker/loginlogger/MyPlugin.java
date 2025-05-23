package org.logintracker.loginlogger;

import org.bukkit.plugin.java.JavaPlugin;

public class MyPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("LoginLogger aktiviert.");
        getLogger().info("Starte Logging.")
        getServer().getPluginManager().registerEvents(new PlayerSessionTracker(this), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("LoginLogger deaktiviert. Goodbye!");
    }
}
