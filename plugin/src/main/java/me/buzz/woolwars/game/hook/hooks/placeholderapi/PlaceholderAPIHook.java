package me.buzz.woolwars.game.hook.hooks.placeholderapi;

import me.buzz.woolwars.game.hook.ExternalPluginHook;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlaceholderAPIHook implements ExternalPluginHook {

    private final List<PlaceholderExpansion> expansions = new ArrayList<>();

    @Override
    public void init() {
        expansions.add(new WoolwarsExpansion());
        expansions.forEach(PlaceholderExpansion::register);
    }

    @Override
    public void stop() {
        expansions.forEach(PlaceholderExpansion::unregister);
    }

    public String apply(String s, Player player) {
        return PlaceholderAPI.setPlaceholders(player, s);
    }

}
