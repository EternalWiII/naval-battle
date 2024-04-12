package com.navel.navalbattle;

import com.navel.navalbattle.database.DatabaseConnector;
import com.navel.navalbattle.interfaces.WindowsManipulations;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainMenuController extends Controller implements WindowsManipulations {
    private Stage stage;
    private Connection connection;

    /**
     * Завантажує наступну сцену з розташуванням кораблів та події для неї.
     * @param e Подія натискання на кнопку.
     * @throws IOException Помилка при читанні fxml файлу.
     */
    @FXML
    protected void onStartGameClick(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ships_positioning.fxml"));
        Scene scene = new Scene(loader.load());
        ShipsPositioningController controller = loader.getController();

        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();;

        stage.setScene(scene);
        stage.show();

        scene.setOnKeyPressed(event -> {
            event.consume();

            if (event.getCode() == KeyCode.ESCAPE) {
                processReturnToMain(stage);
            }

            if (event.getCode() == KeyCode.R) {
                controller.setRPressed();
            }
        });
    }

    /**
     * Оброблює натискання на кнопку виходу.
     */
    @FXML
    protected void onExitClick(ActionEvent e) {
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        processExit(stage);
    }

    @FXML
    protected void onStatClick(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("statistics.fxml"));
        Scene scene = new Scene(loader.load());

        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.show();

        scene.setOnKeyPressed(event -> {
            event.consume();

            if (event.getCode() == KeyCode.ESCAPE) {
                processReturnToMain(stage);
            }
        });
    }

    public void checkConnection() throws SQLException {
        DatabaseConnector connector = new DatabaseConnector();

        if (DatabaseConnector.getConnection() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Помилка підключення до бази даних", ButtonType.OK);
            alert.showAndWait();
            System.exit(1); //!!!!!!!!!!!!!!!!!
        }

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField username = new TextField();
        username.setPromptText("Username");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        grid.add(username, 0, 0);
        grid.add(password, 0, 1);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Login");
        dialog.setHeaderText("Enter your username and password");
        dialog.getDialogPane().setContent(grid);

        Button loginButton = new Button("Login");
        loginButton.setOnAction(event -> {
            dialog.setResult(ButtonType.OK);
        });

        Button registerButton = new Button("Register");
        registerButton.setOnAction(event -> {
            dialog.setResult(ButtonType.CANCEL);
        });

        if (dialog.showAndWait().get() == ButtonType.OK) {

        }
    }
}