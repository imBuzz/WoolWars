package me.buzz.woolwars.game.configuration;

import ch.jalu.configme.SettingsHolder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.buzz.woolwars.game.configuration.files.ConfigFile;
import me.buzz.woolwars.game.configuration.files.LanguageFile;

@RequiredArgsConstructor
@Getter
public enum ConfigurationType {

    CONFIG("config.yml", ConfigFile.class),
    LANGUAGE("language.yml", LanguageFile.class);

    private final String fileName;
    private final Class<? extends SettingsHolder> clazz;

}
