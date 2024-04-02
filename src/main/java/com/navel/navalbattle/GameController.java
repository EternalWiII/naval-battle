package com.navel.navalbattle;

import com.navel.navalbattle.bot.EasyBot;
import com.navel.navalbattle.interfaces.GridCalculations;
import com.navel.navalbattle.records.GridPosition;
import com.navel.navalbattle.records.ShipUsedArea;
import com.navel.navalbattle.ships.Ship;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Random;

public class GameController extends Controller implements GridCalculations {
    private Ship[] playerShipArr;
    private int alivePlayerShips = 10;
    private Ship[] enemyShipArr;
    private int aliveEnemyShips = 10;
    @FXML
    private GridPane playerFieldPane;
    @FXML
    private GridPane enemyFieldPane;
    private boolean turnHasEnded = false;
    private boolean playerVictory = false;
    private boolean isPlayersTurn;
    private EasyBot bot = new EasyBot();

    private boolean[][] enemyIsAlreadyHit = new boolean[10][10];
    private boolean[][] playerIsAlreadyHit = new boolean[10][10];

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
//        isPlayersTurn = rand.nextBoolean();
        isPlayersTurn = true;


        enemyFieldPane.setOnMousePressed((MouseEvent event) -> {
            double mouseX = event.getX();
            double mouseY = event.getY();

            checkHit(new GridPosition((int) mouseX / squareSize, (int) mouseY / squareSize));
        });
    }

    private void checkVictory() {
        if (alivePlayerShips == 0) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Game Over");
                alert.setHeaderText("You lost!");
                alert.setContentText("All your ships have been destroyed.");
                alert.showAndWait();
            });
        } else if (aliveEnemyShips == 0) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Game Over");
                alert.setHeaderText("You won!");
                alert.setContentText("All enemy ships have been destroyed.");
                alert.showAndWait();
            });
        }
    }

    /**
     * Використовується для перенесення кораблів гравця із минулої сцени. Виконується їх копіювання в новий масив та видалення подій розташування.
     * @param shipArr Масив кораблів, який буде скопійовано.
     */
    public void setPlayerShipArr(Ship[] shipArr) {
        this.playerShipArr = shipArr;

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
    public void checkHit(GridPosition coord) {
        if (isPlayersTurn) {
            for (int i = 0; i < 10 ; i++) {
                ShipUsedArea area = enemyShipArr[i].getUsedArea();

                if (!enemyIsAlreadyHit[coord.x()][coord.y()]) {
                    if (area.xMin() + 1 <= coord.x() && area.xMax() - 1 >= coord.x() && area.yMin() + 1 <= coord.y() && area.yMax() - 1 >= coord.y()) {

                        markHit(coord);

                        if(enemyShipArr[i].getHit()) {
                            aliveEnemyShips--;
                            markAreaAroundDeadShip(enemyShipArr[i]);
                        }

                        checkVictory();
                        return;
                    }
                }
                return;
            }

            if (!enemyIsAlreadyHit[coord.x()][coord.y()]) {
                markMiss(coord);
            }

            isPlayersTurn = false;
            checkHit(bot.makeDicision(playerIsAlreadyHit));
            checkVictory();
        } else {
            for (int i = 0; i < 10 ; i++) {
                ShipUsedArea area = playerShipArr[i].getUsedArea();

                if (area.xMin() + 1 <= coord.x() && area.xMax() - 1 >= coord.x() && area.yMin() + 1 <= coord.y() && area.yMax() - 1 >= coord.y()
                        && !playerIsAlreadyHit[coord.x()][coord.y()]) {

                    markHit(coord);

                    if(playerShipArr[i].getHit()) {
                        alivePlayerShips--;
                        markAreaAroundDeadShip(playerShipArr[i]);
                    }

                    checkHit(bot.makeDicision(playerIsAlreadyHit));
                    checkVictory();
                    return;
                }
            }

            if (!playerIsAlreadyHit[coord.x()][coord.y()]) {
                markMiss(coord);
            }
            isPlayersTurn = true;
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
                    Rectangle rec = new Rectangle();
                    rec.setHeight(squareSize);
                    rec.setWidth(squareSize);
                    rec.setFill(Color.WHITE);
                    rec.setTranslateX(j * squareSize);
                    rec.setTranslateY(g * squareSize);
                    
                    if (isPlayersTurn) {
                        if (!enemyIsAlreadyHit[j][g]) {
                            enemyIsAlreadyHit[j][g] = true;
                            enemyFieldPane.getChildren().add(rec);
                        }
                    } else {
                        if (!playerIsAlreadyHit[j][g]) {
                            playerIsAlreadyHit[j][g] = true;
                            playerFieldPane.getChildren().add(rec);
                        }
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
        Rectangle rec = new Rectangle();
        rec.setHeight(squareSize);
        rec.setWidth(squareSize);
        rec.setFill(Color.GOLD);
        
        if (isPlayersTurn) {
            enemyFieldPane.getChildren().add(rec);
            enemyIsAlreadyHit[position.x()][position.y()] = true;
        } else {
            playerFieldPane.getChildren().add(rec);
            playerIsAlreadyHit[position.x()][position.y()] = true;
        }

        rec.setTranslateX(position.x() * squareSize);
        rec.setTranslateY(position.y() * squareSize);
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
        
        if (isPlayersTurn) {
            enemyFieldPane.getChildren().add(rec);
            enemyIsAlreadyHit[position.x()][position.y()] = true;
        } else {
            playerFieldPane.getChildren().add(rec);
            playerIsAlreadyHit[position.x()][position.y()] = true;
        }

        rec.setTranslateX(position.x() * squareSize);
        rec.setTranslateY(position.y() * squareSize);
    }
}