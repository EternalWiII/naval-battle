package com.navel.navalbattle;

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
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.util.Random;

public class ShipsPositioningController {
    @FXML
    private Stage stage;
    @FXML
    private Pane fieldPane;
    @FXML
    private Pane hangarPane;
    @FXML
    private BorderPane borderPane;
    private int fieldSize = 400;
    private int fieldSpots = 10;
    private int squareSize = fieldSize / fieldSpots;
    private int upOffset = 70;

    private int shipStartX = 440;
    private int shipStartY = 0;
    int[][] isAvailable;
    Ship[] shipArr;
    Rectangle[] recArr;
    boolean isDragged = false;
    private Ship draggedShip;
    private record gridPosition(int x, int y) {}

    @FXML
    public void initialize() {
        recArr = new Rectangle[10];
        shipArr = new Ship[10];
        createShips();
    }

    public void createShips () {
        for (int i = 0; i < 10; i++) {
            recArr[i] = new Rectangle();
            recArr[i].setFill(Color.RED);
            recArr[i].setStroke(Color.BLACK);

            int curSize;
            switch (i) {
                case 0 -> curSize = 4;
                case 1, 2 -> curSize = 3;
                case 3, 4, 5 -> curSize = 2;
                default -> curSize = 1;
            }

            shipArr[i] = new Ship(i, squareSize, recArr[i], shipStartX, shipStartY, curSize);

            fieldPane.getChildren().add(recArr[i]);

            shipArr[i].draw();

            shipStartY += 40;

            int curShip = i;
            recArr[i].setOnMousePressed(event -> recPressed(event, shipArr[curShip]));
            recArr[i].setOnMouseDragged(event -> recDragged(event, shipArr[curShip]));
            recArr[i].setOnMouseReleased(event -> recReleased(event, shipArr[curShip]));
        }
    }

    public void recPressed(MouseEvent event, Ship s) {
        s.setHomeX(s.getX());
        s.setHomeY(s.getY());
        s.setHomeIsVertical(s.isVertical());
        draggedShip = s;
    }

    public void recDragged(MouseEvent event, Ship s) {
        isDragged = true;

        s.getRec().toFront();
        s.getRec().setFill(Color.ORANGE);

        s.setX((event.getSceneX()) - (double)(squareSize * s.getShipSize()) / 2 - 20);
        s.setY((event.getSceneY()) - (double)(squareSize) / 2 - 70);

        s.draw();
    }

    public void recReleased(MouseEvent event, Ship s) {
        isDragged = false;
        s.getRec().setFill(Color.RED);

        gridPosition position = getPosition(s);

        if (canPlace(s, position, 10)) {
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

        int[] area = s.getUsedArea();
        for ( int i = 0; i<4 ; i++) {
            System.out.println(area[i]);
        }
    }

    private boolean canPlace(Ship s, gridPosition position, int shipAmount) {
        int[] usedArea = new int[4];

        for (int sn = 0; sn < shipAmount; sn++) {
            if (s.getShipID() != sn ) {
                usedArea = shipArr[sn].getUsedArea();

                if (s.isVertical()) {
                    for (int i = position.y(); i < position.y() + s.getShipSize(); i++) {
                        if (position.x() >= usedArea[0] && position.x() <= usedArea[1] && i >= usedArea[2] && i <= usedArea[3]) {
                            return false;
                        }
                    }
                }
                else {
                    for (int i = position.x(); i < position.x() + s.getShipSize(); i++) {
                        if (i >= usedArea[0] && i <= usedArea[1] && position.y() >= usedArea[2] && position.y() <= usedArea[3]) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    private gridPosition getPosition(Ship s) {
        int gridx;
        int gridy;

        if (s.isVertical()) {
            gridx = ((int) s.getX() + (int) s.getOffset() + 20) / squareSize;

            if (gridx < 0) {
                gridx = 0;
            }
            if (gridx >= 15) {
                gridx = 14;
            }
            if (gridx == 10) {
                gridx = 11;
            }

            gridy = ((int) s.getY() - (int) s.getOffset() + 20) / squareSize;

            if (gridy < 0) {
                gridy = 0;
            }
            if (gridy >= 10) {
                gridy = 9;
            }
            if ( gridy + s.getShipSize() >= 10) {
                gridy = 10 - s.getShipSize();
            }
        }
        else {
            gridx = ((int)s.getX() + 20) / squareSize;

            if (gridx < 0) {
                gridx = 0;
            }
            if (gridx >= 15) {
                gridx = 14;
            }
            if (gridx == 10) {
                gridx = 11;
            }
            if ( gridx + s.getShipSize() - 1 >= 10 && gridx < 10) {
                gridx = 10 - s.getShipSize();
            }
            if (gridx + s.getShipSize() >= 15 && gridx >=11) {
                gridx = 15 - s.getShipSize();
            }

            gridy = ((int) s.getY() + 20) / squareSize;

            if (gridy < 0) {
                gridy = 0;
            }
            if (gridy >= 10) {
                gridy = 9;
            }
        }

        gridPosition position = new gridPosition(gridx, gridy);
        return position;
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
            controller.setShipArr(shipArr);
            controller.drawPlayerShips();

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

        int gridx, gridy;
        boolean shipNotPlaced, randVertical;
        Random rand = new Random();

        for (int i = 0; i < 10; i++) {
            shipNotPlaced = true;
            while (shipNotPlaced) {
                gridx = rand.nextInt(fieldSpots - 1);
                gridy = rand.nextInt(fieldSpots - 1);
                randVertical = rand.nextBoolean();

                if (shipArr[i].isVertical() != randVertical) {
                    shipArr[i].flipIsVertical();
                }

                if (shipArr[i].isVertical()) {
                    shipArr[i].setX(gridx * squareSize - shipArr[i].getOffset());
                    shipArr[i].setY(gridy * squareSize + shipArr[i].getOffset());
                }
                else {
                    shipArr[i].setX(gridx * squareSize);
                    shipArr[i].setY(gridy * squareSize);
                }

                if(canPlace(shipArr[i], getPosition(shipArr[i]), 10)) {
                    gridPosition shipPos = getPosition(shipArr[i]);
                    if (shipArr[i].isVertical()) {
                        shipArr[i].setX(shipPos.x * squareSize - shipArr[i].getOffset());
                        shipArr[i].setY(shipPos.y * squareSize + shipArr[i].getOffset());
                    }
                    else {
                        shipArr[i].setX(shipPos.x * squareSize);
                        shipArr[i].setY(shipPos.y * squareSize);
                    }
                    System.out.print("SHIP " + i + ": ");
                    System.out.print(shipArr[i].getX());
                    System.out.print(shipArr[i].getY());
                    System.out.println();
                    shipArr[i].draw();
                    shipNotPlaced = false;
                }
            }
        }
    }
}
