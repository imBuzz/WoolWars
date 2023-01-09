package me.buzz.woolwars.game.game.match.listener;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public interface MatchListener {

    void processCommand(PlayerCommandPreprocessEvent event);

    void move(PlayerMoveEvent event);

    void damage(EntityDamageEvent event);

    void damageByEntity(EntityDamageByEntityEvent event);

    void interact(PlayerInteractEvent event);

    void itemPickup(PlayerPickupItemEvent event);

    void place(BlockPlaceEvent event);

    void blockBreak(BlockBreakEvent event);

    void dropItem(PlayerDropItemEvent event);

    void chat(AsyncPlayerChatEvent event);

    void entityExplode(EntityExplodeEvent event);

    void blockExplode(BlockExplodeEvent event);

    void inventoryClick(InventoryClickEvent event);

}
