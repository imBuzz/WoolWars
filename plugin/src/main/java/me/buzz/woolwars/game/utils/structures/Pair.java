package me.buzz.woolwars.game.utils.structures;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Pair<T, K> {
    private T key;
    private K value;
}
