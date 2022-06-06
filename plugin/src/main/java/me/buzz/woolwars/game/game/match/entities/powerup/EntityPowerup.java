package me.buzz.woolwars.game.game.match.entities.powerup;

import lombok.RequiredArgsConstructor;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.game.match.entities.TickedEntity;
import me.buzz.woolwars.game.utils.workload.Workload;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class EntityPowerup implements Workload, TickedEntity {

    private final WoolMatch woolMatch;
    private final PowerUPType type;
    private final Location location;
    private boolean picked = false;
    private ArmorStand entity, name;

    @Override
    public void spawn() {
        entity = location.getWorld().spawn(location, ArmorStand.class);
        entity.setGravity(false);
        entity.setVisible(false);
        entity.setMarker(true);

        name = location.getWorld().spawn(entity.getLocation().clone().add(0, 2.2, 0), ArmorStand.class);
        name.setGravity(false);
        name.setVisible(false);
        name.setMarker(true);
        name.setSmall(true);

        type.getAdjust().accept(entity);
        WoolMatch.workloadObjects.add(this);
    }

    @Override
    public void die() {
        entity.remove();
        name.remove();

        WoolMatch.workloadObjects.remove(this);
    }

    public void move() {
        Location location = entity.getLocation();

        float newPos = location.getYaw() + 5;
        newPos = newPos > 360 ? 0 : newPos;

        location.setYaw(newPos);

        entity.teleport(location);
    }

    public void setName(String newName) {
        name.setCustomName(newName);
        if (!name.isCustomNameVisible()) name.setCustomNameVisible(true);
    }

    @Override
    public void tick() {
        move();
        for (Entity nearbyEntity : entity.getNearbyEntities(1, 1, 1)) {
            if (picked) break;
            if (nearbyEntity.getType() != EntityType.PLAYER) continue;
            Player player = (Player) nearbyEntity;
            if (woolMatch.getPlayerHolder().isSpectator(player)) continue;

            picked = true;
            type.getAction().accept(player);
            woolMatch.pickUP(player, this, type);
            die();
        }
    }

    @Override
    public void compute() {
        tick();
    }
}
