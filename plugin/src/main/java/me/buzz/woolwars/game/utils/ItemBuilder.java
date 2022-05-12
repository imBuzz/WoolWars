package me.buzz.woolwars.game.utils;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;

import java.util.*;

public class ItemBuilder {

    private final ItemStack item;
    private final ItemMeta meta;

    public ItemBuilder(Material material, int amount, short damage) {
        this.item = new ItemStack(material, amount, damage);
        this.meta = this.item.getItemMeta();
    }

    public ItemBuilder(Material material, int amount) {
        this(material, amount, (short) 0);
    }

    public ItemBuilder(Material material) {
        this(material, 1, (short) 0);
    }

    public ItemBuilder(ItemStack item) {
        this.item = item;
        this.meta = this.item.getItemMeta();
    }

    public ItemBuilder(ItemStack item, int amount) {
        this.item = item;
        if (amount > 64 || amount < 0) amount = 64;
        this.item.setAmount(amount);
        this.meta = this.item.getItemMeta();
    }

    public ItemBuilder setName(String name) {
        meta.setDisplayName(StringsUtils.colorize(name));
        return this;
    }

    public ItemBuilder setDurability(short damage) {
        item.setDurability(damage);
        return this;
    }

    public List<String> getLore() {
        return meta.getLore();
    }

    public ItemBuilder setLore(List<String> lore) {
        meta.setLore(StringsUtils.colorize(lore));
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        return setLore(Arrays.asList(lore));
    }

    public ItemBuilder addLore(List<String> lore) {
        List<String> newLore = meta.getLore() == null ? new ArrayList<>() : meta.getLore();
        newLore.addAll(StringsUtils.colorize(lore));
        meta.setLore(newLore);
        return this;
    }

    public ItemBuilder addLore(String... lore) {
        return addLore(Arrays.asList(lore));
    }

    public Map<Enchantment, Integer> getEnchantments() {
        return Collections.unmodifiableMap(meta.getEnchants());
    }

    public ItemBuilder setFlags(ItemFlag... flags) {
        for (ItemFlag flag : flags) {
            meta.addItemFlags(flags);
        }
        return this;
    }

    public ItemBuilder addEnchant(Enchantment enchantment, int level) {
        meta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemBuilder setPlayerSkull(String playerName) {
        SkullMeta meta = (SkullMeta) this.meta;
        meta.setOwner(playerName);
        return this;
    }

    public ItemBuilder spawner(EntityType entityType) {
        BlockStateMeta blockMeta = (BlockStateMeta) meta;
        CreatureSpawner spawner = (CreatureSpawner) blockMeta.getBlockState();

        spawner.setSpawnedType(entityType);
        blockMeta.setBlockState(spawner);
        return this;
    }

    public ItemBuilder setUnbreakable(boolean state) {
        meta.spigot().setUnbreakable(state);
        return this;
    }

    public ItemBuilder setLeatherColor(Color color) {
        LeatherArmorMeta armorMeta = (LeatherArmorMeta) meta;
        armorMeta.setColor(color);
        return this;
    }

    public BookItemMeta book() {
        return new BookItemMeta();
    }

    public ItemStack build() {
        item.setItemMeta(meta);
        return item;
    }

    public class BookItemMeta {
        private final BookMeta meta = (BookMeta) ItemBuilder.this.meta;

        public BookItemMeta setTitle(String title) {
            meta.setTitle(StringsUtils.colorize(title));
            return this;
        }

        public BookItemMeta setAuthor(String author) {
            meta.setAuthor(author);
            return this;
        }

        public BookItemMeta setPages(String... pages) {
            meta.setPages(StringsUtils.colorize(pages));
            return this;
        }

        public BookItemMeta setPages(List<String> pages) {
            meta.setPages(StringsUtils.colorize(pages));
            return this;
        }

        public ItemBuilder build() {
            ItemBuilder.this.item.setItemMeta(meta);
            return ItemBuilder.this;
        }
    }
}
