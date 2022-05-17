package me.buzz.woolwars.game.configuration;

import ch.jalu.configme.SettingsHolder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.buzz.woolwars.game.configuration.files.ConfigFile;
import me.buzz.woolwars.game.configuration.files.DatabaseFile;
import me.buzz.woolwars.game.configuration.files.gui.GuiFile;
import me.buzz.woolwars.game.configuration.files.lang.LanguageFile;

import java.io.File;

@RequiredArgsConstructor
@Getter
public enum ConfigurationType {

    DATABASE("database.yml", false, "", DatabaseFile.class),
    CONFIG("config.yml", true, "", ConfigFile.class),
    GUI("guis.yml", true, File.separator + "guis", GuiFile.class),
    LANGUAGE("language.yml", true, File.separator + "lang", LanguageFile.class);

    private final String fileName;
    private final boolean reloadable;

    private final String path;

    private final Class<? extends SettingsHolder> clazz;

}
