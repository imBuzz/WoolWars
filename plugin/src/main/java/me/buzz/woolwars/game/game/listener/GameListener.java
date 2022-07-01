package me.buzz.woolwars.game.game.listener;

import me.buzz.woolwars.api.player.QuitGameReason;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.configuration.files.lang.LanguageFile;
import me.buzz.woolwars.game.game.GameManager;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.hook.ImplementedHookType;
import me.buzz.woolwars.game.hook.hooks.vault.VaultAPIHook;
import me.buzz.woolwars.game.player.WoolPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class GameListener implements Listener {

    private final GameManager gameManager = WoolWars.get().getGameManager();

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void processCommand(PlayerCommandPreprocessEvent event) {
        WoolMatch woolMatch = gameManager.getMatchByWorldName(event.getPlayer().getWorld().getName());
        if (woolMatch == null) return;

        woolMatch.getMatchListener().processCommand(event);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void teleport(PlayerTeleportEvent event) {
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.PLUGIN) return;
        if (event.getFrom().getWorld() == event.getTo().getWorld()) return;

        Player player = event.getPlayer();
        WoolPlayer woolPlayer = WoolPlayer.getWoolPlayer(player);

        WoolMatch woolMatch = gameManager.getMatchByWorldName(event.getTo().getWorld().getName());
        if (woolMatch == null) return;

        WoolMatch playerMatch = gameManager.getInternalMatchByPlayer(player);
        if (playerMatch != null) playerMatch.quit(woolPlayer, QuitGameReason.OTHER);

        if (woolMatch.checkJoin(woolPlayer)) {
            woolMatch.joinAsPlayer(woolPlayer);
        } else {
            woolMatch.joinAsSpectator(woolPlayer);
        }
    }

    @EventHandler
    public void move(PlayerMoveEvent event) {
        WoolMatch woolMatch = gameManager.getMatchByWorldName(event.getPlayer().getWorld().getName());
        if (woolMatch == null) return;

        woolMatch.getMatchListener().move(event);
    }

    @EventHandler
    public void damage(EntityDamageEvent event) {
        WoolMatch woolMatch = gameManager.getMatchByWorldName(event.getEntity().getWorld().getName());
        if (woolMatch == null) return;

        woolMatch.getMatchListener().damage(event);
    }

    @EventHandler
    public void damageByEntity(EntityDamageByEntityEvent event) {
        WoolMatch woolMatch = gameManager.getMatchByWorldName(event.getEntity().getWorld().getName());
        if (woolMatch == null) return;

        woolMatch.getMatchListener().damageByEntity(event);
    }

    @EventHandler
    public void interact(PlayerInteractEvent event) {
        WoolMatch woolMatch = gameManager.getMatchByWorldName(event.getPlayer().getWorld().getName());
        if (woolMatch == null) return;

        woolMatch.getMatchListener().interact(event);
    }

    @EventHandler
    public void itemPickup(PlayerPickupItemEvent event) {
        WoolMatch woolMatch = gameManager.getMatchByWorldName(event.getItem().getWorld().getName());
        if (woolMatch == null) return;

        woolMatch.getMatchListener().itemPickup(event);
    }

    @EventHandler
    public void place(BlockPlaceEvent event) {
        WoolMatch woolMatch = gameManager.getMatchByWorldName(event.getBlock().getWorld().getName());
        if (woolMatch == null) return;

        woolMatch.getMatchListener().place(event);
    }

    @EventHandler
    public void blockBreak(BlockBreakEvent event) {
        WoolMatch woolMatch = gameManager.getMatchByWorldName(event.getBlock().getWorld().getName());
        if (woolMatch == null) return;

        woolMatch.getMatchListener().blockBreak(event);
    }

    @EventHandler
    public void entityExplode(EntityExplodeEvent event) {
        WoolMatch woolMatch = gameManager.getMatchByWorldName(event.getEntity().getWorld().getName());
        if (woolMatch == null) return;

        woolMatch.getMatchListener().entityExplode(event);
    }

    @EventHandler
    public void inventoryClick(InventoryClickEvent event) {
        WoolMatch woolMatch = gameManager.getMatchByWorldName(event.getWhoClicked().getWorld().getName());
        if (woolMatch == null) return;

        woolMatch.getMatchListener().inventoryClick(event);
    }

    @EventHandler
    public void blockExplode(BlockExplodeEvent event) {
        WoolMatch woolMatch = gameManager.getMatchByWorldName(event.getBlock().getWorld().getName());
        if (woolMatch == null) return;

        woolMatch.getMatchListener().blockExplode(event);
    }

    @EventHandler
    public void dropItem(PlayerDropItemEvent event) {
        WoolMatch woolMatch = gameManager.getMatchByWorldName(event.getItemDrop().getWorld().getName());
        if (woolMatch == null) return;

        woolMatch.getMatchListener().dropItem(event);
    }

    @EventHandler
    public void foodLose(FoodLevelChangeEvent event) {
        WoolMatch woolMatch = gameManager.getMatchByWorldName(event.getEntity().getWorld().getName());
        if (woolMatch == null) return;

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onChat(AsyncPlayerChatEvent event) {
        VaultAPIHook vaultAPIHook = WoolWars.get().getHook(ImplementedHookType.VAULT);

        WoolMatch worldMatch = gameManager.getMatchByWorldName(event.getPlayer().getWorld().getName());
        WoolMatch playerMatch = gameManager.getInternalMatchByPlayer(event.getPlayer());

        if (worldMatch == null || playerMatch == null) {
            event.setCancelled(true);

            String message = WoolWars.get().getLanguage().getProperty(LanguageFile.LOBBY_CHAT)
                    .replace("{prefix}", vaultAPIHook != null ? vaultAPIHook.getPrefix(event.getPlayer()) : "")
                    .replace("{player}", event.getPlayer().getName())
                    .replace("{message}", event.getMessage());

            for (WoolPlayer woolOnlinePlayer : WoolPlayer.getWoolOnlinePlayers()) {
                if (woolOnlinePlayer.isInMatch()) continue;
                woolOnlinePlayer.toBukkitPlayer().sendMessage(message);
            }

            return;
        }

        worldMatch.getMatchListener().chat(event);
    }

}
