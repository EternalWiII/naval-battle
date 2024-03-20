package com.navel.navalbattle.ships;

public class ThreeBlocksShip extends Ship {
    public ThreeBlocksShip(int shipID, int squareSize, int x, int y) {
        super(shipID, squareSize, x, y);
        shipSize = 3;
        hp = shipSize;
        offset = squareSize;
    }
}
