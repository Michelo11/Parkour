package me.michelemanna.parkour;

import me.michelemanna.parkour.commands.ParkourCommand;
import me.michelemanna.parkour.listeners.PlayerListener;
import me.michelemanna.parkour.managers.ParkourManager;
import org.bukkit.plugin.java.JavaPlugin;
import me.michelemanna.parkour.managers.DatabaseManager;

import java.sql.SQLException;

public final class ParkourPlugin extends JavaPlugin {
    private static ParkourPlugin instance;
    private DatabaseManager database;
    private ParkourManager parkourManager;

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();

        getCommand("parkour").setExecutor(new ParkourCommand(this));
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        try {
            this.database = new DatabaseManager(this);

            this.parkourManager = new ParkourManager(this);
            this.parkourManager.loadParkours();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onDisable() {
        this.database.close();
    }

    public static ParkourPlugin getInstance() {
        return instance;
    }

    public DatabaseManager getDatabase() {
        return database;
    }

    public ParkourManager getParkourManager() {
        return parkourManager;
    }
}
