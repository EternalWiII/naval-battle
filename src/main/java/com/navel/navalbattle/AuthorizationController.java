package com.navel.navalbattle;

import com.navel.navalbattle.database.DatabaseConnector;
import com.navel.navalbattle.interfaces.WindowsManipulations;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorizationController extends Controller implements WindowsManipulations {
    private Connection connection;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;

    public void makeConnection() {
        DatabaseConnector databaseConnector = new DatabaseConnector();
        connection = databaseConnector.getConnection();
    }
    @FXML
    private void onLoginBtnPress(ActionEvent e) throws SQLException, IOException {
        String query = "SELECT EXISTS (\n" +
                "    SELECT 1\n" +
                "    FROM accounts\n" +
                "    WHERE username = ? AND password = ?\n" +
                ") AS \"exists\";";
        PreparedStatement statement = DatabaseConnector.getConnection().prepareStatement(query);
        statement.setString(1, usernameField.getText());
        statement.setString(2, passwordField.getText());
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {

            if (resultSet.getBoolean(1)) {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("main_menu.fxml"));
                Scene scene = new Scene(loader.load());
                MainMenuController controller = loader.getController();

                Stage stage = (Stage) usernameField.getScene().getWindow();

                stage.setScene(scene);

                scene.setOnKeyPressed(event -> {
                    event.consume();

                    if (event.getCode() == KeyCode.ESCAPE) {
                        processExit(stage);
                    }
                });
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "You are not authorized", ButtonType.OK);
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void onRegisterBtnPress() throws SQLException, IOException {
        String query = "INSERT INTO accounts (username, password) VALUES (?, ?);";
        PreparedStatement statement = DatabaseConnector.getConnection().prepareStatement(query);
        statement.setString(1, usernameField.getText());
        statement.setString(2, passwordField.getText());

        try {
            statement.executeUpdate();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("main_menu.fxml"));
            Scene scene = new Scene(loader.load());
            MainMenuController controller = loader.getController();

            Stage stage = (Stage) usernameField.getScene().getWindow();

            stage.setScene(scene);

            scene.setOnKeyPressed(event -> {
                event.consume();

                if (event.getCode() == KeyCode.ESCAPE) {
                    processExit(stage);
                }
            });
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Registration failed", ButtonType.OK);
            alert.showAndWait();
        }
    }
}
