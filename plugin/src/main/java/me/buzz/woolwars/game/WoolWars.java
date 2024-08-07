package me.buzz.woolwars.game;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import com.hakan.core.HCore;
import com.jeff_media.updatechecker.UpdateCheckSource;
import com.jeff_media.updatechecker.UpdateChecker;
import com.jeff_media.updatechecker.UserAgentBuilder;
import lombok.Getter;
import me.buzz.woolwars.api.ApiWoolWars;
import me.buzz.woolwars.game.commands.WoolCommand;
import me.buzz.woolwars.game.configuration.ConfigurationType;
import me.buzz.woolwars.game.configuration.files.ConfigFile;
import me.buzz.woolwars.game.data.DataProvider;
import me.buzz.woolwars.game.game.GameManager;
import me.buzz.woolwars.game.hook.ExternalPluginHook;
import me.buzz.woolwars.game.hook.ImplementedHookType;
import me.buzz.woolwars.game.player.listener.PlayerListener;
import me.buzz.woolwars.game.player.tablist.ITabHandler;
import me.buzz.woolwars.game.player.tablist.impl.EmptyTabHandler;
import me.buzz.woolwars.game.player.tablist.impl.NativeTabHandler;
import me.buzz.woolwars.game.utils.workload.WorkloadHandler;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class WoolWars extends JavaPlugin implements ApiWoolWars {

    private final Map<ConfigurationType, SettingsManager> files = new HashMap<>();
    private final Map<ImplementedHookType, ExternalPluginHook> hooks = new HashMap<>();

    //private Metrics metrics;
    @Getter
    private DataProvider dataProvider;
    @Getter
    private GameManager gameManager;
    @Getter
    private ITabHandler tabHandler;

    @Override
    public void onEnable() {
        instance = this;
        if (!setupFiles()) {
            getLogger().severe("Error on creating plugin files, stopping...");
            setEnabled(false);
            return;
        }

        HCore.initialize(this);

        dataProvider = getSettings().getProperty(ConfigFile.DATABASE_TYPE).getProviderSupplier().get();
        dataProvider.init();

        WorkloadHandler.run();

        gameManager = new GameManager();
        gameManager.init();

        tabHandler = getSettings().getProperty(ConfigFile.ENABLE_NATIVE_TABLIST) ? new NativeTabHandler() : new EmptyTabHandler();

        checkForHooks();

        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        HCore.registerCommands(new WoolCommand());

        //metrics = new Metrics(this, 15400);
        new UpdateChecker(this, UpdateCheckSource.SPIGET, "102486")
                .checkEveryXHours(12)
                .setNotifyByPermissionOnJoin("woolwars.update")
                .setUserAgent(new UserAgentBuilder().addPluginNameAndVersion())
                .setDownloadLink("")
                .checkNow();

        printInformation();
    }

    @Override
    public void onDisable() {
        hooks.values().forEach(ExternalPluginHook::stop);
        gameManager.stop();

        hooks.clear();
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

    public Collection<SettingsManager> getAllFiles(){
        return files.values();
    }

    private void checkForHooks() {
        for (ImplementedHookType value : ImplementedHookType.values()) {
            if (value.isEnabled()) {
                ExternalPluginHook hook = value.getSupplier().get();
                hook.init();
                hooks.put(value, hook);
            }
        }

        getLogger().info("Loaded " + hooks.size() + " hooks " + hooks.keySet());
    }

    public <T extends ExternalPluginHook> T getHook(ImplementedHookType hook) {
        return (T) hooks.get(hook);
    }

    public SettingsManager getLanguage() {
        return files.get(ConfigurationType.LANGUAGE);
    }

    public SettingsManager getSettings() {
        return files.get(ConfigurationType.CONFIG);
    }

    public SettingsManager getGUISettings() {
        return files.get(ConfigurationType.GUI);
    }

    private void printInformation() {
        getLogger().info("▄▄▌ ▐ ▄▌            ▄▄▌  ▄▄▌ ▐ ▄▌ ▄▄▄· ▄▄▄  .▄▄ ·");
        getLogger().info("██· █▌▐█ ▄█▀▄  ▄█▀▄ ██•  ██· █▌▐█▐█ ▀█ ▀▄ █·▐█ ▀.");
        getLogger().info("██▪▐█▐▐▌▐█▌.▐▌▐█▌.▐▌██▪  ██▪▐█▐▐▌▄█▀▀█ ▐▀▀▄ ▄▀▀▀█▄");
        getLogger().info("▐█▌██▐█▌▐█▌.▐▌▐█▌.▐▌▐█▌▐▌▐█▌██▐█▌▐█ ▪▐▌▐█•█▌▐█▄▪▐█");
        getLogger().info("▀▀▀▀ ▀▪ ▀█▄▀▪ ▀█▄▀▪.▀▀▀  ▀▀▀▀ ▀▪ ▀  ▀ .▀  ▀ ▀▀▀▀ ");
        getLogger().info("");
        getLogger().info("Author: ImBuzz");
        getLogger().info("Version: " + getDescription().getVersion());
        getLogger().info("Running on: " + getServer().getVersion());
        getLogger().info("Java Version: " + System.getProperty("java.version"));
    }

    private static WoolWars instance;

    public static WoolWars get() {
        return instance;
    }
}
