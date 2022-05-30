package me.buzz.woolwars.game.hook;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.buzz.woolwars.game.hook.hooks.PlaceholderAPIHook;
import org.bukkit.Bukkit;

import java.util.function.Supplier;

@RequiredArgsConstructor
@Getter
public enum ImplementedHookType {

    PLACEHOLDER_API("PlaceholderAPI", PlaceholderAPIHook::new);

    private final String pluginName;
    private final Supplier<ExternalPluginHook<?, ?>> supplier;

    public boolean isEnabled() {
        return Bukkit.getPluginManager().getPlugin(pluginName) != null && Bukkit.getPluginManager().getPlugin(pluginName).isEnabled();
    }

}
