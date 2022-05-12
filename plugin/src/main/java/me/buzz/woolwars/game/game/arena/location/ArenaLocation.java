package me.buzz.woolwars.game.game.arena.location;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.World;

@RequiredArgsConstructor @Getter
public class ArenaLocation {

    private Location location;

    private final double x;
    private final double y;
    private final double z;
    private final float pitch;
    private final float yaw;

    //World is used only the first time when "location" is null
    public Location toBukkitLocation(World world){
        if (location == null) return location = new Location(world, x, y, z, pitch, yaw);
        return location;
    }

    public String toString(){
        return x + ":" + y + ":" + z + ":" + pitch + ":" + yaw;
    }

    public static ArenaLocation fromString(String s){
        String[] parts = s.split(":");
        return new ArenaLocation(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]), Double.parseDouble(parts[2]),
                Float.parseFloat(parts[3]), Float.parseFloat(parts[4]));
    }

}
