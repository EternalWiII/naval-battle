package com.navel.navalbattle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;

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
    boolean rPressed = false;
    boolean isDragged = false;
//    double mouseX, mouseY;
    @FXML
    public void initialize() {
//        fieldPane.setOnMouseMoved(event -> {
//            System.out.println("FASfafsfasf");
//            mouseX = event.getX();
//            mouseY = event.getY();
//        });

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
            if (i==0) {
                shipArr[i].setVertical(false);
            }
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

    private Ship draggedShip;
    public void pressed(MouseEvent event, Ship s) {
        s.setHomeX(s.getX());
        s.setHomeY(s.getY());
        draggedShip = s;
    }

    public void dragged(MouseEvent event, Ship s) {
        System.out.println("DRAGGED");
        System.out.println(s.getX());
        System.out.println(s.getY());
        isDragged = true;
        if (s.isVertical()) {
            s.setX(s.getX() + event.getX() - 20);
            s.setY(s.getY() + event.getY() - 20);
        }
        else {
            s.setX(s.getX() + event.getX() - 20);
            s.setY(s.getY() + event.getY() - 20);
        }
        s.draw();
    }

    public void released(MouseEvent event, Ship s) {
        System.out.println(s.getX());
        System.out.println(s.getY());
        isDragged = false;
        int gridx, gridy;
        if (s.isVertical()) {
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
            if (s.getShipSize() % 2 == 0) {
                gridy = ((int)s.getY() + 40) / squareSize;
            }
            else {
                gridy = ((int)s.getY() + 20) / squareSize;
            }
            if (gridy < 0) {
                gridy = 0;
            }
            if (gridy >= 10) {
                gridy = 9;
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
            gridy = ((int)s.getY() + 20) / squareSize;
            if (gridy < 0) {
                gridy = 0;
            }
            if (gridy >= 10) {
                gridy = 9;
            }
        }
        System.out.println("GRIDX/GRIDY");
        System.out.println(gridx);
        System.out.println(gridy);

        boolean canPlace = true;
        int[] usedArea = new int[4];
        for (int sn = 0; sn < 10 && canPlace; sn++) {
            if (s.getShipID() != sn ) {
                usedArea = shipArr[sn].getUsedArea();
                for (int i = gridx; i < gridx + s.getShipSize(); i++) { //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    if (i >= usedArea[0] && i <= usedArea[1] && gridy >= usedArea[2] && gridy <= usedArea[3]) {
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

}
