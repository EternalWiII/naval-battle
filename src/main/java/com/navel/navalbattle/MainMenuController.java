package com.navel.navalbattle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController extends Controller {
    @FXML
    private AnchorPane scenePane;
    private Stage stage;
    private Scene scene;

    @FXML
    protected void onStartGameClick(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ships_positioning.fxml"));
        System.out.println(loader.getClass());
        Parent root = loader.load();
        ShipsPositioningController controller = loader.getController();

        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);

        stage.setScene(scene);
        stage.show();

        scene.setOnKeyPressed(event -> {
            event.consume();
            if (event.getCode() == KeyCode.ESCAPE) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                if (alert.showAndWait().get() == ButtonType.OK) {
                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main_menu.fxml"));
                    try {
                        Scene scene = new Scene(fxmlLoader.load());
                        stage.setScene(scene);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
            if (event.getCode() == KeyCode.R) {
                controller.setRPressed();
            }
        });
    }
    @FXML
    protected void onExitClick(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        if (alert.showAndWait().get() == ButtonType.OK) {
            stage = (Stage) scenePane.getScene().getWindow();
            stage.close();
        }
    }
}