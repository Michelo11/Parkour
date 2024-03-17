package me.michelemanna.parkour.data;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class Parkour {
    private final int id;
    private final String name;
    private final List<Location> checkpoints = new ArrayList<>();

    public Parkour(int id, String name, List<Location> checkpoints) {
        this.id = id;
        this.name = name;
        this.checkpoints.addAll(checkpoints);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Location> getCheckpoints() {
        return checkpoints;
    }
}