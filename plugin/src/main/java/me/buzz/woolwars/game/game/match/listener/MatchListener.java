package me.buzz.woolwars.game.game.match.listener;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public interface MatchListener {

    void interactAtEntity(PlayerInteractAtEntityEvent event);

    void damage(EntityDamageEvent event);

    void damageByEntity(EntityDamageByEntityEvent event);

    void interact(PlayerInteractEvent event);

    void itemPickup(PlayerPickupItemEvent event);

    void place(BlockPlaceEvent event);

    void blockBreak(BlockBreakEvent event);

    void chat(AsyncPlayerChatEvent event);

}
