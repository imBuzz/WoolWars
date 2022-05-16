package me.buzz.woolwars.game.utils.structures;

import lombok.Data;
import lombok.NoArgsConstructor;
import me.buzz.woolwars.game.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@NoArgsConstructor
@Data
public class WoolItem {

    private String name;
    private List<String> lines;

    public ItemStack toItemStack(Material material) {
        return new ItemBuilder(material).setName(name).setLore(lines).build();
    }

    public static WoolItem from(String name, List<String> lines) {
        WoolItem item = new WoolItem();
        item.setName(name);
        item.setLines(lines);
        return item;
    }

}
