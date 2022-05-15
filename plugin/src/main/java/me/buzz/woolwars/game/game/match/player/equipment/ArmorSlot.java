package me.buzz.woolwars.game.game.match.player.equipment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ArmorSlot {

    HELMET(40),
    CHESTPLATE(39),
    LEGGINGS(38),
    BOOTS(37);

    private final int slot;

}
