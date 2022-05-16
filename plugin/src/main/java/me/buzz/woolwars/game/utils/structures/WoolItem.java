package me.buzz.woolwars.game.utils.structures;

import lombok.Data;
import lombok.NoArgsConstructor;
import me.buzz.woolwars.game.utils.ItemBuilder;
import me.buzz.woolwars.game.utils.StringsUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
public class WoolItem {

    private String name;
    private List<String> lines;

    public ItemStack toItemStack(Material material) {
        List<String> l = new ArrayList<>();
        for (String line : lines) l.add(StringsUtils.colorize(line));
        return new ItemBuilder(material).setName(name).setLore(l).build();
    }

    public static WoolItem from(String name, List<String> lines) {
        WoolItem item = new WoolItem();
        item.setName(name);
        item.setLines(lines);
        return item;
    }

}
