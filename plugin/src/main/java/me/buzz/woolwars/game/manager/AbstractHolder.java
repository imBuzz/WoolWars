package me.buzz.woolwars.game.manager;

import lombok.RequiredArgsConstructor;
import me.buzz.woolwars.game.game.match.WoolMatch;

@RequiredArgsConstructor
public abstract class AbstractHolder {

    protected final WoolMatch match;

}
