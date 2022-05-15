package me.buzz.woolwars.game.game.match.listener;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;

public interface MatchListener {

    void interactAtEntity(PlayerInteractAtEntityEvent event);

    void damage(EntityDamageEvent event);

    void damageByEntity(EntityDamageByEntityEvent event);

    void interact(PlayerInteractEvent event);

    void itemPickup(PlayerPickupItemEvent event);

    void place(BlockPlaceEvent event);

    void blockBreak(BlockBreakEvent event);

    void dropItem(PlayerDropItemEvent event);

    void chat(AsyncPlayerChatEvent event);

}
