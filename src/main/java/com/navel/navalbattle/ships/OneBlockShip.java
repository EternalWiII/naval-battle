package com.navel.navalbattle.ships;

public class OneBlockShip extends Ship {

    /**
     * Конструктор об'єкта OneBlockShip, який ініціалізує основні параметри за допомгою батьківського конструктора
     * та задає додаткові параметри.
     * @param shipID Порядковий номер корабля.
     * @param squareSize Довжина однієї сторони клітини в пікселях.
     * @param x Координата X корабля.
     * @param y Координата Y корабля.
     */
    public OneBlockShip(int shipID, int squareSize, int x, int y) {
        super(shipID, squareSize, x, y);
        shipSize = 1;
        hp = shipSize;
        offset = 0;
        rec.setWidth(squareSize * shipSize);
        rec.setHeight(squareSize);
    }
}
