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
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainMenuController extends Controller implements WindowsManipulations {
    @FXML
    private Pane scenePane;
    @FXML
    private VBox vBox;
    private Stage stage;
    private Connection connection;

    @FXML
    private void initialize() {
        vBox.prefWidthProperty().bind(scenePane.widthProperty().subtract(200));
        vBox.prefHeightProperty().bind(scenePane.heightProperty().subtract(200));

        vBox.spacingProperty().bind(scenePane.heightProperty().divide(5));
    }

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

        stage.setMinWidth(stage.getWidth() - scene.getWidth() + 640);
        stage.setMinHeight(stage.getHeight() - scene.getHeight() + 540);

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
        if (DatabaseConnector.getConnection() != null) {
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
       else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Connect online to see statistics");

            alert.showAndWait();
        }
    }
}