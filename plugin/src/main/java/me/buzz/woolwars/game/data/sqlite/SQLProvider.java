package me.buzz.woolwars.game.data.sqlite;

import me.buzz.woolwars.api.game.match.player.player.classes.PlayableClassType;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.data.DataProvider;
import me.buzz.woolwars.game.data.DataProviderType;
import me.buzz.woolwars.game.player.WoolPlayer;
import org.bukkit.entity.Player;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class SQLProvider implements DataProvider {

    private final WoolWars woolWars = WoolWars.get();
    private Connection connection;

    @Override
    public void init() {
        try {
            File databaseFile = new File(woolWars.getDataFolder(), "database.db");
            if (!databaseFile.exists()) {
                databaseFile.createNewFile();
            }

            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + databaseFile.getAbsolutePath());
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(5);

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS wool_players (" +
                    "uuid VARCHAR(36) NOT NULL, " +
                    "username VARCHAR(16), " +
                    "woolPlaced INTEGER, " +
                    "blocksBroken INTEGER, " +
                    "powerUpsGotten INTEGER, " +
                    "wins INTEGER, " +
                    "played INTEGER, " +
                    "kills INTEGER, " +
                    "deaths INTEGER, PRIMARY KEY (uuid))");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS wool_kits (" +
                    "uuid VARCHAR(36) NOT NULL, " +
                    "username VARCHAR(16), " +
                    "tank_kit VARCHAR(9), " +
                    "assault_kit VARCHAR(9), " +
                    "archer_kit VARCHAR(9), " +
                    "swordman_kit VARCHAR(9), " +
                    "golem_kit VARCHAR(9), " +
                    "engineer_kit VARCHAR(9), PRIMARY KEY (uuid))");
            statement.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public CompletableFuture<WoolPlayer> loadPlayer(Player player) {
        return CompletableFuture.supplyAsync(() -> {
            WoolPlayer woolPlayer;
            if (isPlayerPresentOnDB(player, "wool_players")) {

                Map<PlayableClassType, String> kitLayout = new HashMap<>();
                String[] layouts = getStrings(player, "wool_kits",
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
                        getInt(player, "woolPlaced", "wool_players"),
                        getInt(player, "blocksBroken", "wool_players"),
                        getInt(player, "powerUpsGotten", "wool_players"),
                        getInt(player, "wins", "wool_players"),
                        getInt(player, "played", "wool_players"),
                        getInt(player, "kills", "wool_players"),
                        getInt(player, "deaths", "wool_players"), false);
            } else {
                woolPlayer = new WoolPlayer(player);
            }

            woolPlayer.load();
            return woolPlayer;
        });
    }

    @Override
    public void savePlayer(WoolPlayer woolPlayer) {
        Player player = woolPlayer.toBukkitPlayer();
        if (isPlayerPresentOnDB(player, "wool_players")) {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("UPDATE wool_players SET " +
                        "username='" + woolPlayer.getName() + "', " +
                        "woolPlaced='" + woolPlayer.getWoolPlaced() + "', " +
                        "blocksBroken='" + woolPlayer.getBlocksBroken() + "', " +
                        "powerUpsGotten='" + woolPlayer.getPowerUpsGotten() + "', " +
                        "wins='" + woolPlayer.getWins() + "', " +
                        "played='" + woolPlayer.getPlayed() + "', " +
                        "kills='" + woolPlayer.getKills() + "', " +
                        "deaths='" + woolPlayer.getDeaths() + "' WHERE uuid='" + player.getUniqueId().toString() + "'");

                statement.executeUpdate("UPDATE wool_kits SET " +
                        "username='" + woolPlayer.getName() + "', " +
                        "tank_kit='" + woolPlayer.getKitLayout(PlayableClassType.TANK) + "', " +
                        "assault_kit='" + woolPlayer.getKitLayout(PlayableClassType.ASSAULT) + "', " +
                        "archer_kit='" + woolPlayer.getKitLayout(PlayableClassType.ARCHER) + "', " +
                        "swordman_kit='" + woolPlayer.getKitLayout(PlayableClassType.SWORDMAN) + "', " +
                        "golem_kit='" + woolPlayer.getKitLayout(PlayableClassType.GOLEM) + "', " +
                        "engineer_kit='" + woolPlayer.getKitLayout(PlayableClassType.ENGINEER) +
                        "' WHERE uuid='" + player.getUniqueId().toString() + "'");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("INSERT INTO wool_players (uuid, username, woolPlaced, blocksBroken, powerUpsGotten, " +
                        "wins, played, kills, deaths) " + "VALUES ('" +
                        player.getUniqueId().toString() +
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
                        player.getUniqueId().toString() +
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
    }

    @Override
    public DataProviderType getType() {
        return DataProviderType.SQLITE;
    }

    private boolean isPlayerPresentOnDB(Player player, String table) {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + table + " WHERE uuid='" + player.getUniqueId().toString() + "'");
            boolean isPresent = resultSet.next();

            resultSet.close();
            statement.close();

            return isPresent;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private int getInt(Player player, String column, String table) {
        try (Statement statement = connection.createStatement()) {
            ResultSet set = statement.executeQuery("SELECT * FROM " + table + " WHERE uuid='" + player.getUniqueId().toString() + "' LIMIT 1");
            set.next();
            int value = set.getInt(column);

            set.close();
            statement.close();

            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private String[] getStrings(Player player, String table, String... columns) {
        try (Statement statement = connection.createStatement()) {
            ResultSet set = statement.executeQuery("SELECT * FROM " + table + " WHERE uuid=" + player.getUniqueId().toString() + " LIMIT 1");
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
