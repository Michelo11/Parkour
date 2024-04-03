package me.michelemanna.parkour.managers.providers;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.michelemanna.parkour.ParkourPlugin;
import org.bukkit.configuration.ConfigurationSection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class MySQLProvider implements DatabaseProvider {
    private HikariDataSource dataSource;

    @Override
    public void connect() throws SQLException, ClassNotFoundException {
        ConfigurationSection cs = ParkourPlugin.getInstance()
                .getConfig()
                .getConfigurationSection("mysql");
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

    @Override
    public void disconnect() throws SQLException {
        if (dataSource != null)
           dataSource.close();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void closeConnection(Connection connection) throws SQLException {
        connection.close();
    }
}
