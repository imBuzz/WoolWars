package me.buzz.woolwars.game.utils.structures.itembuilder;

import com.hakan.core.item.ItemBuilder;
import lombok.Setter;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class LeatherItemBuilder extends ItemBuilder {

    @Setter
    Color color;

    public LeatherItemBuilder(ItemStack type) {
        super(type);
    }

    public ItemBuilder setLeatherColor(Color color) {
        this.color = color;
        return this;
    }

    @Override
    public ItemStack build() {
        ItemStack itemStack = super.build();

        LeatherArmorMeta armorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
        armorMeta.setColor(color);

        itemStack.setItemMeta(armorMeta);
        return itemStack;
    }
}
