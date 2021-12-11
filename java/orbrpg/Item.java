package orbrpg;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class Item {
    public static ItemStack refreshItem(ItemStack item) {
        if (item == null) return null;
        String itemID = getIDOfItem(item);
        if (itemID == null) return item;
        return OrbRPG.getInstance().getItemDatabase().getItemStack(itemID);
    }
    public static float getFloatFromItem(ItemStack item, String nameSpace) {
        if (item == null) return 0f;
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return 0f;
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        Float returnValue = container.get(new NamespacedKey(OrbRPG.getInstance(), nameSpace), PersistentDataType.FLOAT);
        if (returnValue == null) returnValue = 0f;
        return returnValue;
    }
    public static String getIDOfItem(ItemStack item) {
        if (item == null) return null;
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return null;
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        return container.get(new NamespacedKey(OrbRPG.getInstance(), "item_id"), PersistentDataType.STRING);
    }
    public static String getTypeOfItem(ItemStack item) {
        if (item == null) return null;
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return null;
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        return container.get(new NamespacedKey(OrbRPG.getInstance(), "item_type"), PersistentDataType.STRING);
    }
    public static boolean isCustomItem(ItemStack item) {
        if (item == null) return false;
        String itemID = getIDOfItem(item);
        if (itemID == null) return false;
        return OrbRPG.getInstance().getItemDatabase().contains(itemID);
    }
}
