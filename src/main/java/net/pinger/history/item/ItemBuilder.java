package net.pinger.history.item;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * This class is a utility class mainly used for easier
 * creation of different ItemStacks.
 * <p>
 * Using this class, you can make your code even more readable
 * and also do it in as little operations as possible.
 *
 * @since 2.0
 * @author Pinger
 */

public class ItemBuilder {

    private String displayName;
    private List<String> lore;
    private Map<Enchantment, Integer> enchantments;
    private List<ItemFlag> itemFlags;

    /**
     * This is the {@link ItemStack} we're performing the changes to.
     */

    private final ItemStack item;

    /**
     * Constructs a new {@link ItemBuilder} with the
     * given {@link ItemStack} to modify.
     *
     * @param item the item to modify
     */

    public ItemBuilder(ItemStack item) {
        this.item = item;

        // Instantiate some fields
        this.enchantments = new HashMap<>();
        this.itemFlags = new ArrayList<>();
        this.lore = new ArrayList<>();
    }

    /**
     * Constructs a new {@link ItemBuilder} with the
     * given {@link ItemStack} contents.
     *
     * @param material the material type
     * @param amount the amount of the {@link ItemStack}
     * @param data the data of the {@link ItemStack}
     */

    public ItemBuilder(Material material, int amount, short data) {
        this(new ItemStack(material, amount, data));
    }

    /**
     * Constructs a new {@link ItemBuilder} with the
     * given material and its' amount.
     *
     * @param material the material
     * @param amount the amount
     */

    public ItemBuilder(Material material, int amount) {
        this(new ItemStack(material, amount));
    }

    /**
     * Constructs a new {@link ItemBuilder} with the
     * given material and the material data.
     *
     * @param material the material
     * @param data the material data
     */

    public ItemBuilder(Material material, short data) {
        this(new ItemStack(material, 1, data));
    }

    /**
     * Constructs a new {@link ItemBuilder} with the
     * given material.
     *
     * @param material the material
     */

    public ItemBuilder(Material material) {
        this(new ItemStack(material));
    }

    /**
     * Constructs a new {@link ItemBuilder} with the
     * given {@link XMaterial}.
     *
     * @param material the material
     */

    public ItemBuilder(XMaterial material) {
        this(material.parseItem());
    }

    /**
     * This method sets the display name of
     * the {@link ItemStack} instance.
     *
     * @param displayName the display name
     * @return this instance
     */

    public ItemBuilder name(String displayName) {
        this.displayName = displayName;
        return this;
    }

    /**
     * This method overrides the lore
     * of the {@link ItemStack} with the specified lore.
     *
     * @param lore the lore to override
     * @return this instance
     */

    public ItemBuilder lore(String... lore) {
        // Allow items to be added to this
        // ArrayList again
        this.lore = new ArrayList<>(Arrays.asList(lore));
        return this;
    }

    /**
     * This method overrides the lore
     * of the {@link ItemStack} with the specified lore.
     *
     * @param lore the lore to override
     * @return this instance
     */


    public ItemBuilder lore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    /**
     * This method adds lore to the current
     * lore of the {@link ItemStack}.
     *
     * @param lore the lore to add
     * @return this instance
     */

    public ItemBuilder addLore(List<String> lore) {
        if (this.lore != null) {
            this.lore.addAll(lore);
        }

        return this;
    }

    /**
     * This method adds lore to the current
     * lore of the {@link ItemStack}.
     *
     * @param lore the lore to add
     * @return this instance
     */

    public ItemBuilder addLore(String... lore) {
        return this.addLore(Arrays.asList(lore));
    }

    /**
     * This method adds flags to the {@link ItemStack}.
     *
     * @param flags the flags to add
     * @return this instance
     */

    public ItemBuilder flag(ItemFlag... flags) {
        this.itemFlags.addAll(Arrays.asList(flags));
        return this;
    }

    /**
     * This method adds all flags from the {@link ItemFlag} enum
     * to the item.
     *
     * @return this instance
     */

    public ItemBuilder flag() {
        for (ItemFlag flag : ItemFlag.values()) {
            this.flag(flag);
        }

        return this;
    }

    /**
     * This method adds a leveled enchantment to the {@link ItemStack}.
     *
     * @param enchantment the enchantment to add
     * @param level the level of the enchantment
     * @return this instance
     */

    public ItemBuilder enchant(Enchantment enchantment, int level) {
        this.enchantments.put(enchantment, level);
        return this;
    }

    /**
     * This method adds an enchantment to the {@link ItemStack}.
     *
     * @param enchantment the enchantment to add
     * @return this instance
     */

    public ItemBuilder enchant(Enchantment enchantment) {
        return this.enchant(enchantment, 1);
    }

    /**
     * This method builds this {@link ItemBuilder} into a
     * {@link ItemStack} instance while taking all specified
     * fields into account.
     *
     * @return the built {@link ItemStack}
     */

    public ItemStack build() {
        ItemMeta meta = this.item.getItemMeta();

        // Check if the item meta is null
        // This will only happen if the Material#getType is Material.AIR
        if (meta == null) {
            return this.item;
        }

        // Set the display name of the item
        // If it isn't null
        if (this.displayName != null) {
            meta.setDisplayName(this.displayName);
        }

        // Set the lore of the item
        // If it isn't null or empty
        // Developers mostly use this for description
        // Of the item
        if (this.lore != null && !this.lore.isEmpty()) {
            meta.setLore(this.lore);
        }

        // Add item flags to this item
        meta.addItemFlags(this.itemFlags.toArray(new ItemFlag[0]));

        // Add all needed enchantments
        // We prefer to use unsafeEnchantments
        this.item.addUnsafeEnchantments(this.enchantments);
        this.item.setItemMeta(meta);
        return this.item;
    }

}
