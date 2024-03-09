package com.navel.navalbattle;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class GameController {
    Ship[] shipArr;

    @FXML
    Pane playerFieldPane;

    public void setShipArr(Ship[] shipArr) {
        this.shipArr = shipArr.clone();
    }
    public void drawPlayerShips() {
        for (int i = 0; i < 10; i++) {
            Rectangle rec = shipArr[i].getRec();
            playerFieldPane.getChildren().add(rec);
            rec.setOnMousePressed(null);
            rec.setOnMouseDragged(null);
            rec.setOnMouseReleased(null);
            shipArr[i].draw();
        }
    }

    @FXML
    public void initialize() {

    }
}
