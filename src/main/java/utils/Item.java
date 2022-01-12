package utils;

import net.kyori.adventure.text.Component;
import orbrpg.OrbRPG;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Item {
    Item() { throw new IllegalStateException("Utility class"); }
    public static ItemStack getItem(String itemId) {
        if (!OrbRPG.getInstance().getItemDatabase().contains(itemId))
            itemId = "default";
        var i = OrbRPG.getInstance().getItemDatabase().getItemStack(itemId);
        if (i == null)
            return OrbRPG.getInstance().getItemDatabase().getItemStack("default");
        i.setAmount(1);
        return i;
    }
    public static ItemStack refreshItem(ItemStack item) {
        if (item == null || item.getType().isAir())
            return null;
        String itemID = getIDOfItem(item);
        if (itemID == null)
            return item;
        var newItem = OrbRPG.getInstance().getItemDatabase().getItemStack(itemID, item);
        newItem.setAmount(item.getAmount());
        return newItem;
    }
    public static void refreshInventory(Player p) {
        PlayerInventory inv = p.getInventory();
        for (var i = 0; i < 35; i++) {
            if (inv.getItem(i) == null)
                continue;
            p.getInventory().setItem(i, refreshItem(inv.getItem(i)));
        }
        p.getInventory().setHelmet(refreshItem(inv.getHelmet()));
        p.getInventory().setChestplate(refreshItem(inv.getChestplate()));
        p.getInventory().setLeggings(refreshItem(inv.getLeggings()));
        p.getInventory().setBoots(refreshItem(inv.getBoots()));
        p.getInventory().setItemInOffHand(refreshItem(inv.getItemInOffHand()));
    }
    public static float getFloatFromItem(ItemStack item, String nameSpace) {
        if (item == null)
            return 0F;
        var itemMeta = item.getItemMeta();
        if (itemMeta == null)
            return 0F;
        var container = itemMeta.getPersistentDataContainer();
        Float returnValue = container.get(new NamespacedKey(OrbRPG.getInstance(), nameSpace), PersistentDataType.FLOAT);
        if (returnValue == null)
            returnValue = 0F;
        return returnValue;
    }
    public static String getIDOfItem(ItemStack item) {
        if (item == null)
            return "default";
        var itemMeta = item.getItemMeta();
        if (itemMeta == null)
            return "default";
        var container = itemMeta.getPersistentDataContainer();
        return container.get(new NamespacedKey(OrbRPG.getInstance(), "item_id"), PersistentDataType.STRING);
    }
    public static String getTypeOfItem(ItemStack item) {
        if (item == null)
            return null;
        var itemMeta = item.getItemMeta();
        if (itemMeta == null)
            return null;
        var container = itemMeta.getPersistentDataContainer();
        return container.get(new NamespacedKey(OrbRPG.getInstance(), "item_type"), PersistentDataType.STRING);
    }
    public static PersistentDataContainer getDataOfItem(ItemStack item) {
        if (item == null)
            return null;
        var itemMeta = item.getItemMeta();
        if (itemMeta == null)
            return null;
        return itemMeta.getPersistentDataContainer();
    }

    public static ItemStack hideAttributes(ItemStack item) {
        ItemStack newItem;
        newItem = item;
        var itemMeta = newItem.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        itemMeta.addItemFlags(ItemFlag.HIDE_DYE);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        itemMeta.setUnbreakable(true);
        newItem.setItemMeta(itemMeta);
        return newItem;
    }
    public static @NotNull ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        var item = new ItemStack(material, 1);
        final var meta = item.getItemMeta();
        meta.displayName(Component.text(Misc.coloured(name)));
        var colouredLore = new ArrayList<String>();
        for (String l : lore)
            colouredLore.add(Misc.coloured(l));
        meta.setLore(colouredLore);
        item.setItemMeta(meta);
        return hideAttributes(item);
    }
}
