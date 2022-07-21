package me.buzz.woolwars.game.utils;

import com.hakan.core.HCore;
import com.hakan.core.utils.ProtocolVersion;
import me.buzz.woolwars.game.game.match.WoolMatch;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class TeamUtils {

    public static int getHalfApprox(int size) {
        if (size % 2 == 1) return (size / 2) + 1;
        else return size / 2;
    }

    public static List<DyeColor> getTopTeamPlacedByWoolColor(Map<DyeColor, Integer> map) {
        return map.entrySet()
                .stream()
                .parallel()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public static Map<String, Integer> getTopKillers(WoolMatch match) {
        Map<String, Integer> killMap = new HashMap<>();
        match.getPlayerHolder().getStats().forEach((key, value) -> killMap.put(key, value.getMatchKills()));

        return killMap.entrySet()
                .stream()
                .parallel()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }

    public static Map<String, Integer> getTopBroken(WoolMatch match) {
        Map<String, Integer> blocksBroken = new HashMap<>();
        match.getPlayerHolder().getStats().forEach((key, value) -> blocksBroken.put(key, value.getMatchBlocksBroken()));

        return blocksBroken.entrySet()
                .stream()
                .parallel()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }

    public static Map<String, Integer> getTopWool(WoolMatch match) {
        Map<String, Integer> woolMap = new HashMap<>();
        match.getPlayerHolder().getStats().forEach((key, value) -> woolMap.put(key, value.getMatchWoolPlaced()));

        return woolMap.entrySet()
                .stream()
                .parallel()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }

    public static float getAbsorptionHearts(Player player) {
        try {
            if (HCore.getProtocolVersion().isOlderOrEqual(ProtocolVersion.v1_13_R2)) {
                Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + HCore.getVersionString() + ".entity.CraftPlayer");
                Class<?> entityPlayerClass = Class.forName("net.minecraft.server." + HCore.getVersionString() + ".EntityLiving");

                Object craftPlayer = craftPlayerClass.cast(player);
                Object entityPlayer = craftPlayerClass.getMethod("getHandle").invoke(craftPlayer);

                return (float) entityPlayerClass.getMethod("getAbsorptionHearts").invoke(entityPlayer);
            } else {
                return (float) player.getClass().getMethod("getAbsorptionAmount").invoke(player);
            }
        } catch (Exception ignored) {
            return 0;
        }
    }

    public static boolean hasAbsorptionHearts(Player player) {
        try {
            if (HCore.getProtocolVersion().isOlderOrEqual(ProtocolVersion.v1_13_R2)) {
                Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + HCore.getVersionString() + ".entity.CraftPlayer");
                Class<?> entityPlayerClass = Class.forName("net.minecraft.server." + HCore.getVersionString() + ".EntityLiving");

                Object craftPlayer = craftPlayerClass.cast(player);
                Object entityPlayer = craftPlayerClass.getMethod("getHandle").invoke(craftPlayer);

                return (float) entityPlayerClass.getMethod("getAbsorptionHearts").invoke(entityPlayer) > 0;
            } else {
                return (float) player.getClass().getMethod("getAbsorptionAmount").invoke(player) > 0;
            }
        } catch (Exception ignored) {
            return false;
        }
    }

    public static void setAbsorptionHearts(Player player, float absorptionHearts) {
        try {
            if (HCore.getProtocolVersion().isOlderOrEqual(ProtocolVersion.v1_13_R2)) {
                Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + HCore.getVersionString() + ".entity.CraftPlayer");
                Class<?> entityPlayerClass = Class.forName("net.minecraft.server." + HCore.getVersionString() + ".EntityLiving");

                Object craftPlayer = craftPlayerClass.cast(player);
                Object entityPlayer = craftPlayerClass.getMethod("getHandle").invoke(craftPlayer);

                float currentHearts = (float) entityPlayerClass.getMethod("getAbsorptionHearts").invoke(entityPlayer);
                if (currentHearts > 0) {
                    player.damage(currentHearts);
                }
                if (absorptionHearts > 0) {
                    entityPlayerClass.getMethod("setAbsorptionHearts", float.class).invoke(entityPlayer, absorptionHearts);
                }
            } else {
                float currentHearts = (float) player.getClass().getMethod("getAbsorptionAmount").invoke(player);
                if (currentHearts > 0) {
                    player.damage(currentHearts);
                }
                if (absorptionHearts > 0) {
                    player.getClass().getMethod("setAbsorptionAmount", float.class).invoke(player, absorptionHearts);
                }
            }
        } catch (Exception ignored) {

        }
    }

}