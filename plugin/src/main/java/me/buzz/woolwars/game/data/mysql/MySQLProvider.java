package me.buzz.woolwars.game.data.mysql;

import me.buzz.woolwars.api.game.match.player.player.classes.PlayableClassType;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.configuration.files.DatabaseFile;
import me.buzz.woolwars.game.data.DataProvider;
import me.buzz.woolwars.game.data.DataProviderType;
import me.buzz.woolwars.game.data.credentials.DatabaseCredentials;
import me.buzz.woolwars.game.data.mysql.utils.MySQL;
import me.buzz.woolwars.game.player.WoolPlayer;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class MySQLProvider implements DataProvider {

    private final WoolWars woolWars = WoolWars.get();
    private final String WOOL_PLAYERS_TABLE = "wool_players";
    private final String WOOL_KITS_TABLE = "wool_kits";

    private MySQL mySQl;

    @Override
    public void init() {
        DatabaseCredentials credentials = woolWars.getDataSettings().getProperty(DatabaseFile.DATABASE_CREDENTIALS);
        mySQl = new MySQL(credentials.getAddress(), credentials.getHost(), credentials.getDatabase(), credentials.getUsername(), credentials.getPassword());
        try {
            mySQl.createTable(new String[]{
                    "uuid VARCHAR(36) NOT NULL",
                    "username VARCHAR(16)",
                    "woolPlaced INTEGER",
                    "blocksBroken INTEGER",
                    "powerUpsGotten INTEGER",
                    "wins INTEGER",
                    "played INTEGER",
                    "kills INTEGER",
                    "deaths INTEGER",
                    "PRIMARY KEY (uuid)"
            }, WOOL_PLAYERS_TABLE);

            mySQl.createTable(new String[]{
                    "uuid VARCHAR(36) NOT NULL",
                    "username VARCHAR(16)",
                    "tank_kit VARCHAR(9)",
                    "assault_kit VARCHAR(9)",
                    "archer_kit VARCHAR(9)",
                    "swordman_kit VARCHAR(9)",
                    "golem_kit VARCHAR(9)",
                    "engineer_kit VARCHAR(9)",
                    "PRIMARY KEY (uuid)"
            }, WOOL_KITS_TABLE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public CompletableFuture<WoolPlayer> loadPlayer(Player player) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                WoolPlayer woolPlayer;

                if (mySQl.rowExists("uuid", player.getUniqueId(), WOOL_PLAYERS_TABLE)) {
                    Map<PlayableClassType, String> kitLayout = new HashMap<>();

                    String[] layouts = getStrings(player, WOOL_KITS_TABLE,
                            "tank_kit", "assault_kit", "archer_kit", "swordman_kit", "golem_kit", "engineer_kit");

                    if (layouts != null) {
                        int i = 0;
                        for (PlayableClassType value : PlayableClassType.values()) {
                            kitLayout.put(value, layouts[i]);
                            i++;
                        }
                    }

                    woolPlayer = new WoolPlayer(player.getUniqueId(), player.getName(),
                            kitLayout,

                            mySQl.getInteger("uuid", player.getUniqueId(), "woolPlaced", "wool_players"),
                            mySQl.getInteger("uuid", player.getUniqueId(), "blocksBroken", "wool_players"),
                            mySQl.getInteger("uuid", player.getUniqueId(), "powerUpsGotten", "wool_players"),
                            mySQl.getInteger("uuid", player.getUniqueId(), "wins", "wool_players"),
                            mySQl.getInteger("uuid", player.getUniqueId(), "played", "wool_players"),
                            mySQl.getInteger("uuid", player.getUniqueId(), "kills", "wool_players"),
                            mySQl.getInteger("uuid", player.getUniqueId(), "deaths", "wool_players"), false);

                } else {
                    woolPlayer = new WoolPlayer(player);
                }

                woolPlayer.load();
                return woolPlayer;
            } catch (Exception e) {
                return null;
            }
        });
    }

    @Override
    public void savePlayer(WoolPlayer woolPlayer) {
        try {
            if (mySQl.rowExists("uuid", woolPlayer.getUUID(), WOOL_PLAYERS_TABLE)) {
                try (Connection connection = mySQl.connector.getConnection()) {
                    Statement statement = connection.createStatement();
                    statement.executeUpdate("UPDATE wool_players SET " +
                            "username='" + woolPlayer.getName() + "', " +
                            "woolPlaced='" + woolPlayer.getWoolPlaced() + "', " +
                            "blocksBroken='" + woolPlayer.getBlocksBroken() + "', " +
                            "powerUpsGotten='" + woolPlayer.getPowerUpsGotten() + "', " +
                            "wins='" + woolPlayer.getWins() + "', " +
                            "played='" + woolPlayer.getPlayed() + "', " +
                            "kills='" + woolPlayer.getKills() + "', " +
                            "deaths='" + woolPlayer.getDeaths() + "' WHERE uuid='" + woolPlayer.getUUID().toString() + "'");

                    statement.executeUpdate("UPDATE wool_kits SET " +
                            "username='" + woolPlayer.getName() + "', " +
                            "tank_kit='" + woolPlayer.getKitLayout(PlayableClassType.TANK) + "', " +
                            "assault_kit='" + woolPlayer.getKitLayout(PlayableClassType.ASSAULT) + "', " +
                            "archer_kit='" + woolPlayer.getKitLayout(PlayableClassType.ARCHER) + "', " +
                            "swordman_kit='" + woolPlayer.getKitLayout(PlayableClassType.SWORDMAN) + "', " +
                            "golem_kit='" + woolPlayer.getKitLayout(PlayableClassType.GOLEM) + "', " +
                            "engineer_kit='" + woolPlayer.getKitLayout(PlayableClassType.ENGINEER) +
                            "' WHERE uuid='" + woolPlayer.getUUID().toString() + "'");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try (Connection connection = mySQl.connector.getConnection()) {
                    Statement statement = connection.createStatement();
                    statement.executeUpdate("INSERT INTO wool_players (uuid, username, woolPlaced, blocksBroken, powerUpsGotten, " +
                            "wins, played, kills, deaths) " + "VALUES ('" +
                            woolPlayer.getUUID().toString() +
                            "', '" +
                            woolPlayer.getName() +
                            "', '" +
                            woolPlayer.getWoolPlaced() +
                            "', '" +
                            woolPlayer.getBlocksBroken() +
                            "', '" +
                            woolPlayer.getPowerUpsGotten() +
                            "', '" +
                            woolPlayer.getWins() +
                            "', '" +
                            woolPlayer.getPlayed() +
                            "', '" +
                            woolPlayer.getKills() +
                            "', '" +
                            woolPlayer.getDeaths() + "')");


                    statement.executeUpdate("INSERT INTO wool_kits (uuid, username, tank_kit, assault_kit, archer_kit, swordman_kit, golem_kit, engineer_kit) " + "VALUES ('" +
                            woolPlayer.getUUID().toString() +
                            "', '" +
                            woolPlayer.getName() +
                            "', '" +
                            woolPlayer.getKitLayout(PlayableClassType.TANK) +
                            "', '" +
                            woolPlayer.getKitLayout(PlayableClassType.ASSAULT) +
                            "', '" +
                            woolPlayer.getKitLayout(PlayableClassType.ARCHER) +
                            "', '" +
                            woolPlayer.getKitLayout(PlayableClassType.SWORDMAN) +
                            "', '" +
                            woolPlayer.getKitLayout(PlayableClassType.GOLEM) +
                            "', '" +
                            woolPlayer.getKitLayout(PlayableClassType.ENGINEER) + "')");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public DataProviderType getType() {
        return DataProviderType.MYSQL;
    }

    private String[] getStrings(Player player, String table, String... columns) {
        try (Connection connection = mySQl.connector.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery("SELECT * FROM " + table + " WHERE uuid='" + player.getUniqueId().toString() + "' LIMIT 1");
            set.next();

            List<String> values = new ArrayList<>();
            for (String column : columns) values.add(set.getString(column));

            set.close();
            statement.close();

            return values.toArray(new String[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
