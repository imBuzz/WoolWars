package me.buzz.woolwars.game.game.listener;

import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.game.GameManager;
import me.buzz.woolwars.game.game.match.WoolMatch;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;

public class GameListener implements Listener {

    private final GameManager gameManager = WoolWars.get().getGameManager();

    @EventHandler
    public void interactAtEntity(PlayerInteractAtEntityEvent event) {
        WoolMatch woolMatch = gameManager.getMatchByWorldName(event.getPlayer().getWorld().getName());
        if (woolMatch == null) return;

        woolMatch.getMatchListener().interactAtEntity(event);
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

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        WoolMatch woolMatch = gameManager.getMatchByWorldName(event.getPlayer().getWorld().getName());
        if (woolMatch == null) return;

        woolMatch.getMatchListener().chat(event);
    }

}