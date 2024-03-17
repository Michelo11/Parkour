package me.michelemanna.parkour.data;

import org.bukkit.entity.Player;

public class ParkourSession {
    private final Parkour parkour;
    private int checkpointIndex;

    public ParkourSession(Parkour parkour) {
        this.parkour = parkour;
        this.checkpointIndex = 0;
    }

    public Parkour getParkour() {
        return parkour;
    }

    public int getCheckpointIndex() {
        return checkpointIndex;
    }

    public void setCheckpointIndex(int checkpointIndex) {
        this.checkpointIndex = checkpointIndex;
    }

    public void teleport(Player player) {
        player.teleport(parkour.getCheckpoints().get(checkpointIndex));
    }

    public void restart() {
        checkpointIndex = 0;
    }
}
