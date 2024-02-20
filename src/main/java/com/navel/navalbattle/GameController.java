package com.navel.navalbattle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class GameController {
    @FXML
    private Stage stage;
    @FXML
    private AnchorPane scenePane;
    private Parent root;

    protected void onEscBtn() {
        scenePane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(final KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ESCAPE) {
                    stage = (Stage) scenePane.getScene().getWindow();
                    stage.close();
                }
            }
        });
    }

}
