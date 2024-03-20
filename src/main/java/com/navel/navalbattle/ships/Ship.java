package com.navel.navalbattle.ships;

import com.navel.navalbattle.records.ShipUsedArea;
import javafx.scene.paint.Color;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Ship {
    protected int shipID;
    protected int shipSize;
    protected int squareSize;
    protected Rectangle rec;
    protected double x;
    protected double y;
    protected boolean isVertical = false;
    protected Color color = Color.BLACK;
    protected double homeX;
    protected double homeY;
    protected boolean homeIsVertical;
    protected double offset = 0;
    protected int hp;

    public Ship(int shipID, int squareSize, int x, int y) {
        this.squareSize = squareSize;
        rec = new Rectangle();
        rec.setFill(Color.RED);
        rec.setStroke(Color.BLACK);
        this.x = x;
        this.y = y;
        this.shipID = shipID;
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

    public ShipUsedArea getUsedArea() {
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

        return new ShipUsedArea(area[0], area[1], area[2] , area[3]);
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