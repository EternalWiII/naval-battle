package com.navel.navalbattle.bot;

import com.navel.navalbattle.records.GridPosition;

import java.util.Random;

public class EasyBot implements Bot {
    @Override
    public GridPosition makeDicision(boolean[][] isAlreadyHit) {
        Random rand = new Random();
        do {
            int x = rand.nextInt(10);
            int y = rand.nextInt(10);
            if (!isAlreadyHit[x][y]) {
                return new GridPosition(x, y);
            }
        } while (true);
    }
}
