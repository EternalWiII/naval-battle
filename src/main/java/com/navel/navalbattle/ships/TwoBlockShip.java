package com.navel.navalbattle.ships;

public class TwoBlockShip extends Ship {
    /**
     * Конструктор об'єкта TwoBlocksShip, який ініціалізує основні параметри за допомгою батьківського конструктора
     * та задає додаткові параметри.
     * @param shipID Порядковий номер корабля.
     * @param squareSize Довжина однієї сторони клітини в пікселях.
     * @param x Координата X корабля.
     * @param y Координата Y корабля.
     */
    public TwoBlockShip(int shipID, int squareSize, int x, int y) {
        super(shipID, squareSize, x, y);
        shipSize = 2;
        hp = shipSize;
        offset = (double)squareSize / 2;
        rec.setWidth(squareSize * shipSize);
        rec.setHeight(squareSize);
    }
}
