package me.buzz.woolwars.game.game.match.listener.impl;

import lombok.RequiredArgsConstructor;
import me.buzz.woolwars.api.game.arena.region.ArenaRegionType;
import me.buzz.woolwars.api.game.arena.region.Region;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.game.match.listener.MatchListener;
import me.buzz.woolwars.game.game.match.player.team.impl.WoolTeam;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

@RequiredArgsConstructor
public class BasicMatchListener implements MatchListener {

    private final WoolMatch match;

    @Override
    public void interactAtEntity(PlayerInteractAtEntityEvent event) {
        //NPC DYNAMIC
    }

    @Override
    public void damage(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.CONTACT) return;
        if (event.getEntityType() != EntityType.PLAYER) return;
        Player victim = (Player) event.getEntity();

        if (event.getCause() == EntityDamageEvent.DamageCause.VOID || victim.getHealth() - event.getFinalDamage() <= 0) {
            event.setCancelled(true);
            match.handleDeath(victim, null, event.getCause());
        }
    }

    @Override
    public void damageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager().getType() != EntityType.PLAYER) return;
        if (event.getEntityType() != EntityType.PLAYER) return;

        Player victim = (Player) event.getEntity();

        if (victim.getHealth() - event.getFinalDamage() <= 0) {
            event.setCancelled(true);
            match.handleDeath(victim, (Player) event.getDamager(), event.getCause());
        }
    }

    @Override
    public void interact(PlayerInteractEvent event) {
        event.setUseInteractedBlock(Event.Result.DENY);
    }

    @Override
    public void itemPickup(PlayerPickupItemEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void place(BlockPlaceEvent event) {
        Region centerRegion = match.getArena().getRegion(ArenaRegionType.CENTER);
        if (!centerRegion.isInRegion(event.getBlockPlaced().getLocation())) {
            event.setCancelled(true);
            return;
        }

        Player player = event.getPlayer();
        WoolTeam woolTeam = match.getPlayerHolder().getMatchStats(player).getTeam();

        int totalBlocks = 0, sameTypeBlocks = 0;

        for (Block block : centerRegion.getBlocks()) {
            totalBlocks++;
            if (block.getType() != Material.WOOL) continue;

            DyeColor dyeColor = DyeColor.getByWoolData(block.getData());
            if (dyeColor == woolTeam.getTeamColor().getDC()) sameTypeBlocks++;
        }

        player.sendMessage("TotalBlocks: " + totalBlocks + " - SameBlocks: " + sameTypeBlocks);
        //if (totalBlocks == sameTypeBlocks) match.getRoundHolder().endRound();
    }

    @Override
    public void blockBreak(BlockBreakEvent event) {
        Region centerRegion = match.getArena().getRegion(ArenaRegionType.CENTER);
        if (centerRegion.isInRegion(event.getBlock().getLocation())) return;

        event.setCancelled(true);
    }

    @Override
    public void chat(AsyncPlayerChatEvent event) {

    }

}
