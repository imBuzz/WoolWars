package me.buzz.woolwars.game.hook.hooks.vault;

import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.hook.ExternalPluginHook;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultAPIHook implements ExternalPluginHook {

    private Chat chat;

    @Override
    public void init() {
        RegisteredServiceProvider<Chat> rsp = Bukkit.getServer().getServicesManager().getRegistration(Chat.class);
        if (rsp != null) chat = rsp.getProvider();
        else {
            WoolWars.get().getLogger().info("VaultAPI found, but no chat provider used!");
        }
    }

    @Override
    public void stop() {
    }

    public String getPrefix(Player player) {
        if (chat == null) return "";
        return ChatColor.translateAlternateColorCodes('&', chat.getPlayerPrefix(player));
    }

}
