package com.navel.navalbattle;
import javafx.scene.input.Dragboard;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainMenuController {

    @FXML
    private Button exitBtn;
    @FXML
    private AnchorPane scenePane;

    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    protected void onStartGameClick(ActionEvent e) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ships_positioning.fxml")));
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