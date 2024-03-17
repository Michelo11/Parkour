package me.michelemanna.parkour.listeners;

import me.michelemanna.parkour.ParkourPlugin;
import me.michelemanna.parkour.data.Parkour;
import me.michelemanna.parkour.data.ParkourSession;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerListener implements Listener {
    private final ParkourPlugin plugin;

    public PlayerListener(ParkourPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (event.getTo() == null) return;

        if (event.getFrom().getX() == event.getTo().getX() &&
                event.getFrom().getY() == event.getTo().getY() &&
                event.getFrom().getZ() == event.getTo().getZ()) {
            return;
        }

        Location location = event.getTo().getBlock().getLocation();

        Parkour parkour = plugin.getParkourManager()
                .getParkours()
                .values()
                .stream()
                .filter(p -> p.getCheckpoints().contains(location))
                .findFirst()
                .orElse(null);

        if (parkour == null) {
            return;
        }

        ParkourSession session = plugin.getParkourManager().getSession(event.getPlayer());
        if (session == null) {
            plugin.getParkourManager().start(event.getPlayer(), parkour);
            return;
        }

        if (!session.getParkour().equals(parkour)) {
            return;
        }

        if ((session.getCheckpointIndex() + 1) == parkour.getCheckpoints().size()) {
            plugin.getParkourManager().finish(event.getPlayer());
            long time = System.currentTimeMillis() - session.getStartTime();
            event.getPlayer().sendMessage("§aCongratulations, you finished the parkour in " + time + "ms!");
            return;
        }

        session.setCheckpointIndex(parkour.getCheckpoints().indexOf(location));
    }
}