package me.buzz.woolwars.game.utils;

import java.util.UUID;

public final class UUIDUtils {

    public static String getNewUUID() {
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.substring(0, 6) + uuid.substring(8, 10);
        return uuid;
    }

}
