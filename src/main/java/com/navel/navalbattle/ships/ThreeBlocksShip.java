package com.navel.navalbattle.ships;

public class ThreeBlocksShip extends Ship {
    /**
     * Конструктор об'єкта ThreeBlocksShip, який ініціалізує основні параметри за допомгою батьківського конструктора
     * та задає додаткові параметри.
     * @param shipID Порядковий номер корабля.
     * @param squareSize Довжина однієї сторони клітини в пікселях.
     * @param x Координата X корабля.
     * @param y Координата Y корабля.
     */
    public ThreeBlocksShip(int shipID, int squareSize, int x, int y) {
        super(shipID, squareSize, x, y);
        shipSize = 3;
        hp = shipSize;
        offset = squareSize;
        rec.setWidth(squareSize * shipSize);
        rec.setHeight(squareSize);
    }
}
