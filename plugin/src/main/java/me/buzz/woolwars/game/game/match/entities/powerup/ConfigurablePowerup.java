package me.buzz.woolwars.game.game.match.entities.powerup;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ConfigurablePowerup {

    private String holoName;
    private String pickupMessage;

    public static ConfigurablePowerup from(String holoName, String message) {
        ConfigurablePowerup powerup = new ConfigurablePowerup();
        powerup.setHoloName(holoName);
        powerup.setPickupMessage(message);
        return powerup;
    }

}
