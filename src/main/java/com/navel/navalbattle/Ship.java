package com.navel.navalbattle;

import javafx.scene.paint.Color;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Ship {
    private int shipID;
    private int shipSize;
    private int squareSize;
    private Rectangle rec;
    private double x;
    private double y;
    private Color color = Color.BLACK;
    private double homeX;
    private double homeY;
    private boolean homeIsVertical;
    private boolean isVertical = false;
    private boolean firstDrag = true;
    private double offset = 0;

    public boolean isVertical() {
        return isVertical;
    }

    public boolean isHomeIsVertical() {
        return homeIsVertical;
    }

    public void setHomeIsVertical(boolean homeIsVertical) {
        this.homeIsVertical = homeIsVertical;
    }

    public double getOffset() {
        return offset;
    }

    public Ship(int shipID, int squareSize, Rectangle rec, int x, int y, int shipSize) {
        this.squareSize = squareSize;
        this.rec = rec;
        this.x = x;
        this.y = y;
        this.shipSize = shipSize;
        this.shipID = shipID;
        rec.setWidth(squareSize * shipSize);
        rec.setHeight(squareSize);
        switch (shipSize) {
            case 2 -> offset = squareSize / 2;
            case 3 -> offset = squareSize;
            case 4 -> offset = (squareSize * 3) / 2;
        }
    }

    public void setVertical(boolean vertical) {
        isVertical = vertical;
    }

    public void draw() {
        rec.setWidth(squareSize * shipSize);
        rec.setHeight(squareSize);
        rec.setTranslateX(x);
        rec.setTranslateY(y);
    }
    public void flipIsVertical() {
        if (isVertical) {
            isVertical = false;
//            rec.setHeight(squareSize);
//            rec.setWidth(squareSize * shipSize);
            rec.setRotate(0);
            draw();
        }
        else {
            isVertical = true;
//            rec.setHeight(squareSize * shipSize);
//            rec.setWidth(squareSize);
            rec.setRotate(90);
            draw();
        }
    }
    public int[] getUsedArea() {
        int[] area = new int[4];
        if (isVertical) {
            area[0] = ((int)(x) + (int)offset) / squareSize - 1;
            area[1] = area[0] + 2;

            area[2] = ((int)(y) - (int)offset) / squareSize - 1;
            area[3] = area[2] + shipSize + 1;
        }
        else {
            area[0] = (int)(x) / 40 - 1;
            area[1] = area[0] + shipSize + 1;

            area[2] = (int)(y) / 40 - 1;
            area[3] = area[2] + 2;
        }
        return area;
    }

    public int getShipSize() {
        return shipSize;
    }

    public void setShipSize(int shipSize) {
        this.shipSize = shipSize;
    }

    public int getSquareSize() {
        return squareSize;
    }

    public void setSquareSize(int squareSize) {
        this.squareSize = squareSize;
    }

    public Rectangle getRec() {
        return rec;
    }

    public void setRec(Rectangle rec) {
        this.rec = rec;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getX() {
        return x;
    }

    public double getHomeX() {
        return homeX;
    }

    public void setHomeX(double homeX) {
        this.homeX = homeX;
    }

    public double getHomeY() {
        return homeY;
    }

    public void setHomeY(double homeY) {
        this.homeY = homeY;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public int getShipID() {
        return shipID;
    }

    public void setShipID(int shipID) {
        this.shipID = shipID;
    }

    public void setY(double y) {
        this.y = y;
    }
}