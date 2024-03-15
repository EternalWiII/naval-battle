package com.navel.navalbattle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Random;

public class GameController {
    Ship[] shipArr;
    Ship[] enemyShipArr;
    Rectangle[] enemyRecArr;

    private record gridPosition(int x, int y) {}

    @FXML
    Pane playerFieldPane;
    @FXML
    GridPane enemyFieldPane;

    private int squareSize = 40;
    private int fieldSpots = 10;

    public void setShipArr(Ship[] shipArr) {
        this.shipArr = shipArr.clone();
    }


    public boolean[][] isAlreadyHit = new boolean[10][10];

    @FXML
    public void initialize() {
        enemyShipArr = new Ship[10];
        enemyRecArr = new Rectangle[10];

        createShips();
        drawEnemyShips();
        Random rand = new Random();
        boolean isPlayersTurn = rand.nextBoolean();

        System.out.println(isAlreadyHit[0][0]);
        System.out.println(isAlreadyHit[5][5]);

        enemyFieldPane.setOnMousePressed((MouseEvent event) -> {
            double mouseX = event.getX();
            double mouseY = event.getY();
            System.out.println("Mouse pressed at X: " + mouseX + ", Y: " + mouseY);
            checkHit(mouseX, mouseY);
//            if (isPlayersTurn) {
//
//            }
        });




        boolean gameIsActive = true;
//        while (gameIsActive) {
//            if (isPlayersTurn) {
//
//            }
//            else {
//
//            }
//        }
    }

    public void checkHit(double x, double y) {
        int gridx, gridy;
        gridPosition position = getPosition(x, y);
        gridx = position.x;
        gridy = position.y;

        boolean isNotHit = true;
        int[] usedArea;
        for (int i = 0; i < 10 && isNotHit; i++) {
            usedArea = enemyShipArr[i].getUsedArea();

            if (usedArea[0] + 1 <= gridx && usedArea[1] - 1 >= gridx && usedArea[2] + 1 <= gridy && usedArea[3] - 1 >= gridy
                    && !isAlreadyHit[gridx][gridy]) {
                isNotHit = false;
                System.out.println("HIT");
                Rectangle rec = new Rectangle();
                rec.setHeight(squareSize);
                rec.setWidth(squareSize);
                rec.setFill(Color.BLACK);
                enemyFieldPane.getChildren().add(rec);
                isAlreadyHit[gridx][gridy] = true;

                rec.setTranslateX(gridx * squareSize);
                rec.setTranslateY(gridy * squareSize);

                if(enemyShipArr[i].getHit()) {
                    int[] deadArea = enemyShipArr[i].getUsedArea();

                    for (int j = deadArea[0]; j <= deadArea[1]; j++) {
                        for (int g = deadArea[2]; g <= deadArea[3]; g++) {
                            if ( j >= 0 && j <= 9 && g >= 0 && g <= 9) {
                                isAlreadyHit[j][g] = true;

                                Rectangle emptyRec = new Rectangle();
                                emptyRec.setHeight(squareSize);
                                emptyRec.setWidth(squareSize);
                                emptyRec.setFill(Color.WHITE);
                                enemyFieldPane.getChildren().add(emptyRec);
                                emptyRec.setTranslateX(j * squareSize);
                                emptyRec.setTranslateY(g * squareSize);
                            }
                        }
                    }

                    enemyShipArr[i].becomeDestroyed();
                }
            }
        }
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

    public void drawEnemyShips() {
        for (int j = 0; j < 10; j++) {
            int gridx, gridy;
            boolean shipNotPlaced, randVertical;
            Random rand = new Random();

            for (int i = 0; i < 10; i++) {
                shipNotPlaced = true;
                while (shipNotPlaced) {
                    gridx = rand.nextInt(fieldSpots - 1);
                    gridy = rand.nextInt(fieldSpots - 1);
                    randVertical = rand.nextBoolean();

                    if (enemyShipArr[i].isVertical() != randVertical) {
                        enemyShipArr[i].flipIsVertical();
                    }

                    if (enemyShipArr[i].isVertical()) {
                        enemyShipArr[i].setX(gridx * squareSize - enemyShipArr[i].getOffset());
                        enemyShipArr[i].setY(gridy * squareSize + enemyShipArr[i].getOffset());
                    }
                    else {
                        enemyShipArr[i].setX(gridx * squareSize);
                        enemyShipArr[i].setY(gridy * squareSize);
                    }

                    if(canPlace(enemyShipArr[i], getPosition(enemyShipArr[i]), 10)) {
                        gridPosition shipPos = getPosition(enemyShipArr[i]);
                        if (enemyShipArr[i].isVertical()) {
                            enemyShipArr[i].setX(shipPos.x * squareSize - enemyShipArr[i].getOffset());
                            enemyShipArr[i].setY(shipPos.y * squareSize + enemyShipArr[i].getOffset());
                        }
                        else {
                            enemyShipArr[i].setX(shipPos.x * squareSize);
                            enemyShipArr[i].setY(shipPos.y * squareSize);
                        }

                        enemyShipArr[i].draw();
                        shipNotPlaced = false;
                    }
                }
            }
        }

    }

    private boolean canPlace(Ship s, gridPosition position, int shipAmount) {
        int[] usedArea = new int[4];

        for (int sn = 0; sn < shipAmount; sn++) {
            if (s.getShipID() != sn ) {
                usedArea = enemyShipArr[sn].getUsedArea();

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

    private gridPosition getPosition(double x, double y) {
        int gridx;
        int gridy;

        gridx = ((int) x) / squareSize;
        gridy = ((int) y) / squareSize;

        gridPosition position = new gridPosition(gridx, gridy);
        return position;
    }

    public void createShips () {
        for (int i = 0; i < 10; i++) {
            enemyRecArr[i] = new Rectangle();
            enemyRecArr[i].setFill(Color.RED);
            enemyRecArr[i].setStroke(Color.BLACK);

            int curSize;
            switch (i) {
                case 0 -> curSize = 4;
                case 1, 2 -> curSize = 3;
                case 3, 4, 5 -> curSize = 2;
                default -> curSize = 1;
            }

            enemyShipArr[i] = new Ship(i, squareSize, enemyRecArr[i], 800, 800, curSize);

            enemyFieldPane.getChildren().add(enemyRecArr[i]);
        }
    }

//    private void addGridEvent() {
//        enemyFieldPane.getChildren().forEach(item -> {
//            item.setOnMouseClicked(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent event) {
//                    System.out.println(event.getSceneX());
//                    System.out.println(event.getSceneY());
//                }
//            });
//
//        });
//    }
}
