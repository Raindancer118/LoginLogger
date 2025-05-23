package org.logintracker.loginlogger;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

public class PlayerSessionTracker implements Listener {

    private final HashMap<UUID, Long> loginTimes = new HashMap<>();
    private final Logger logger;
    
    private final File logFile;

    public PlayerSessionTracker(JavaPlugin plugin) {
        this.logger = plugin.getLogger();
        this.logFile = new File(plugin.getDataFolder(), "logs.txt");
        if (!logFile.exists()) {
            try {
                plugin.getDataFolder().mkdirs();
                logFile.createNewFile();
            } catch (IOException e) {
                logger.warning("Konnte Logdatei nicht erstellen: " + e.getMessage());
            }
        }
    }

    private void writeLog(String message) {
        try (FileWriter fw = new FileWriter(logFile, true)) {
            fw.write("[" + new java.util.Date() + "] " + message + "\n");
        } catch (IOException e) {
            logger.warning("Fehler beim Schreiben in Logdatei: " + e.getMessage());
        }
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        long currentTime = System.currentTimeMillis();
        loginTimes.put(playerId, currentTime);

        String message = event.getPlayer().getName() + " hat sich eingeloggt um " + currentTime;
        logger.info(message);
        writeLog(message);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        long logoutTime = System.currentTimeMillis();
        Long loginTime = loginTimes.remove(playerId);

        if (loginTime != null) {
            long playTimeMillis = logoutTime - loginTime;
            long seconds = playTimeMillis / 1000 % 60;
            long minutes = (playTimeMillis / (1000 * 60)) % 60;
            long hours = (playTimeMillis / (1000 * 60 * 60)) % 24;

            String message = event.getPlayer().getName() + " hat sich ausgeloggt um " + logoutTime +
                             " nach " + hours + "h " + minutes + "m " + seconds + "s Spielzeit.";
            logger.info(message);
            writeLog(message);
        } else {
            String message = "Logout-Zeit konnte nicht berechnet werden für " + event.getPlayer().getName();
            logger.warning(message);
            writeLog(message);
        }
    }
}
