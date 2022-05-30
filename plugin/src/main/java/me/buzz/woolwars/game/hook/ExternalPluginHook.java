package me.buzz.woolwars.game.hook;

public interface ExternalPluginHook<E, K> {

    void init();

    void stop();

    E apply(E e, K k);

}
