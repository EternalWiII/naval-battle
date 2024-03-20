package com.navel.navalbattle.ships;

public class TwoBlocksShip extends Ship {
    public TwoBlocksShip(int shipID, int squareSize, int x, int y) {
        super(shipID, squareSize, x, y);
        shipSize = 2;
        hp = shipSize;
        offset = (double)squareSize / 2;
    }
}
