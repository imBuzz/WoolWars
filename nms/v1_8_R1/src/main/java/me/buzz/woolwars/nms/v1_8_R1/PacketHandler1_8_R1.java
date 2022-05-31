package me.buzz.woolwars.nms.v1_8_R1;

import com.hakan.core.packet.event.PacketEvent;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.game.GameManager;
import me.buzz.woolwars.game.nms.NMSPacketHandler;
import net.minecraft.server.v1_8_R1.PacketPlayOutChat;

public class PacketHandler1_8_R1 implements NMSPacketHandler {

    private final GameManager gameManager = WoolWars.get().getGameManager();

    @Override
    public void onPacketReceive(PacketEvent event) {
        if (event.getPacket() instanceof PacketPlayOutChat) {


        }
    }


}
