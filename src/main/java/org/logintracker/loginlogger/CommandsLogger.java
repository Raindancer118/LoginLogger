package org.logintracker.loginlogger;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

public class CommandsLogger implements Listener {

    private final File cmdLogFile;
    private final Logger logger;

    public CommandsLogger(JavaPlugin plugin) {
        this.logger = plugin.getLogger();
        this.cmdLogFile = new File(plugin.getDataFolder(), "commandslog.txt");
        if (!cmdLogFile.exists()) {
            try {
                plugin.getDataFolder().mkdirs();
                cmdLogFile.createNewFile();
            } catch (IOException e) {
                logger.warning("Konnte commandslog.txt nicht erstellen: " + e.getMessage());
            }
        }
    }

    private void writeCommandLog(String message) {
        try (FileWriter fw = new FileWriter(cmdLogFile, true)) {
            fw.write("[" + new Date() + "] " + message + "\n");
        } catch (IOException e) {
            logger.warning("Fehler beim Schreiben in commandslog.txt: " + e.getMessage());
        }
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        String player = event.getPlayer().getName();
        String cmd    = event.getMessage();
        String msg    = "PLAYER:  " + player + " issued " + cmd;
        logger.info(msg);
        writeCommandLog(msg);
    }

    @EventHandler
    public void onConsoleCommand(ServerCommandEvent event) {
        String cmd = event.getCommand();
        String msg = "CONSOLE issued " + cmd;
        logger.info(msg);
        writeCommandLog(msg);
    }
}
