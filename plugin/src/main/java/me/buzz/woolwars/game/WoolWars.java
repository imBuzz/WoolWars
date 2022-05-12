package me.buzz.woolwars.game;

import me.buzz.woolwars.api.WoolWarsAPI;
import org.bukkit.plugin.java.JavaPlugin;

public final class WoolWars extends JavaPlugin implements WoolWarsAPI {

    @Override
    public void onEnable() {
        instance = this;

        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private static WoolWars instance;
    public static WoolWars get(){
        return instance;
    }

}
