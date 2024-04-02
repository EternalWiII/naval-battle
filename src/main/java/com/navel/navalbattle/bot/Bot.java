package com.navel.navalbattle.bot;

import com.navel.navalbattle.records.GridPosition;

interface Bot {
    /**
     * Виконує удар по кораблям користувача.
     */
    default void makeAction() {

    }

    /**
     * Робить вибір клітини, по якій потрібно нанести удар.
     */
    GridPosition makeDicision(boolean[][] isAlreadyHit);
}
