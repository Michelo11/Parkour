package me.michelemanna.parkour.managers.providers;

import me.michelemanna.parkour.ParkourPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteProvider implements DatabaseProvider {
    private Connection connection;

    @Override
    public void connect() throws SQLException, IOException {
        File file = new File(ParkourPlugin.getInstance().getDataFolder(), "database.db");
        if (!file.exists()) {
            file.createNewFile();
        }

        connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());

        Statement statement = connection.createStatement();

        statement.execute(
                "CREATE TABLE IF NOT EXISTS parkours (" +
                        "id INTEGER AUTOINCREMENT PRIMARY KEY," +
                        "name VARCHAR(36) NOT NULL," +
                        "checkpoint TEXT" +
                        ")"
        );

        statement.close();
    }

    @Override
    public void disconnect() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public void closeConnection(Connection connection) {

    }
}
