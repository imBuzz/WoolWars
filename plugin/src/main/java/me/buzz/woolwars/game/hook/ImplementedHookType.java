package me.buzz.woolwars.game.hook;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.buzz.woolwars.game.hook.hooks.placeholderapi.PlaceholderAPIHook;
import me.buzz.woolwars.game.hook.hooks.vault.VaultAPIHook;
import me.buzz.woolwars.game.hook.hooks.viaversion.ViaVersionAPIHook;
import org.bukkit.Bukkit;

import java.util.function.Supplier;

@RequiredArgsConstructor
@Getter
public enum ImplementedHookType {

    VAULT("Vault", VaultAPIHook::new),
    PLACEHOLDER_API("PlaceholderAPI", PlaceholderAPIHook::new),
    VIA_VERSION("ViaVersion", ViaVersionAPIHook::new);

    private final String pluginName;
    private final Supplier<ExternalPluginHook> supplier;

    public boolean isEnabled() {
        return Bukkit.getPluginManager().getPlugin(pluginName) != null && Bukkit.getPluginManager().getPlugin(pluginName).isEnabled();
    }

}
