package me.buzz.woolwars.game;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import lombok.Getter;
import me.buzz.woolwars.api.ApiWoolWars;
import me.buzz.woolwars.game.commands.WoolCommandsHandler;
import me.buzz.woolwars.game.configuration.ConfigurationType;
import me.buzz.woolwars.game.configuration.files.ConfigFile;
import me.buzz.woolwars.game.data.DataProvider;
import me.buzz.woolwars.game.game.GameManager;
import me.buzz.woolwars.game.player.listener.PlayerListener;
import me.buzz.woolwars.game.player.task.PlayerAsyncTickTask;
import me.buzz.woolwars.nms.INMSHandler;
import me.buzz.woolwars.nms.ServerProtocols;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class WoolWars extends JavaPlugin implements ApiWoolWars {

    private final Map<ConfigurationType, SettingsManager> files = new HashMap<>();
    private WoolCommandsHandler commandsHandler;

    @Getter
    private DataProvider dataProvider;
    @Getter
    private GameManager gameManager;
    @Getter
    private INMSHandler nmsHandler;

    @Override
    public void onEnable() {
        instance = this;
        if (!setupFiles()) {
            getLogger().severe("Error on creating plugin files, stopping...");
            setEnabled(false);
            return;
        }

        nmsHandler = ServerProtocols.getNmsHandler(this);
        if (nmsHandler == null) {
            setEnabled(false);
            return;
        }

        dataProvider = getSettings().getProperty(ConfigFile.DATABASE_TYPE).getProviderSupplier().get();
        dataProvider.init();

        gameManager = new GameManager();
        gameManager.init();

        commandsHandler = new WoolCommandsHandler();

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
            e.printStackTrace();
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
