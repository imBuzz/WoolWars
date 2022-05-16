package me.buzz.woolwars.game.game.arena.location;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.World;

@Getter
@NoArgsConstructor
public class SerializedLocation {

    private Location location;

    @Setter
    private String worldName;
    @Setter
    private double x;
    @Setter
    private double y;
    @Setter
    private double z;
    @Setter
    private float pitch;
    @Setter
    private float yaw;

    public SerializedLocation(String worldName, double x, double y, double z, float pitch, float yaw) {
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    //World is used only the first time when "location" is null
    public Location toBukkitLocation(World world) {
        if (location == null) return location = new Location(world, x, y, z, pitch, yaw);
        return location;
    }

    public String toString() {
        return x + ":" + y + ":" + z + ":" + pitch + ":" + yaw;
    }

    public static SerializedLocation fromString(String s) {
        String[] parts = s.split(":");
        return new SerializedLocation("", Double.parseDouble(parts[0]), Double.parseDouble(parts[1]), Double.parseDouble(parts[2]),
                Float.parseFloat(parts[3]), Float.parseFloat(parts[4]));
    }

    public static SerializedLocation from(String worldName, double x, double y, double z, float pitch, float yaw) {
        SerializedLocation location = new SerializedLocation();
        location.setWorldName(worldName);
        location.setX(x);
        location.setY(y);
        location.setZ(z);
        location.setPitch(pitch);
        location.setYaw(yaw);
        return location;
    }

}
