package net.pinger.history.sql;

import org.bukkit.Bukkit;

import java.sql.*;
import java.util.logging.Level;

public class Database {

    private String name, password, host, database;
    private int port;

    private Connection connection;

    public Database(String name, String password, String db, String host, int port) {
        this.name = name;
        this.password = password;
        this.database = db;
        this.host = host;
        this.port = port;
    }

    public void createConnection() {
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, name, password);
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.SEVERE, "[History] Failed to connect to sql server!!!");
            Bukkit.getLogger().log(Level.SEVERE, "[History] Failed to connect to sql server!!!");
            Bukkit.getLogger().log(Level.SEVERE, "[History] Failed to connect to sql server!!!");
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static void close(PreparedStatement preparedStatement, ResultSet resultSet) {
        try {
            // Necessary check
            if (preparedStatement != null) {
                if (!preparedStatement.isClosed()) {
                    preparedStatement.close();
                }
            }

            // Necessary check
            if (resultSet != null) {
                if (!resultSet.isClosed()) {
                    resultSet.close();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
