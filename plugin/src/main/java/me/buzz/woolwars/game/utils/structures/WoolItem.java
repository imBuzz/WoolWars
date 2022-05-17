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

    private Material material;
    private String name;
    private List<String> lines;

    public static WoolItem from(Material material, String name, List<String> lines) {
        WoolItem item = new WoolItem();
        item.setMaterial(material);
        item.setName(name);
        item.setLines(lines);
        return item;
    }

    public ItemStack toItemStack() {
        return new ItemBuilder(material).setName(name).setLore(lines).build();
    }

}
