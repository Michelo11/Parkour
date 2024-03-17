package me.michelemanna.parkour.managers;

import com.google.gson.Gson;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.michelemanna.parkour.ParkourPlugin;
import me.michelemanna.parkour.data.Parkour;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class DatabaseManager {
    private final HikariDataSource dataSource;
    private final Gson gson = new Gson();

    public DatabaseManager(ParkourPlugin plugin) throws SQLException, ClassNotFoundException {
        ConfigurationSection cs = plugin.getConfig().getConfigurationSection("mysql");
        Objects.requireNonNull(cs, "Unable to find the following key: mysql");
        HikariConfig config = new HikariConfig();

        Class.forName("com.mysql.cj.jdbc.Driver");

        config.setJdbcUrl("jdbc:mysql://" + cs.getString("host") + ":" + cs.getString("port") + "/" + cs.getString("database"));
        config.setUsername(cs.getString("username"));
        config.setPassword(cs.getString("password"));
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setConnectionTimeout(10000);
        config.setLeakDetectionThreshold(10000);
        config.setMaximumPoolSize(10);
        config.setMaxLifetime(60000);
        config.setPoolName("ParkourPool");
        config.addDataSourceProperty("useSSL", cs.getBoolean("ssl"));

        this.dataSource = new HikariDataSource(config);

        Connection connection = dataSource.getConnection();

        Statement statement = connection.createStatement();

        statement.execute(
                "CREATE TABLE IF NOT EXISTS parkours (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY," +
                        "name VARCHAR(36) NOT NULL," +
                        "checkpoint TEXT" +
                        ")"
        );

        statement.close();
        connection.close();
    }

    public void close() {
        dataSource.close();
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
                connection.close();

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
                connection.close();

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
                connection.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
