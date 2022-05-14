package me.buzz.woolwars.nms.v1_9_R2;

import me.buzz.woolwars.nms.INMSHandler;
import me.buzz.woolwars.nms.interfacing.IPlayerHandler;
import me.buzz.woolwars.nms.v1_9_R2.player.PlayerHandler;

public class NmsHandler implements INMSHandler {

    private final PlayerHandler playerHandler = new PlayerHandler();

    @Override
    public IPlayerHandler getPlayerHandler() {
        return playerHandler;
    }

}
