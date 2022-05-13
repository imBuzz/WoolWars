package me.buzz.woolwars.game;

import lombok.Getter;
import me.buzz.woolwars.api.ApiWoolWars;
import me.buzz.woolwars.game.commands.StartCommand;
import me.buzz.woolwars.game.commands.TestCommand;
import me.buzz.woolwars.game.game.GameManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class WoolWars extends JavaPlugin implements ApiWoolWars {

    @Getter
    private GameManager gameManager;

    @Override
    public void onEnable() {
        instance = this;

        gameManager = new GameManager();
        gameManager.init();

        getCommand("test").setExecutor(new TestCommand());
        getCommand("start").setExecutor(new StartCommand());
    }

    @Override
    public void onDisable() {
        gameManager.stop();
    }

    private static WoolWars instance;
    public static WoolWars get(){
        return instance;
    }
}
