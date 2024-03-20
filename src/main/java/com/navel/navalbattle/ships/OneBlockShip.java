package com.navel.navalbattle.ships;

public class OneBlockShip extends Ship {
    public OneBlockShip(int shipID, int squareSize, int x, int y) {
        super(shipID, squareSize, x, y);
        shipSize = 1;
        hp = shipSize;
        offset = 0;
    }
}
