package com.navel.navalbattle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class ShipsPositioningController {
    @FXML
    private Stage stage;
    @FXML
    private Pane fieldPane;
    @FXML
    private Pane hangarPane;
    @FXML
    private BorderPane borderPane;
    private Parent root;

    private int fieldSize = 400;
    private int hangarSize = 160;
    private int fieldSpots = 10;
    private int hangarSpots = 4;
    private int squareSize = fieldSize / fieldSpots;
    private Rectangle[][] fieldGrid;
    private Rectangle[][] hangarGrid;

    private int shipStartX = 440;
    private int shipStartY = 0;
    int[][] isAvailable;
    Ship[] shipArr;

    @FXML
    public void initialize() {
//        fieldGrid = new Rectangle[fieldSpots][fieldSpots];
//        for (int i = xStart; i < fieldSize; i += squareSize) {
//            for (int j = yStart; j < fieldSize; j += squareSize) {
//                Rectangle r = new Rectangle(i, j, squareSize, squareSize);
//                fieldGrid[i / squareSize][j / squareSize] = r;
//                r.setFill(Color.AQUA);
//                r.setStroke(Color.BLACK);
//                fieldPane.getChildren().add(r);
//            }
//        }

//        hangarGrid = new Rectangle[hangarSpots][fieldSpots];
//        for (int i = 0; i < hangarSize; i += squareSize) {
//            for (int j = 0; j < fieldSize; j += squareSize) {
//                Rectangle r = new Rectangle(i, j, squareSize, squareSize);
//                hangarGrid[i / squareSize][j / squareSize] = r;
//                r.setFill(Color.TRANSPARENT);
//                r.setStroke(Color.BLACK);
//                hangarPane.getChildren().add(r);
//            }
//        }

        Rectangle[] recArr = new Rectangle[10];
        shipArr = new Ship[10];

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
            recArr[i].setOnMousePressed(event -> pressed(event, shipArr[curShip]));
            recArr[i].setOnMouseDragged(event -> dragged(event, shipArr[curShip]));
            recArr[i].setOnMouseReleased(event -> released(event, shipArr[curShip]));
        }

        isAvailable = new int[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                isAvailable[i][j] = 0;
            }
        }
    }

    public void pressed(MouseEvent event, Ship s) {
        s.setHomeX(s.getX());
        s.setHomeY(s.getY());
    }

    public void dragged(MouseEvent event, Ship s) {
        s.setX(s.getX() + event.getX() - 20);
        s.setY(s.getY() + event.getY() - 20);
        s.draw();
    }

    public void released(MouseEvent event, Ship s) {
        int gridx = ((int)s.getX() + 20) / squareSize;
        if (gridx < 0) {
            gridx = 0;
        }
        if (gridx >= 15) {
            gridx = 14;
        }
        if (gridx == 10) {
            gridx = 11;
        }
        int gridy = ((int)s.getY() + 20) / squareSize;
        if (gridy < 0) {
            gridy = 0;
        }
        if (gridy >= 10) {
            gridy = 9;
        }

        boolean canPlace = true;
        int xMin, xMax, yMin, yMax;
        for (int sn = 0; sn < 10 && canPlace; sn++) {
            if (s.getShipID() != sn ) {
                xMin = (int)(shipArr[sn].getX()) / 40 - 1;
                xMax = xMin + shipArr[sn].getShipSize() + 1;

                yMin = (int)(shipArr[sn].getY()) / 40 - 1;
                yMax = yMin + 2;

                for (int i = gridx; i < gridx + s.getShipSize(); i++) {
                    if (i >= xMin && i <= xMax && gridy >= yMin && gridy <= yMax) {
                        canPlace = false;
                    }
                }
            }
        }

        if (canPlace) {
            s.setX(squareSize * gridx);
            s.setY(squareSize * gridy);
            s.draw();
        }
        else {
            s.setX(s.getHomeX());
            s.setY(s.getHomeY());
            s.draw();
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

}
