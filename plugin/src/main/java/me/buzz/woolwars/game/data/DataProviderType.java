package me.buzz.woolwars.game.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.buzz.woolwars.game.data.mysql.MySQLProvider;
import me.buzz.woolwars.game.data.sqlite.SQLProvider;

import java.util.function.Supplier;

@RequiredArgsConstructor
@Getter
public enum DataProviderType {

    MYSQL(MySQLProvider::new),
    SQLITE(SQLProvider::new);

    private final Supplier<DataProvider> providerSupplier;


}
