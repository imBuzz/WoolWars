package me.buzz.woolwars.game;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import com.hakan.core.HCore;
import lombok.Getter;
import me.buzz.woolwars.api.ApiWoolWars;
import me.buzz.woolwars.game.commands.WoolCommand;
import me.buzz.woolwars.game.configuration.ConfigurationType;
import me.buzz.woolwars.game.configuration.files.DatabaseFile;
import me.buzz.woolwars.game.data.DataProvider;
import me.buzz.woolwars.game.game.GameManager;
import me.buzz.woolwars.game.player.listener.PlayerListener;
import me.buzz.woolwars.game.player.task.PlayerAsyncTickTask;
import me.buzz.woolwars.game.utils.workload.WorkloadHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class WoolWars extends JavaPlugin implements ApiWoolWars {

    private final Map<ConfigurationType, SettingsManager> files = new HashMap<>();

    @Getter
    private DataProvider dataProvider;
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

        HCore.initialize(this);

        dataProvider = getDataSettings().getProperty(DatabaseFile.DATABASE_TYPE).getProviderSupplier().get();
        dataProvider.init();

        WorkloadHandler.run();

        gameManager = new GameManager();
        gameManager.init();

        new PlayerAsyncTickTask().runTaskTimerAsynchronously(this, 5L, 5L);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);

        HCore.registerCommands(new WoolCommand());

        //new UpdateChecker(this, UpdateCheckSource.SPIGET, String.valueOf(SPIGOT_CODE))
        //        .checkEveryXHours(12)
        //        .setNotifyByPermissionOnJoin("petsreloaded.update")
        //        .setUserAgent(new UserAgentBuilder().addPluginNameAndVersion())
        //        .setDownloadLink("https://www.spigotmc.org/resources/petsreloaded-create-your-own-custom-pets-eula-compliant-1-8-x-1-18-x.98113/")
        //        .checkNow();
    }

    @Override
    public void onDisable() {
        gameManager.stop();
    }

    private boolean setupFiles() {
        try {
            for (ConfigurationType value : ConfigurationType.values()) {
                files.put(value, SettingsManagerBuilder
                        .withYamlFile(new File(getDataFolder().getAbsolutePath() + value.getPath(), value.getFileName()))
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

    public SettingsManager getDataSettings() {
        return files.get(ConfigurationType.DATABASE);
    }

    public SettingsManager getGUISettings() {
        return files.get(ConfigurationType.GUI);
    }

    private static WoolWars instance;

    public static WoolWars get() {
        return instance;
    }
}
