package com.navel.navalbattle.database;
import javafx.scene.chart.PieChart;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private static final String URL = "jdbc:postgresql://localhost:5432/NavalBattle";
    private static final String user = "postgres";
    private static final String password = "12121938";
    private static Connection connection = null;

    public DatabaseConnector() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, user, password);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static Connection getConnection() {
        return connection; //!!!!!!!!!!!!!!!!!!!!!!
    }
}
