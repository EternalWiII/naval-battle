package com.navel.navalbattle;

import com.navel.navalbattle.interfaces.GridCalculations;
import com.navel.navalbattle.records.GridPosition;
import com.navel.navalbattle.records.ShipUsedArea;
import com.navel.navalbattle.ships.Ship;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Random;

public class GameController extends Controller implements GridCalculations {
    Ship[] playerShipArr;
    Ship[] enemyShipArr;
    @FXML
    GridPane playerFieldPane;
    @FXML
    GridPane enemyFieldPane;

    public boolean[][] isAlreadyHit = new boolean[10][10];

    /**
     * Вкионується при ініціалізації відповідного контроллера, створює, розташовує та відмальовує ворожі кораблі, запускає процес гри.
     */
    @FXML
    public void initialize() {
        enemyShipArr = new Ship[10];
        createShips(enemyShipArr, enemyFieldPane, squareSize, 600, 0);
        autoplaceShips(enemyShipArr, squareSize, fieldSpots);
        drawShips(enemyShipArr);

        Random rand = new Random();
        boolean isPlayersTurn = rand.nextBoolean();

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

    /**
     * Використовується для перенесення кораблів гравця із минулої сцени. Виконується їх копіювання в новий масив та видалення подій розташування.
     * @param shipArr Масив кораблів, який буде скопійовано.
     */
    public void setPlayerShipArr(Ship[] shipArr) {
        this.playerShipArr = shipArr.clone();

        for (Ship ship : shipArr) {
            playerFieldPane.getChildren().add(ship.getRec());
            ship.getRec().setOnMousePressed(null);
            ship.getRec().setOnMouseDragged(null);
            ship.getRec().setOnMouseReleased(null);
        }

        drawShips(playerShipArr);
    }

    /**
     * Перевіряє чи знаходився ворожий корабель на заданих координатах. Відповідно помічає промах, влучення та знищення корабля.
     * @param x Координата x відносно ворожої панелі.
     * @param y Координата y відносно ворожої панелі.
     */
    public void checkHit(double x, double y) {
        GridPosition position = getPosition(x, y, squareSize);

        for (int i = 0; i < 10 ; i++) {
            ShipUsedArea area = enemyShipArr[i].getUsedArea();

            if (area.xMin() + 1 <= position.x() && area.xMax() - 1 >= position.x() && area.yMin() + 1 <= position.y() && area.yMax() - 1 >= position.y()
                    && !isAlreadyHit[position.x()][position.y()]) {

                markHit(position);

                if(enemyShipArr[i].getHit()) {
                    markAreaAroundDeadShip(enemyShipArr[i]);
                }
                return;
            }
        }

        if (!isAlreadyHit[position.x()][position.y()]) {
            markMiss(position);
        }
    }

    /**
     * Відмічає клітини навколо знищеного корабля.
     * @param ship Знищений корабель.
     */
    private void markAreaAroundDeadShip(Ship ship) {
        ShipUsedArea deadArea = ship.getUsedArea();

        for (int j = deadArea.xMin(); j <= deadArea.xMax(); j++) {
            for (int g = deadArea.yMin(); g <= deadArea.yMax(); g++) {

                if ( j >= 0 && j <= 9 && g >= 0 && g <= 9) {

                    if (!isAlreadyHit[j][g]) {
                        Rectangle emptyRec = new Rectangle();
                        emptyRec.setHeight(squareSize);
                        emptyRec.setWidth(squareSize);
                        emptyRec.setFill(Color.WHITE);
                        enemyFieldPane.getChildren().add(emptyRec);
                        emptyRec.setTranslateX(j * squareSize);
                        emptyRec.setTranslateY(g * squareSize);
                        isAlreadyHit[j][g] = true;
                    }
                }
            }
        }
        ship.becomeDestroyed();
    }

    /**
     * Відмічає клітину із промахом.
     * @param position Координати клітини.
     */
    private void markMiss(GridPosition position) {
        Rectangle emptyRec = new Rectangle();
        emptyRec.setHeight(squareSize);
        emptyRec.setWidth(squareSize);
        emptyRec.setFill(Color.GOLD);
        enemyFieldPane.getChildren().add(emptyRec);

        emptyRec.setTranslateX(position.x() * squareSize);
        emptyRec.setTranslateY(position.y() * squareSize);

        isAlreadyHit[position.x()][position.y()] = true;
    }

    /**
     * Відмічає клітину із влученням.
     * @param position Координати клітини.
     */
    private void markHit(GridPosition position) {
        Rectangle rec = new Rectangle();
        rec.setHeight(squareSize);
        rec.setWidth(squareSize);
        rec.setFill(Color.BLACK);
        enemyFieldPane.getChildren().add(rec);

        rec.setTranslateX(position.x() * squareSize);
        rec.setTranslateY(position.y() * squareSize);

        isAlreadyHit[position.x()][position.y()] = true;
    }
}