package com.navel.navalbattle.ships;

public class FourBlockShip extends Ship {
    /**
     * Конструктор об'єкта FourBlocksShip, який ініціалізує основні параметри за допомгою батьківського конструктора
     * та задає додаткові параметри.
     * @param shipID Порядковий номер корабля.
     * @param squareSize Довжина однієї сторони клітини в пікселях.
     * @param x Координата X корабля.
     * @param y Координата Y корабля.
     */
    public FourBlockShip(int shipID, int squareSize, int x, int y) {
        super(shipID, squareSize, x, y);
        shipSize = 4;
        hp = shipSize;
        offset = ((double)squareSize * 3) / 2;
        rec.setWidth(squareSize * shipSize);
        rec.setHeight(squareSize);
    }
}
