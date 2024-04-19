package com.navel.navalbattle.database;
import javafx.scene.chart.PieChart;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.postgresql.util.PSQLException;

public class DatabaseConnector {
    private static final String URL = "jdbc:postgresql://localhost:5432/NavalBattle";
    private static final String user = "postgres";
    private static final String password = "12345";
    private static Connection connection = null;
    private static int userId;

    public static boolean makeConnection() {
        try {
            connection = DriverManager.getConnection(URL, user, password);
            return true;
        } catch (SQLException e) {
            Throwable cause = e.getCause();
            if (cause instanceof ConnectException) {
                System.out.println("Database is offline");
                return false;
            }
            e.printStackTrace();
            return false;
        }
    }

    public static void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection; //!!!!!!!!!!!!!!!!!!!!!!
    }

    public static void setUserId(int userId) {
        DatabaseConnector.userId = userId;
    }

    public static int getUserId() {
        return userId;
    }
}
