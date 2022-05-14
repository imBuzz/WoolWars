package me.buzz.woolwars.nms.v1_9_R1.player;

import me.buzz.woolwars.game.utils.StringsUtils;
import me.buzz.woolwars.nms.interfacing.IPlayerHandler;
import net.minecraft.server.v1_9_R1.ChatComponentText;
import net.minecraft.server.v1_9_R1.IChatBaseComponent;
import net.minecraft.server.v1_9_R1.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PlayerHandler implements IPlayerHandler {

    @Override
    public void sendActionBar(Player player, String message) {
        IChatBaseComponent component = new ChatComponentText(StringsUtils.colorize(message));
        PacketPlayOutChat packet = new PacketPlayOutChat(component, (byte) 2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

}
