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

    @FXML
    public void initialize() {
        fieldGrid = new Rectangle[fieldSpots][fieldSpots];
        for (int i = 0; i < fieldSize; i += squareSize) {
            for (int j = 0; j < fieldSize; j += squareSize) {
                Rectangle r = new Rectangle(i, j, squareSize, squareSize);
                fieldGrid[i / squareSize][j / squareSize] = r;
                r.setFill(Color.TRANSPARENT);
                r.setStroke(Color.BLACK);
                fieldPane.getChildren().add(r);
            }
        }

        hangarGrid = new Rectangle[hangarSpots][fieldSpots];
        for (int i = 0; i < hangarSize; i += squareSize) {
            for (int j = 0; j < fieldSize; j += squareSize) {
                Rectangle r = new Rectangle(i, j, squareSize, squareSize);
                hangarGrid[i / squareSize][j / squareSize] = r;
                r.setFill(Color.TRANSPARENT);
                r.setStroke(Color.BLACK);
                hangarPane.getChildren().add(r);
            }
        }

        Rectangle r = new Rectangle();
        r.setFill(Color.RED);
        r.setStroke(Color.BLACK);
        int x = 0;
        int y = 0;
        Ship s = new Ship(squareSize, r, x, y);
        borderPane.getChildren().add(r);

        s.draw();

        r.setOnMousePressed(event -> pressed(event, s));
        r.setOnMouseDragged(event -> dragged(event, s));
        r.setOnMouseReleased(event -> released(event, s));

    }

    public void pressed(MouseEvent event, Ship s) {
    }

    public void dragged(MouseEvent event, Ship s) {
        s.setX(s.getX() + event.getX() - 20);
        s.setY(s.getY() + event.getY() - 20);
        System.out.println(s.getX());
        System.out.println(s.getY());
        s.draw();
    }

    public void released(MouseEvent event, Ship s) {
        int gridx = ((int)s.getX() + 20) / squareSize;
        int gridy = ((int)s.getY() + 20) / squareSize;
        s.setX( squareSize * gridx);
        s.setY( squareSize * gridy);
        System.out.println(squareSize * gridx);
        System.out.println(squareSize * gridy);
        s.draw();
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
