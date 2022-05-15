package me.buzz.woolwars.game.utils.structures;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.buzz.woolwars.game.utils.ItemBuilder;
import me.buzz.woolwars.game.utils.StringsUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
public class WoolItem {

    private String name;
    private List<String> lines;

    public ItemStack toItemStack(Material material) {
        List<String> l = new ArrayList<>();
        for (String line : lines) l.add(StringsUtils.colorize(line));
        return new ItemBuilder(material).setName(name).setLore(l).build();
    }

}
