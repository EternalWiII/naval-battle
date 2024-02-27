package com.navel.navalbattle;

import javafx.scene.paint.Color;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Ship {
    private int shipSize;
    private int squareSize;
    private Rectangle rec;
    private double x;
    private double y;
    private Color color = Color.BLACK;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Ship(int squareSize, Rectangle rec, int x, int y, int shipSize) {
        this.squareSize = squareSize;
        this.rec = rec;
        this.x = x;
        this.y = y;
        this.shipSize = shipSize;
    }

    public void draw() {
        rec.setWidth(squareSize * shipSize);
        rec.setHeight(squareSize);
        rec.setTranslateX(x);
        rec.setTranslateY(y);
    }
}