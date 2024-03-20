package com.navel.navalbattle.ships;

public class FourBlocksShip extends Ship {
    public FourBlocksShip(int shipID, int squareSize, int x, int y) {
        super(shipID, squareSize, x, y);
        shipSize = 4;
        hp = shipSize;
        offset = ((double)squareSize * 3) / 2;
    }
}
