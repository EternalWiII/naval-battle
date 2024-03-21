package com.navel.navalbattle;

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

public class Main extends Application implements WindowsManipulations {
    /**
     * Отримує поточний stage та завантажує сцену головного меню.
     * @param stage Поточний stage.
     * @throws IOException Помилка при читанні fxml файлу.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("main_menu.fxml"));
        Scene scene = new Scene(loader.load());

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
    }

    /**
     * Запуск програми.
     * @param args Додаткові параметри.
     */
    public static void main(String[] args) {
        launch();
    }
}