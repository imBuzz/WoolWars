package me.buzz.woolwars.game;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import lombok.Getter;
import me.buzz.woolwars.api.ApiWoolWars;
import me.buzz.woolwars.game.commands.StartCommand;
import me.buzz.woolwars.game.commands.TestCommand;
import me.buzz.woolwars.game.configuration.ConfigurationType;
import me.buzz.woolwars.game.game.GameManager;
import me.buzz.woolwars.game.player.PlayerAsyncTickTask;
import me.buzz.woolwars.game.player.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class WoolWars extends JavaPlugin implements ApiWoolWars {

    //TODO: FARE IL REJOIN TRAMITE UNA CACHE DI 3 MINUTI NELLA QUALE VI E' IL NOME DEL PLAYER E L'ID DELL ULTIMO GAME NEL QUALE E' ENTRATO

    private final Map<ConfigurationType, SettingsManager> files = new HashMap<>();
    @Getter
    private GameManager gameManager;

    @Override
    public void onEnable() {
        instance = this;
        if (!setupFiles()) {
            getLogger().severe("Error on creating plugin files, stopping...");
            setEnabled(false);
            return;
        }

        gameManager = new GameManager();
        gameManager.init();

        getCommand("test").setExecutor(new TestCommand());
        getCommand("start").setExecutor(new StartCommand());

        new PlayerAsyncTickTask().runTaskTimerAsynchronously(this, 5L, 5L);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
    }

    @Override
    public void onDisable() {
        gameManager.stop();
    }

    private boolean setupFiles() {
        try {
            for (ConfigurationType value : ConfigurationType.values()) {
                files.put(value, SettingsManagerBuilder
                        .withYamlFile(new File(getDataFolder(), value.getFileName()))
                        .configurationData(value.getClazz())
                        .useDefaultMigrationService()
                        .create());
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public SettingsManager getLanguage() {
        return files.get(ConfigurationType.LANGUAGE);
    }

    public SettingsManager getSettings() {
        return files.get(ConfigurationType.CONFIG);
    }

    private static WoolWars instance;

    public static WoolWars get() {
        return instance;
    }
}
