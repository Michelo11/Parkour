package me.michelemanna.parkour.managers;

import me.michelemanna.parkour.ParkourPlugin;
import me.michelemanna.parkour.data.Parkour;
import me.michelemanna.parkour.data.ParkourSession;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ParkourManager {
    private final ParkourPlugin plugin;
    private final Map<String, Parkour> parkours = new HashMap<>();
    private final Map<Player, ParkourSession> players = new HashMap<>();

    public ParkourManager(ParkourPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadParkours() {
        plugin.getDatabase().getParkours().thenAccept(this.parkours::putAll);
    }

    public void start(Player player, Parkour parkour) {
        player.teleport(parkour.getCheckpoints().get(0));
        players.put(player, new ParkourSession(parkour));
    }

    public void finish(Player player) {
        players.remove(player);
    }

    public ParkourSession getSession(Player player) {
        return players.get(player);
    }

    public Map<String, Parkour> getParkours() {
        return parkours;
    }

    public Parkour getParkour(String name) {
        return parkours.get(name);
    }
}