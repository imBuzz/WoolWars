package me.buzz.woolwars.game.data.credentials;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class DatabaseCredentials {

    private String address;
    private int host;
    private String database, username, password;


    public static DatabaseCredentials from(String address, int host, String database, String username, String password) {
        DatabaseCredentials credentials = new DatabaseCredentials();

        credentials.setAddress(address);
        credentials.setHost(host);
        credentials.setDatabase(database);
        credentials.setUsername(username);
        credentials.setPassword(password);

        return credentials;
    }

}
