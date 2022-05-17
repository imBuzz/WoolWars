package me.buzz.woolwars.game.configuration.files;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.Property;
import me.buzz.woolwars.game.data.DataProviderType;
import me.buzz.woolwars.game.data.credentials.DatabaseCredentials;

import static ch.jalu.configme.properties.PropertyInitializer.newBeanProperty;

public class DatabaseFile implements SettingsHolder {

    @Comment("Choose one between MYSQL or SQLITE")
    public static final Property<DataProviderType> DATABASE_TYPE = newBeanProperty(DataProviderType.class, "database.type", DataProviderType.SQLITE);
    public static final Property<DatabaseCredentials> DATABASE_CREDENTIALS = newBeanProperty(DatabaseCredentials.class, "database.credentials",
            DatabaseCredentials.from("localhost", 3306, "minecraft", "root", "root"));

}
