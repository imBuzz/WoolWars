package me.buzz.woolwars.game.utils.structures.itembuilder;

import com.hakan.core.item.ItemBuilder;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

public class PotionAndWoolItemBuilder extends ItemBuilder {

    private DyeColor color;

    private PotionType potionType;
    private int level;
    private boolean splash;

    public PotionAndWoolItemBuilder(Material type) {
        super(type);
    }

    public PotionAndWoolItemBuilder(ItemStack itemStack) {
        super(itemStack);
    }

    public ItemBuilder setColor(DyeColor color) {
        this.color = color;
        return this;
    }

    public ItemBuilder potion(PotionType potionType, int level, boolean splash) {
        this.potionType = potionType;
        this.level = level;
        this.splash = splash;

        return this;
    }

    @Override
    public @NotNull ItemStack build() {
        ItemStack stack;

        if (potionType != null) {
            Potion potion = new Potion(potionType);
            potion.setLevel(level);
            if (splash) potion.splash();
            stack = potion.toItemStack(getAmount());
        } else {
            stack = new Wool(color).toItemStack();
            stack.setAmount(getAmount());
        }

        stack = getNbtManager().set(stack, getNbt());

        ItemMeta meta = stack.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(getName());
            meta.setLore(getLore());
            getItemFlags().forEach(meta::addItemFlags);
            getEnchants().forEach((key, value) -> meta.addEnchant(key, value, true));
            if (isGlow()) meta.addEnchant(getGlowEnchantment(), 0, true);
            stack.setItemMeta(meta);
        }

        return stack;
    }
}
