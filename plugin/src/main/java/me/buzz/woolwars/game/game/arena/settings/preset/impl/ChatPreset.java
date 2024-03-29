package me.buzz.woolwars.game.game.arena.settings.preset.impl;

import lombok.Getter;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.game.arena.settings.preset.ApplicablePreset;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.hook.ImplementedHookType;
import me.buzz.woolwars.game.hook.hooks.placeholderapi.PlaceholderAPIHook;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

@Getter
public class ChatPreset implements ApplicablePreset<String, WoolMatch, Player, ChatPreset.AskingChatMotivation> {

    private final String joinMessage, quitMessage, chatFormat, spectatorFormat;

    public ChatPreset(FileConfiguration data) {
        joinMessage = data.getString("options.chat.join");
        quitMessage = data.getString("options.chat.quit");
        chatFormat = data.getString("options.chat.format");
        spectatorFormat = data.getString("options.chat.spectatorFormat");
    }

    @Override
    public String apply(WoolMatch woolMatch, Player player, AskingChatMotivation motivation) {
        String returnString;

        switch (motivation) {
            case SPECTATOR_CHAT: {
                returnString = spectatorFormat.replace("{displayPlayerName}", player.getDisplayName());
                break;
            }
            case CHAT: {
                returnString = chatFormat.replace("{displayPlayerName}", player.getDisplayName());
                break;
            }
            case JOIN: {
                returnString = joinMessage
                        .replace("{current}", String.valueOf(woolMatch.getPlayerHolder().getPlayersCount()))
                        .replace("{max}", String.valueOf(woolMatch.getMaxPlayers()))
                        .replace("{player}", player.getName());
                break;
            }
            default: {
                returnString = quitMessage
                        .replace("{current}", String.valueOf(woolMatch.getPlayerHolder().getPlayersCount() - 1))
                        .replace("{max}", String.valueOf(woolMatch.getMaxPlayers()))
                        .replace("{player}", player.getName());
                break;
            }
        }

        PlaceholderAPIHook placeholderHook = WoolWars.get().getHook(ImplementedHookType.PLACEHOLDER_API);
        return ChatColor.translateAlternateColorCodes('&', placeholderHook != null ? placeholderHook.apply(returnString, player) : returnString);
    }

    public enum AskingChatMotivation {
        CHAT,
        JOIN,
        QUIT,
        SPECTATOR_CHAT
    }

}
