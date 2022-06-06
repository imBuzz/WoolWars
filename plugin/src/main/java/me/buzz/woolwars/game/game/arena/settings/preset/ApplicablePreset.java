package me.buzz.woolwars.game.game.arena.settings.preset;

public interface ApplicablePreset<A, B, C, D> {

    A apply(B b, C c, D d);

}
