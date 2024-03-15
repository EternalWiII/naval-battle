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
    private double offset = 0;
    private int hp;

    public Ship(int shipID, int squareSize, Rectangle rec, int x, int y, int shipSize) {
        this.squareSize = squareSize;
        this.rec = rec;
        this.x = x;
        this.y = y;
        this.shipSize = shipSize;
        hp = shipSize;
        this.shipID = shipID;
        switch (shipSize) {
            case 2 -> offset = squareSize / 2;
            case 3 -> offset = squareSize;
            case 4 -> offset = (squareSize * 3) / 2;
        }
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
            rec.setRotate(0);
            draw();
        }
        else {
            isVertical = true;
            rec.setRotate(90);
            draw();
        }
    }

    public int[] getUsedArea() {
        int[] area = new int[4];
        if (isVertical) {
            if (((int)(x) + (int)offset ) / squareSize >= 10) {
                System.out.println(((int)(x) + (int)offset / squareSize));
                area[0] = ((int)(x) + (int)offset) / squareSize;
                area[1] = area[0];

                area[2] = ((int)(y) - (int)offset) / squareSize;
                area[3] = area[2] + shipSize - 1;
            }
            else {
                area[0] = ((int)(x) + (int)offset) / squareSize - 1;
                area[1] = area[0] + 2;

                area[2] = ((int)(y) - (int)offset) / squareSize - 1;
                area[3] = area[2] + shipSize + 1;
            }
        }
        else {
            if (((int)(x) / squareSize) >= 10) {
                area[0] = (int)(x) / squareSize;
                area[1] = area[0] + shipSize - 1;

                area[2] = (int)(y) / squareSize;
                area[3] = area[2];
            }
            else {
                area[0] = (int)(x) / squareSize - 1;
                area[1] = area[0] + shipSize + 1;

                area[2] = (int)(y) / squareSize - 1;
                area[3] = area[2] + 2;
            }
        }
        return area;
    }

    public boolean getHit() {
        hp--;
        if (hp == 0) {
            return true;
        }
        return false;
    }

    public void becomeDestroyed() {
        rec.setFill(Color.GRAY);
        rec.toFront();
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
    public boolean isVertical() {
        return isVertical;
    }
    public void setVertical(boolean vertical) {
        isVertical = vertical;
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
    public int getShipSize() {
        return shipSize;
    }
    public Rectangle getRec() {
        return rec;
    }
    public int getShipID() {
        return shipID;
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
    public Color getColor() {
        return color;
    }
    public void setColor(Color color) {
        this.color = color;
    }
    public void setShipID(int shipID) {
        this.shipID = shipID;
    }
}