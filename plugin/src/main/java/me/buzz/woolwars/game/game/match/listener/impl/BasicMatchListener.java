package me.buzz.woolwars.game.game.match.listener.impl;

import me.buzz.woolwars.game.game.match.listener.MatchListener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class BasicMatchListener implements MatchListener {


    @Override
    public void interactAtEntity(PlayerInteractAtEntityEvent event) {

    }

    @Override
    public void damage(EntityDamageEvent event) {

    }

    @Override
    public void damageByEntity(EntityDamageByEntityEvent event) {

    }

    @Override
    public void interact(PlayerInteractEvent event) {

    }

    @Override
    public void itemPickup(PlayerPickupItemEvent event) {

    }

    @Override
    public void place(BlockPlaceEvent event) {

    }

    @Override
    public void blockBreak(BlockBreakEvent event) {

    }

    @Override
    public void chat(AsyncPlayerChatEvent event) {

    }

}
