package me.michelemanna.parkour.managers;

import com.google.gson.Gson;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.michelemanna.parkour.ParkourPlugin;
import me.michelemanna.parkour.data.Parkour;
import me.michelemanna.parkour.managers.providers.DatabaseProvider;
import me.michelemanna.parkour.managers.providers.MySQLProvider;
import me.michelemanna.parkour.managers.providers.SQLiteProvider;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class DatabaseManager {
    private final DatabaseProvider dataSource;
    private final Gson gson = new Gson();

    public DatabaseManager() throws SQLException, ClassNotFoundException, IOException {
        ConfigurationSection cs = ParkourPlugin.getInstance()
                .getConfig()
                .getConfigurationSection("mysql");
        Objects.requireNonNull(cs, "Unable to find the following key: mysql");

        if (cs.getString("type", "mysql").equalsIgnoreCase("mysql")) {
            this.dataSource = new MySQLProvider();
        } else
            this.dataSource = new SQLiteProvider();

        dataSource.connect();
    }

    public void close() throws SQLException {
        dataSource.disconnect();
    }

    @SuppressWarnings("unchecked")
    public CompletableFuture<Map<String, Parkour>> getParkours() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Connection connection = dataSource.getConnection();

                PreparedStatement statement = connection.prepareStatement("SELECT * FROM parkours;");

                ResultSet result = statement.executeQuery();

                Map<String, Parkour> parkours = new HashMap<>();

                while (result.next()) {
                    int id = result.getInt("id");
                    String name = result.getString("name");
                    List<Map<String, Object>> checkpoints = gson.fromJson(result.getString("checkpoint"), List.class);

                    Parkour value = new Parkour(id, name, checkpoints.stream().map(Location::deserialize).toList());
                    parkours.put(name, value);

                    value.setFinished(true);
                }

                result.close();
                statement.close();
                dataSource.closeConnection(connection);

                return parkours;
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return new HashMap<>();
        }, (runnable) -> Bukkit.getScheduler().runTaskAsynchronously(ParkourPlugin.getInstance(), runnable));
    }

    public void createParkour(Parkour parkour) {
        Bukkit.getScheduler().runTaskAsynchronously(ParkourPlugin.getInstance(), () -> {
            try {
                Connection connection = dataSource.getConnection();

                PreparedStatement statement = connection.prepareStatement("INSERT INTO parkours (name, checkpoint) VALUES (?, ?);");

                statement.setString(1, parkour.getName());
                statement.setString(2, gson.toJson(parkour.getCheckpoints().stream().map(Location::serialize).toList()));


                statement.executeUpdate();
                statement.close();
                dataSource.closeConnection(connection);

                ParkourPlugin.getInstance().getParkourManager().loadParkours();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void deleteParkour(Parkour parkour) {
        Bukkit.getScheduler().runTaskAsynchronously(ParkourPlugin.getInstance(), () -> {
            try {
                Connection connection = dataSource.getConnection();

                PreparedStatement statement = connection.prepareStatement("DELETE FROM parkours WHERE id = ?;");

                statement.setInt(1, parkour.getId());

                statement.executeUpdate();
                statement.close();
                dataSource.closeConnection(connection);

                Bukkit.getScheduler().runTask(ParkourPlugin.getInstance(), () -> {
                    parkour.getCheckpoints()
                            .forEach(location -> location.getWorld().getNearbyEntities(location, 3, 3, 3, entity -> entity instanceof ArmorStand &&
                                            entity.getCustomName() != null &&
                                            entity.getCustomName().contains("Checkpoint"))
                                    .forEach(Entity::remove));
                });

                ParkourPlugin.getInstance().getParkourManager().getParkours().remove(parkour.getName());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}