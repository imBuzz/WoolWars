package me.buzz.woolwars.game.manager;

import me.buzz.woolwars.game.WoolWars;

public abstract class AbstractManager {

    protected final WoolWars plugin = WoolWars.get();

    public abstract void init();
    public abstract void stop();

}
