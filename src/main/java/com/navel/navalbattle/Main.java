package com.navel.navalbattle;

import com.navel.navalbattle.database.DatabaseConnector;
import com.navel.navalbattle.interfaces.WindowsManipulations;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class Main extends Application implements WindowsManipulations {
    /**
     * Отримує поточний stage та завантажує сцену головного меню.
     * @param stage Поточний stage.
     * @throws IOException Помилка при читанні fxml файлу.
     */
    @Override
    public void start(Stage stage) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("authorization.fxml"));
        Scene scene = new Scene(loader.load());
        AuthorizationController controller = loader.getController();

        stage.setTitle("Naval battle");
        stage.setResizable(false);

        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(event -> {
            event.consume();
            processExit(stage);
            }
        );

        scene.setOnKeyPressed(event -> {
            event.consume();

            if (event.getCode() == KeyCode.ESCAPE) {
                processExit(stage);
            }
        });

        controller.makeConnection();
    }

    /**
     * Запуск програми.
     * @param args Додаткові параметри.
     */
    public static void main(String[] args) {
        launch();
    }
}