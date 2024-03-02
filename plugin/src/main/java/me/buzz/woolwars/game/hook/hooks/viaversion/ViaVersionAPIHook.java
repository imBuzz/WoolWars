package me.buzz.woolwars.game.hook.hooks.viaversion;

import com.viaversion.viaversion.api.Via;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.hook.ExternalPluginHook;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class ViaVersionAPIHook implements ExternalPluginHook {

    @Override
    public void init() {
    }

    @Override
    public void stop() {
    }

    public int getPlayerMinecraftVersion(Player player) {
        return Via.getAPI().getPlayerVersion(player.getUniqueId());
    }

}
