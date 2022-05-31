package me.buzz.woolwars.nms.v1_18_R2;

import me.buzz.woolwars.nms.INMSHandler;
import me.buzz.woolwars.nms.interfacing.IPlayerHandler;
import me.buzz.woolwars.nms.v1_18_R2.player.PlayerHandler;
import net.minecraft.network.protocol.game.ClientboundChatPacket;

public class NmsHandler implements INMSHandler {

    private final PlayerHandler playerHandler = new PlayerHandler();

    @Override
    public IPlayerHandler getPlayerHandler() {

        ClientboundChatPacket

        return playerHandler;
    }

}
