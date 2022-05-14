package me.buzz.woolwars.nms.v1_13_R2.player;

import me.buzz.woolwars.game.utils.StringsUtils;
import me.buzz.woolwars.nms.interfacing.IPlayerHandler;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class PlayerHandler implements IPlayerHandler {

    @Override
    public void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(StringsUtils.colorize(message)));
    }

}
