package me.buzz.woolwars.game.utils;

import me.buzz.woolwars.game.game.match.WoolMatch;
import org.bukkit.DyeColor;

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

}