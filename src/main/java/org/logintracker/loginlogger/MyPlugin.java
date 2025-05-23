package org.logintracker.loginlogger;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MyPlugin extends JavaPlugin {

    private File logFile;
    private long startTime;

    @Override
    public void onEnable() {
        startTime = System.currentTimeMillis();

        getLogger().info("=======================================");
        getLogger().info(" _                _       _                                ");
        getLogger().info("| |    ___   __ _(_)_ __ | |    ___   __ _  __ _  ___ _ __ ");
        getLogger().info("| |   / _ \\ / _` | | '_ \\| |   / _ \\ / _` |/ _` |/ _ \\ '__|");
        getLogger().info("| |__| (_) | (_| | | | | | |__| (_) | (_| | (_| |  __/ |   ");
        getLogger().info("|_____\\___/ \\__, |_|_| |_|_____|___/ \\__, |\\__, |\\___|_|   ");
        getLogger().info("            |___/                    |___/ |___/           ");
        getLogger().info("LoginLogger wurde erfolgreich aktiviert!");
        getLogger().info("Alle Login/Logout-Events werden geloggt.");
        getLogger().info("Log-Datei: plugins/LoginLogger/logs.txt");
        getLogger().info("=======================================");
        logFile = new File(getDataFolder(), "logs.txt");

        if (!logFile.exists()) {
            try {
                getDataFolder().mkdirs();
                logFile.createNewFile();
            } catch (IOException e) {
                getLogger().warning("Konnte Logdatei nicht erstellen: " + e.getMessage());
            }
        }

        writeLog("Server wurde gestartet.");
        getServer().getPluginManager().registerEvents(new PlayerSessionTracker(this), this);
        getServer().getPluginManager().registerEvents(new CommandsLogger(this), this);
    }

    @Override
    public void onDisable() {
        long stopTime = System.currentTimeMillis();
        long uptimeMillis = stopTime - startTime;
        long seconds = uptimeMillis / 1000 % 60;
        long minutes = (uptimeMillis / (1000 * 60)) % 60;
        long hours = (uptimeMillis / (1000 * 60 * 60)) % 24;

        String message = "Server wird heruntergefahren nach " + hours + "h " + minutes + "m " + seconds + "s Laufzeit.";
        getLogger().info("=======================================");
        getLogger().info("LoginLogger wird deaktiviert...");
        getLogger().info("Gesamtlaufzeit des Servers: " + hours + "h " + minutes + "m " + seconds + "s");
        getLogger().info("Log-Datei aktualisiert.");
        getLogger().info("Bye Bye!");
        getLogger().info("=======================================");
        writeLog(message);
    }

    private void writeLog(String message) {
        try (FileWriter fw = new FileWriter(logFile, true)) {
            fw.write("[" + new java.util.Date() + "] " + message + "\n");
        } catch (IOException e) {
            getLogger().warning("Fehler beim Schreiben in Logdatei: " + e.getMessage());
        }
    }
}
