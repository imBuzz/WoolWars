package me.buzz.woolwars.game.game.match.player.equipment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ArmorSlot {

    HELMET(103),
    CHESTPLATE(102),
    LEGGINGS(101),
    BOOTS(100),
    OTHER(-1);

    private final int slot;

}
