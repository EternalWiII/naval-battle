package com.navel.navalbattle;

import com.navel.navalbattle.bot.EasyBot;
import com.navel.navalbattle.interfaces.GridCalculations;
import com.navel.navalbattle.interfaces.WindowsManipulations;
import com.navel.navalbattle.records.GridPosition;
import com.navel.navalbattle.records.ShipUsedArea;
import com.navel.navalbattle.ships.Ship;
import com.navel.navalbattle.records.spotStatus;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class GameController extends Controller implements GridCalculations, WindowsManipulations {
    private Ship[] playerShipArr;
    private AtomicInteger alivePlayerShips = new AtomicInteger(10);
    private Ship[] enemyShipArr;
    private AtomicInteger aliveEnemyShips = new AtomicInteger(10);
    @FXML
    private GridPane playerFieldPane;
    @FXML
    private GridPane enemyFieldPane;
    private boolean playerTurnActive = true;
    private boolean gameIsActive = true;
    private boolean isPlayersTurn;
    private EasyBot bot = new EasyBot();

    private List<List<spotStatus>> enemyIsAlreadyHit = new ArrayList<>();
    private List<List<spotStatus>> playerIsAlreadyHit = new ArrayList<>();

    /**
     * Вкионується при ініціалізації відповідного контроллера, створює, розташовує та відмальовує ворожі кораблі, запускає процес гри.
     */
    @FXML
    public void initialize() {
        for (int i = 0; i < 10; i++) {
            enemyIsAlreadyHit.add(new ArrayList<>());
            playerIsAlreadyHit.add(new ArrayList<>());
            for (int j = 0; j < 10; j++) {
                enemyIsAlreadyHit.get(i).add(spotStatus.UNKNOWN);
                playerIsAlreadyHit.get(i).add(spotStatus.UNKNOWN);
            }
        }

        enemyShipArr = new Ship[10];
        createShips(enemyShipArr, enemyFieldPane, squareSize, 600, 0);
        autoplaceShips(enemyShipArr, squareSize, fieldSpots);
        drawShips(enemyShipArr);

        Random rand = new Random();
//        isPlayersTurn = rand.nextBoolean();
        isPlayersTurn = true;


        enemyFieldPane.setOnMousePressed((MouseEvent event) -> {
            if (isPlayersTurn) {
                double mouseX = event.getX();
                double mouseY = event.getY();

                if (mouseX < 0) {
                    mouseX = 0;
                }
                if (mouseY < 0) {
                    mouseY = 0;
                }
                if (mouseX >= 400) {
                    mouseX = 399;
                }
                if (mouseY >= 400) {
                    mouseY = 399;
                }

                checkHit(new GridPosition((int) mouseX / squareSize, (int) mouseY / squareSize));
            }
        });
    }

    public void startGame() throws InterruptedException {
        Thread gameThread = new Thread(() -> {

            while (gameIsActive) {

                if (isPlayersTurn) {
                    playerTurnActive = true;

                    while (playerTurnActive) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    checkHit(bot.makeDicision(playerIsAlreadyHit));
                }
            }
        });

        gameThread.start();
    }

    private void endGame() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("main_menu.fxml"));
        Scene scene = new Scene(loader.load());

        Stage stage = (Stage) playerFieldPane.getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    }

    private void checkVictory() {
        if (alivePlayerShips.get() == 0) {
            gameIsActive = false;
            Platform.runLater(() -> {

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Game Over");
                alert.setHeaderText("You lost!");
                alert.setContentText("All your ships have been destroyed.");
                if (alert.showAndWait().get() == ButtonType.OK) {
                    try {
                        endGame();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else if (aliveEnemyShips.get() == 0) {
            gameIsActive = false;
            Platform.runLater(() -> {

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Game Over");
                alert.setHeaderText("You won!");
                alert.setContentText("All enemy ships have been destroyed.");
                if (alert.showAndWait().get() == ButtonType.OK) {
                    try {
                        endGame();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
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
     * @param coord Координати клітини.
     */
    public void checkHit(GridPosition coord) {
        Ship[] shipArr;
        List<List<spotStatus>> isAlreadyHit;
        AtomicInteger aliveShips;
        Pane fieldPane;


        if (isPlayersTurn) {
            shipArr = enemyShipArr;
            isAlreadyHit = enemyIsAlreadyHit;
            aliveShips = aliveEnemyShips;
            fieldPane = enemyFieldPane;
        }
        else {
            shipArr = playerShipArr;
            isAlreadyHit = playerIsAlreadyHit;
            aliveShips = alivePlayerShips;
            fieldPane = playerFieldPane;
        }

        if (isAlreadyHit.get(coord.x()).get(coord.y()) == spotStatus.EMPTY) {
            return;
        }
        else {
            playerTurnActive = false;

            for (int i = 0; i < 10 ; i++) {
                ShipUsedArea area = shipArr[i].getUsedArea();


                if (area.xMin() + 1 <= coord.x() && area.xMax() - 1 >= coord.x() && area.yMin() + 1 <= coord.y() && area.yMax() - 1 >= coord.y()) {

                    markHit(coord, fieldPane, isAlreadyHit);

                    if(shipArr[i].getHit()) {
                        aliveShips.decrementAndGet();
                        markAreaAroundDeadShip(shipArr[i], fieldPane, isAlreadyHit);
                    }

                    checkVictory();
                    return;
                }
            }

            markMiss(coord, fieldPane, isAlreadyHit);
            checkVictory();
            playerTurnActive = false;
            flipTurn();
        }
    }

    private void flipTurn() {
        isPlayersTurn = !isPlayersTurn;
    }

    /**
     * Відмічає клітини навколо знищеного корабля.
     * @param ship Знищений корабель.
     */
    private void markAreaAroundDeadShip(Ship ship, Pane fieldPane, List<List<spotStatus>> isAlreadyHit) {
        Platform.runLater(() -> {
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

                        if (isAlreadyHit.get(j).get(g) == spotStatus.UNKNOWN || isAlreadyHit.get(j).get(g) == spotStatus.HIT) {
                            isAlreadyHit.get(j).set(g, spotStatus.EMPTY);
                            fieldPane.getChildren().add(rec);
                        }
                    }
                }
            }
            ship.becomeDestroyed();
        });
    }

    /**
     * Відмічає клітину із промахом.
     * @param position Координати клітини.
     */
    private void markMiss(GridPosition position, Pane fieldPane, List<List<spotStatus>> isAlreadyHit) {
        Platform.runLater(() -> {
            Rectangle rec = new Rectangle();
            rec.setHeight(squareSize);
            rec.setWidth(squareSize);
            rec.setFill(Color.GOLD);

            fieldPane.getChildren().add(rec);
            isAlreadyHit.get(position.x()).set(position.y(), spotStatus.EMPTY);

            rec.setTranslateX(position.x() * squareSize);
            rec.setTranslateY(position.y() * squareSize);
        });
    }

    /**
     * Відмічає клітину із влученням.
     * @param position Координати клітини.
     */
    private void markHit(GridPosition position, Pane fieldPane, List<List<spotStatus>> isAlreadyHit) {
        Platform.runLater(() -> {
            Rectangle rec = new Rectangle();
            rec.setHeight(squareSize);
            rec.setWidth(squareSize);
            rec.setFill(Color.BLACK);

            fieldPane.getChildren().add(rec);
            isAlreadyHit.get(position.x()).set(position.y(), spotStatus.HIT);

            rec.setTranslateX(position.x() * squareSize);
            rec.setTranslateY(position.y() * squareSize);
        });
    }
}