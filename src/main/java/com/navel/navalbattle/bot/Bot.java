package com.navel.navalbattle.bot;

interface Bot {
    /**
     * Виконує удар по кораблям користувача.
     */
    default void makeAction() {

    }

    /**
     * Робить вибір клітини, по якій потрібно нанести удар.
     */
    void makeDicision();
}
