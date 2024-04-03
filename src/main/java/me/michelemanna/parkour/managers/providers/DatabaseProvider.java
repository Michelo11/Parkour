package me.michelemanna.parkour.managers.providers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseProvider {
    void connect() throws SQLException, ClassNotFoundException, IOException;
    void disconnect() throws SQLException;
    Connection getConnection() throws SQLException;
    void closeConnection(Connection connection) throws SQLException;
}
