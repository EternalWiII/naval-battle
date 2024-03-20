package com.navel.navalbattle;

import com.navel.navalbattle.ships.*;
import com.navel.navalbattle.records.GridPosition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;

public class ShipsPositioningController extends Controller implements GridCalculations {
    @FXML
    private Stage stage;
    @FXML
    private Pane fieldPane;
    @FXML
    private BorderPane borderPane;
    private int shipStartX = fieldSize + squareSize;
    private int shipStartY = 0;
    Ship[] shipArr;
    boolean isDragged = false;
    private Ship draggedShip;

    public ShipsPositioningController() {
        super();
    }

    @FXML
    public void initialize() {
        shipArr = new Ship[10];

        createShips(shipArr, fieldPane, squareSize, shipStartX, shipStartY);
        addPositioningEvents(shipArr);
    }

    private void addPositioningEvents(Ship[] shipArr) {
        for (Ship ship : shipArr) {
            ship.getRec().setOnMousePressed(event -> recPressed(event, ship));
            ship.getRec().setOnMouseDragged(event -> recDragged(event, ship));
            ship.getRec().setOnMouseReleased(event -> recReleased(event, ship));
        }
    }

    private void recPressed(MouseEvent event, Ship s) {
        s.setHomeX(s.getX());
        s.setHomeY(s.getY());
        s.setHomeIsVertical(s.isVertical());
        draggedShip = s;
    }

    private void recDragged(MouseEvent event, Ship s) {
        isDragged = true;

        s.getRec().toFront();
        s.getRec().setFill(Color.ORANGE);

        s.setX((event.getSceneX()) - (double)(squareSize * s.getShipSize()) / 2 - 20);
        s.setY((event.getSceneY()) - (double)(squareSize) / 2 - 70);

        s.draw();
    }

    private void recReleased(MouseEvent event, Ship s) {
        isDragged = false;
        s.getRec().setFill(Color.RED);

        GridPosition position = getPosition(s, squareSize);

        if (canPlace(s, shipArr, position)) {
            if (s.isVertical()) {
                s.setX(squareSize * position.x() - s.getOffset());
                s.setY(squareSize * position.y() + s.getOffset());
            }
            else {
                s.setX(squareSize * position.x());
                s.setY(squareSize * position.y());
            }
            s.draw();
        }
        else {
            s.setX(s.getHomeX());
            s.setY(s.getHomeY());
            if (s.isVertical() != s.isHomeIsVertical()) {
                s.flipIsVertical();
            }
            s.draw();
        }
    }

    @FXML
    protected void onStartClick(ActionEvent e) throws IOException {
        boolean readyToGo = true;
        for (int i = 0; i < 10; i++) {
            if (shipArr[i].isVertical()) {
                if (shipArr[i].getX() + shipArr[i].getOffset() >= 400) {
                    readyToGo = false;
                }
            }
            else {
                if (shipArr[i].getX() >= 400) {
                    readyToGo = false;
                }
            }
        }
        if (readyToGo) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));
            Parent root = loader.load();
            GameController controller = loader.getController();
            controller.setPlayerShipArr(shipArr);


            stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.show();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.showAndWait();
        }
    }

    public void setRPressed() {
        if(isDragged) {
            draggedShip.flipIsVertical();
        }
    }

    protected void onEscBtn() {
        borderPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(final KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ESCAPE) {
                    stage = (Stage) borderPane.getScene().getWindow();
                    stage.close();
                }
            }
        });
    }

    @FXML
    protected void onClearFieldClick(ActionEvent e) {
        int newY = 0;
        for (int i = 0; i < 10; i++) {
            shipArr[i].setX(fieldSize + squareSize);
            shipArr[i].setY(newY);
            if (shipArr[i].isVertical()) {
                shipArr[i].flipIsVertical();
            }
            newY += squareSize;
            shipArr[i].draw();
        }
    }

    @FXML
    protected void onAutoplaceClick(ActionEvent e) {
        onClearFieldClick(e);

        autoplaceShips(shipArr, squareSize, fieldSpots);
    }
}
