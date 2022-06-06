package me.buzz.woolwars.game.utils.structures;

import com.cryptomorin.xseries.XMaterial;
import com.hakan.core.item.HItemBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@NoArgsConstructor
@Data
public class WoolItem {

    private Material material;
    private String name;
    private List<String> lines;

    public static WoolItem from(String material, String name, List<String> lines) {
        WoolItem item = new WoolItem();
        item.setMaterial(XMaterial.valueOf(material.toUpperCase()).parseMaterial());
        item.setName(name);
        item.setLines(lines);
        return item;
    }

    public ItemStack toItemStack() {
        return new HItemBuilder(material).name(name).lores(lines).build();
    }

}
