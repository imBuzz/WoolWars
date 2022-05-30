package me.buzz.woolwars.game.hook.hooks;

import me.buzz.woolwars.game.hook.ExternalPluginHook;
import me.buzz.woolwars.game.hook.hooks.placeholderapi.WoolwarsExpansion;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlaceholderAPIHook implements ExternalPluginHook<String, Player> {

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

    @Override
    public String apply(String s, Player player) {
        return PlaceholderAPI.setPlaceholders(player, s);
    }

}
