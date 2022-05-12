package me.buzz.woolwars.game.utils;

public final class TeamUtils {

    public static int getHalfApprox(int size) {
        if (size % 2 == 1) return (size / 2) + 1;
        else return size / 2;
    }

}
